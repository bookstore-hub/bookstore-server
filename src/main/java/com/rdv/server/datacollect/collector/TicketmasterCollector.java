package com.rdv.server.datacollect.collector;

import com.rdv.server.datacollect.builder.FeignClientBuilder;
import com.rdv.server.datacollect.client.TicketmasterClient;
import com.rdv.server.datacollect.mapper.ticketmaster.TicketmasterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class TicketmasterCollector {

    private final String ticketmasterURL;

    public TicketmasterCollector(@Value("${dataCollect.montreal.ticketmasterURL}") String ticketmasterURL) {
        this.ticketmasterURL = ticketmasterURL;
    }


    public TicketmasterObject getMontrealEvents() {
        TicketmasterClient ticketmasterClient = FeignClientBuilder.createClient(TicketmasterClient.class, ticketmasterURL);
        return ticketmasterClient.findAllMontrealEvents();
    }


        /** Note: Longer process for debugs
         try {
            feign.Response response = ticketmasterClient.findAllMontrealEventsAsString();
            String json = IOUtils.toString(response.body().asInputStream(), Charsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            TicketmasterObject ticketmasterObject = objectMapper.readValue(json, TicketmasterObject.class);
            System.out.println("TicketmasterObject random content: " + ticketmasterObject.get_embedded().getEvents().get(0).getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        **/

}
