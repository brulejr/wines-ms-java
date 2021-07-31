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
package io.jrb.labs.winesms.web;

import io.jrb.labs.common.resource.Projection;
import io.jrb.labs.common.web.RouteHandler;
import io.jrb.labs.winesms.resource.AddWine;
import io.jrb.labs.winesms.resource.WineResource;
import io.jrb.labs.winesms.service.command.CreateWineCommand;
import io.jrb.labs.winesms.service.command.FindWineCommand;
import io.jrb.labs.winesms.service.command.GetWinesCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Validator;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class WineHandler implements RouteHandler {

    private final CreateWineCommand createWineCommand;
    private final FindWineCommand findWineCommand;
    private final GetWinesCommand getWinesCommand;
    private final Validator validator;

    public WineHandler(
            final CreateWineCommand createWineCommand,
            final FindWineCommand findWineCommand,
            final GetWinesCommand getWinesCommand,
            final Validator validator
    ) {
        this.createWineCommand = createWineCommand;
        this.findWineCommand = findWineCommand;
        this.getWinesCommand = getWinesCommand;
        this.validator = validator;
    }

    public Mono<ServerResponse> createWine(final ServerRequest serverRequest) {
        return requireValidBody((final Mono<AddWine> addWineMono) ->
            addWineMono.flatMap(wine -> {
                final Mono<WineResource> wineResourceMono = createWineCommand.execute(wine);
                return ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .hint(Jackson2CodecSupport.JSON_VIEW_HINT, Projection.Detail.class)
                        .body(wineResourceMono, WineResource.class);
            }), serverRequest, AddWine.class, validator);
    }

    public Mono<ServerResponse> findWine(final ServerRequest serverRequest) {
        final String wineGuid = serverRequest.pathVariable("guid");
        final Mono<WineResource> wineResourceMono = findWineCommand.execute(wineGuid);
        return wineResourceMono.flatMap(wine ->
                ServerResponse.ok()
                        .hint(Jackson2CodecSupport.JSON_VIEW_HINT, Projection.Detail.class)
                        .body(fromValue(wine)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getAllWines(final ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .hint(Jackson2CodecSupport.JSON_VIEW_HINT, Projection.Summary.class)
                .body(getWinesCommand.execute(null), WineResource.class);
    }

}
