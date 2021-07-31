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

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class WineRoutes {

    private final WineHandler wineHandler;

    public WineRoutes(final WineHandler wineHandler) {
        this.wineHandler = wineHandler;
    }

    public RouterFunction<ServerResponse> routes() {
        return route()
                .add(createWineRoute())
                .add(findWineRoute())
                .add(retrieveWinesRoute())
                .build();
    }

    private RouterFunction<ServerResponse> createWineRoute() {
        return route().POST("/wines", wineHandler::createWine)
                .build();
    }

    private RouterFunction<ServerResponse> findWineRoute() {
        return route().GET("/wines/{guid}", wineHandler::findWine)
                .build();
    }

    private RouterFunction<ServerResponse> retrieveWinesRoute() {
        return route().GET("/wines", wineHandler::getAllWines)
                .build();
    }

}
