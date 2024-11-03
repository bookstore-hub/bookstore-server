package com.rdv.server.datacollect.mapper.ticketmaster;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketmasterObject {
    _embedded _embedded;
    _links _links;
    Page Page;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Page {
    private float size;
    private float totalElements;
    private float totalPages;
    private float number;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class _links {
    First FirstObject;
    Self SelfObject;
    Next NextObject;
    Last LastObject;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Last {
    private String href;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Next {
    private String href;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Self {
    private String href;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class First {
    private String href;
}

