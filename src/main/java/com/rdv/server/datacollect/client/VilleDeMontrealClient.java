package com.rdv.server.datacollect.client;

import com.rdv.server.datacollect.mapper.VilleDeMontrealMapper;
import feign.Headers;
import feign.RequestLine;


public interface VilleDeMontrealClient {

    @RequestLine("GET")
    @Headers("Content-Type: application/json")
    VilleDeMontrealMapper findAllMontrealEvents();

}
