package com.vertx.services;

import com.vertx.models.Tweet;

public class TweetService extends MongoService<Tweet> {

    TweetService() {
        super("tweets");
    }
}
