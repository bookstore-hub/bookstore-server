package com.rdv.server.datacollect.builder;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;


public class FeignClientBuilder {
    //private FeignClient bookClient = createClient(BookClient.class, "http://localhost:8081/api/books");

    public static <T> T createClient(Class<T> type, String uri) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(type))
                .logLevel(Logger.Level.FULL)
                .target(type, uri);
    }

}
