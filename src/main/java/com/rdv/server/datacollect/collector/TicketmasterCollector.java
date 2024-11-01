package com.rdv.server.datacollect.collector;

import com.rdv.server.datacollect.client.TicketmasterClient;
import com.rdv.server.datacollect.to.TicketmasterTo;
import feign.Feign;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TicketmasterCollector {


    private final TicketmasterClient ticketmasterClient; //todo tester !!

    public TicketmasterCollector(@Value("${dataCollect.montreal.ticketmasterURL}") String ticketmasterURL) {

        ticketmasterClient = Feign.builder()
                //.client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                //.decoder(new JacksonDecoder())
                //.logger(new Slf4jLogger(VilleDeMontrealClient.class))
                .target(TicketmasterClient.class, ticketmasterURL);
    }

    public TicketmasterTo getMontrealEvents() {
        /*TicketmasterClient ticketmasterClient = Feign.builder()
                //.client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                //.decoder(new JacksonDecoder())
                //.logger(new Slf4jLogger(VilleDeMontrealClient.class))
                .target(TicketmasterClient.class, ticketmasterURL);*/

        return ticketmasterClient.findAllMontrealEvents();
    }

}
