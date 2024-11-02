package com.rdv.server.datacollect.collector;

import com.rdv.server.datacollect.client.TicketmasterClient;
import com.rdv.server.datacollect.mapper.TicketmasterMapping;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


import static org.junit.jupiter.api.Assertions.*;

class TicketmasterCollectorTest {

    @Mock
    private TicketmasterClient ticketmasterClient;

    @Test
    public void testTicketmasterClient() throws Exception {
        TicketmasterMapping ticketmasterMapping = ticketmasterClient.findAllMontrealEvents();

        //int sizeEvents = ticketmasterMapping.get_embeddedObject();

        //assertTrue(books.size() > 2);
    }

}