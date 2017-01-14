package com.vertx.lib;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class App extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut){
        String env = (System.getenv("ENV") != null) ? System.getenv("ENV") : "LOCAL";
        Config.getInstance(config().getJsonObject(env).toString());
        vertx
                .createHttpServer()
                .requestHandler(new Routes().getRoutes(vertx)::accept)
                .listen(Config.getInt("http.port"), result -> {
                    if (result.succeeded()) {
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                });


    }
}
