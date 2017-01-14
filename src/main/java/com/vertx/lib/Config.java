package com.vertx.lib;

import io.vertx.core.json.JsonObject;

public class Config {
    private static Config instance = null;
    private static JsonObject config;

    public static Config getInstance(String json) {
        if(instance == null){
            instance = new Config();
            config = new JsonObject(json);
        }
        return instance;
    }

    public static String getString(String key){
        return config.getString(key);
    }

    public static Integer getInt(String key){
        return config.getInteger(key);
    }

    public static JsonObject getObject(String key){
        return config.getJsonObject(key);
    }


}