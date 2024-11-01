package com.rdv.server.datacollect.client;

import com.rdv.server.datacollect.to.VilleDeMontrealTo;
import feign.Headers;
import feign.RequestLine;


public interface VilleDeMontrealClient {

    @RequestLine("GET")
    @Headers("Content-Type: application/json")
    VilleDeMontrealTo findAllMontrealEvents();

}
