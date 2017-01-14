package com.vertx.controllers;

import com.google.inject.Inject;
import com.vertx.models.Product;
import com.vertx.services.ProductService;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class ProductController {
    @Inject
    private ProductService service;

    public void index(RoutingContext routingContext) {
        service.get().setHandler(r -> {
            routingContext
                    .response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(r.result().toString());
        });
    }

    public void show(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        service.get(id).setHandler(r -> {
            routingContext
                    .response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(r.result().toString());
        });
    }

    public void create(RoutingContext routingContext) {
        final Product product = Json.decodeValue(routingContext.getBodyAsString(), Product.class);
        service.save(product).setHandler(r -> {
            routingContext
                    .response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(r.result().toString());
        });
    }

    public void update(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        final Product p = Json.decodeValue(routingContext.getBodyAsString(), Product.class);
        service.get(id).setHandler(r -> {
            Product product = Json.decodeValue(r.result().toString(), Product.class);
            product.setTitle(p.getTitle());
            product.setDescription(p.getDescription());
            product.setPrice(p.getPrice());
            service.save(product).setHandler( res -> {
                routingContext
                        .response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(r.result().toString());
            });
        });
    }

    public void destroy(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        service.delete(id).setHandler(r -> {
            routingContext
                    .response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(r.result().toString());
        });
    }

}
