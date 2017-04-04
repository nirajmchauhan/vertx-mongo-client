package com.vertx.controllers;

import com.google.inject.Inject;
import com.vertx.models.Tweet;
import com.vertx.services.TweetService;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class TweetController {

    @Inject
    private TweetService service;

    public void index(RoutingContext routingContext) {
        service.get().setHandler(r -> routingContext.response().end(r.result().toString()));
    }

    public void show(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        service.get(id).setHandler(r -> routingContext.response().end(r.result().toString()));
    }

    public void create(RoutingContext routingContext) {
        final Tweet tweet = Json.decodeValue(routingContext.getBodyAsString(), Tweet.class);
        service.save(tweet).setHandler(r -> routingContext.response().end(r.result().toString()));
    }

    public void update(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        final Tweet t = Json.decodeValue(routingContext.getBodyAsString(), Tweet.class);
        service.get(id).setHandler(r -> {
            Tweet tweet = Json.decodeValue(r.result().toString(), Tweet.class);
            tweet.setTweet(t.getTweet());
            service.save(tweet).setHandler(res -> routingContext.response().end(r.result().toString()));
        });
    }

    public void destroy(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        service.delete(id).setHandler(r -> routingContext.response().end(r.result().toString()));
    }

}
