package com.vertx.lib;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

public class AppInjector extends AbstractModule {
    private Vertx vertx;

    public AppInjector(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected void configure() {

        JsonObject mongoConfig = Config.getObject("mongo_config");
        MongoClient mongo = MongoClient.createShared(vertx, mongoConfig);
        bind(MongoClient.class).annotatedWith(Names.named("MongoClient")).toInstance(mongo);
    }
}
