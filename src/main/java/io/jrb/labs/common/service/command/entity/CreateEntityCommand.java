/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jon Brule <brulejr@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.jrb.labs.common.service.command.entity;

import com.mongodb.MongoWriteException;
import io.jrb.labs.common.domain.Entity;
import io.jrb.labs.common.respository.EntityRepository;
import io.jrb.labs.common.service.command.Command;
import io.jrb.labs.common.service.command.CommandException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
public abstract class CreateEntityCommand<REQ, RSP, E extends Entity<E>> implements Command<REQ, RSP> {

    private static final String UNIQUE_INDEX_ERROR = "E11000 duplicate key error collection";

    private final String entityType;
    private final Function<REQ, E> toEntityFn;
    private final Function<E, RSP> toResourceFn;
    private final EntityRepository<E> repository;

    protected CreateEntityCommand(
            final String entityType,
            final Function<REQ, E> toEntityFn,
            final Function<E, RSP> toResourceFn,
            final EntityRepository<E> repository
    ) {
        this.entityType = entityType;
        this.toEntityFn = toEntityFn;
        this.toResourceFn = toResourceFn;
        this.repository = repository;
    }

    @Override
    public Mono<RSP> execute(final REQ request) {
        return Mono.just(request)
                .map(toEntityFn)
                .map(entity -> entity.withGuid(UUID.randomUUID().toString()))
                .flatMap(repository::save)
                .map(toResourceFn)
                .onErrorResume(t -> handleException(t));
    }

    private Mono<RSP> handleException(final Throwable t) {
        final Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        if (rootCause instanceof MongoWriteException) {
            final Optional<String> message = Optional.ofNullable(rootCause).map(Throwable::getMessage);
            if (message.isPresent() && message.get().startsWith(UNIQUE_INDEX_ERROR)) {
                return Mono.error(new DuplicateEntityException(this, entityType));
            }
        }
        return Mono.error(new CommandException(
                this,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "create " + entityType,
                t
        ));
    }

}
