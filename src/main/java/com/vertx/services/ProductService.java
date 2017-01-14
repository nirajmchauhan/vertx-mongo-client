package com.vertx.services;

import com.vertx.models.Product;

public class ProductService extends MongoService<Product> {

    ProductService() {
        super("products");
    }
}
