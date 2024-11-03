package com.rdv.server.datacollect.controller;

import com.rdv.server.datacollect.collector.TicketmasterCollector;
import com.rdv.server.datacollect.mapper.ticketmaster.TicketmasterEvent;
import com.rdv.server.datacollect.mapper.ticketmaster.TicketmasterObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/test")
@Tag(name = "DataCollectTestController", description = "Set of endpoints to handle the RDV data collect tests")
public class DataCollectTestController {

    private final TicketmasterCollector ticketmasterCollector;

    public DataCollectTestController(TicketmasterCollector ticketmasterCollector) {
        this.ticketmasterCollector = ticketmasterCollector;
    }


    /**
     * Retrieves Ticketmaster events
     */
    @Operation(description = "Retrieves Ticketmaster events")
    @GetMapping(value = "/ticketmaster")
    public List<TicketmasterEvent> retrieveTicketmasterEvents() {
        TicketmasterObject ticketmasterObject = ticketmasterCollector.getMontrealData();
        return ticketmasterObject.get_embedded().getEvents();
    }

}