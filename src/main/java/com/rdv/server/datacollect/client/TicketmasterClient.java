package com.rdv.server.datacollect.client;

import com.rdv.server.datacollect.mapper.TicketmasterMapper;
import feign.Headers;
import feign.RequestLine;

public interface TicketmasterClient {

    @RequestLine("GET &city=Montreal&locale=fr")
    @Headers("Content-Type: application/json")
    TicketmasterMapper findAllMontrealEvents();

}
