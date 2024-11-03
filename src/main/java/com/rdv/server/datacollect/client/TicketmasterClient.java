package com.rdv.server.datacollect.client;

import com.rdv.server.datacollect.mapper.ticketmaster.TicketmasterObject;
import feign.Headers;
import feign.RequestLine;

public interface TicketmasterClient {

    @RequestLine("GET")
    @Headers("Content-Type: application/json")
    TicketmasterObject findAllMontrealEvents();

    @RequestLine("GET")
    @Headers("Content-Type: application/json")
    feign.Response findAllMontrealEventsAsString();

}
