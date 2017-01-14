package com.vertx.lib;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vertx.controllers.ProductController;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Routes {

    public Router getRoutes(Vertx vertx) {
        Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("Welcome");
        });

        vertx.executeBlocking(future -> {
            Injector i = Guice.createInjector(new AppInjector(vertx));

            //Product routes
            ProductController product = i.getInstance(ProductController.class);
            router.route("/api/*").handler(BodyHandler.create());
            router.get("/api/products").handler(product::index);
            router.get("/api/products/:id").handler(product::show);
            router.post("/api/products/").handler(product::create);
            router.put("/api/products/:id").handler(product::update);
            router.delete("/api/products/:id").handler(product::destroy);

            future.complete(router);

        }, res -> {
            System.out.println("Routes injected in verticle successfully");
        });

        return router;
    }
}
