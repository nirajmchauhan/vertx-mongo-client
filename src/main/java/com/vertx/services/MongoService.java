package com.vertx.services;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mongodb.MongoWriteException;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

public class MongoService<T> {
    @Inject
    @Named("MongoClient")
    private MongoClient client;

    private String documentName;

    MongoService(String documentName) {
        this.documentName = documentName;
    }

    public Future<List<JsonObject>> get() {
        Future<List<JsonObject>> future = Future.future();
        client.find(documentName, new JsonObject(), res -> {
            if (res.succeeded()) {
                future.complete(res.result());
            } else {
                future.fail(res.cause());
            }
        });

        return future;
    }

    public Future<JsonObject> get(String _id) {
        Future<JsonObject> future = Future.future();
        JsonObject query = new JsonObject().put("_id", _id);
        client.find(documentName, query, res -> {
            if (res.succeeded()) {
                List<JsonObject> objects = res.result();
                JsonObject data = null;
                if (objects.size() > 0) {
                    data = objects.get(0);
                }
                future.complete(data);
            } else {
                future.fail(res.cause());
            }
        });

        return future;
    }

    public Future<JsonObject> save(T model) {
        Future<JsonObject> future = Future.future();
        JsonObject document = new JsonObject(Json.encode(model));
        client.save(documentName, document, res -> {
            if (res.succeeded()) {
                if (document.getString("_id") == null) {
                    document.put("_id", res.result());
                }
                future.complete(document);
            } else {
                if (res.cause() instanceof MongoWriteException && ((MongoWriteException) res.cause()).getCode() == 11000) {
                    future.fail("Duplicate entry not allowed");
                }else{
                    future.fail(res.cause());
                }
            }
        });
        return future;
    }

    public Future<Boolean> delete(String _id) {
        Future<Boolean> future = Future.future();
        JsonObject query = new JsonObject().put("_id", _id);
        client.removeDocument(documentName, query, r -> {
            if (r.succeeded()) {
                future.complete(true);
            } else {
                future.fail(r.cause());
            }
        });
        return future;
    }

}
