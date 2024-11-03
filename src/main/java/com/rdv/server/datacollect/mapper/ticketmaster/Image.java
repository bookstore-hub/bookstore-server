package com.rdv.server.datacollect.mapper.ticketmaster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
        private String ratio;
        private String url;
        private float width;
        private float height;
        private boolean fallback;
}
