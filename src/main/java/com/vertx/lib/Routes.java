package com.vertx.lib;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.vertx.controllers.TweetController;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

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

            //Tweet routes
            TweetController tweet = i.getInstance(TweetController.class);
            router.route("/v1/api/*").handler(BodyHandler.create());
            router.get("/v1/api/tweets").handler(tweet::index);
            router.get("/v1/api/tweets/:id").handler(tweet::show);
            router.post("/v1/api/tweets/").handler(tweet::create);
            router.put("/v1/api/tweets/:id").handler(tweet::update);
            router.delete("/v1/api/tweets/:id").handler(tweet::destroy);

            future.complete(router);

        }, res -> {
            ServiceDiscovery discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
                    .setBackendConfiguration(
                            new JsonObject()
                                    .put("host", "127.0.0.1")
                                    .put("key", "ServiceDiscovery")
                    ));
            discovery.publish(HttpEndpoint.createRecord(
                    "tweets",
                    "localhost", Config.getInt("http.port"),
                    "/v1/api/"),
                    ar -> {
                        if (ar.succeeded()) {
                            System.out.println("REST API published");
                        } else {
                            System.out.println("Unable to publish the REST API: " +
                                    ar.cause().getMessage());
                        }
                    });

            System.out.println("Routes injected in verticle successfully");
        });

        return router;
    }
}
