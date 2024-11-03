package com.rdv.server.datacollect.mapper.ticketmaster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Classification {
        private boolean primary;
        Segment Segment;
        Genre Genre;
        SubGenre SubGenre;
        Type Type;
        SubType SubType;
        private boolean family;
}
