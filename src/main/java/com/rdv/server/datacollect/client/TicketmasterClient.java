package com.rdv.server.datacollect.client;

import com.rdv.server.datacollect.mapper.TicketmasterMapping;
import feign.Headers;
import feign.RequestLine;

public interface TicketmasterClient {

    @RequestLine("GET &city=Montreal&locale=fr")
    @Headers("Content-Type: application/json")
    TicketmasterMapping findAllMontrealEvents();

}
