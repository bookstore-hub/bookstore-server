package com.rdv.server.datacollect.mapper.ticketmaster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceRange {
        private String type;
        private String currency;
        private float min;
        private float max;
}
