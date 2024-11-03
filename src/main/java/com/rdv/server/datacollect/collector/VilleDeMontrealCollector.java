package com.rdv.server.datacollect.collector;

import com.rdv.server.datacollect.builder.FeignClientBuilder;
import com.rdv.server.datacollect.client.TicketmasterClient;
import com.rdv.server.datacollect.client.VilleDeMontrealClient;
import com.rdv.server.datacollect.mapper.VilleDeMontrealMapper;
import com.rdv.server.datacollect.mapper.ticketmaster.TicketmasterObject;
import feign.Feign;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VilleDeMontrealCollector {

    private final String villeDeMontrealURL;

    public VilleDeMontrealCollector(@Value("${dataCollect.montreal.villeDeMontrealURL}") String villeDeMontrealURL) {
        this.villeDeMontrealURL = villeDeMontrealURL;
    }


    public VilleDeMontrealMapper getMontrealEvents() {
        VilleDeMontrealClient villeDeMontrealClient = FeignClientBuilder.createClient(VilleDeMontrealClient.class, villeDeMontrealURL);
        return villeDeMontrealClient.findAllMontrealEvents();
    }

}
