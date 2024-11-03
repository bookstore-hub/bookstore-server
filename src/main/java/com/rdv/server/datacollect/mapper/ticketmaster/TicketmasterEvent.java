package com.rdv.server.datacollect.mapper.ticketmaster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketmasterEvent {
        private String name;
        private String type;
        private String id;
        private boolean test;
        private String url;
        private String locale;
        ArrayList < Image > images = new ArrayList < Image > ();
        Sales Sales;
        Dates Dates;
        ArrayList < Classification > classifications = new ArrayList < Classification > ();
        Promoter Promoter;
        ArrayList < Object > promoters = new ArrayList <  > ();
        private String info;
        ArrayList < PriceRange > priceRanges = new ArrayList < PriceRange > ();
        ArrayList < Object > products = new ArrayList <  > ();
        Seatmap Seatmap;
        Accessibility Accessibility;
        AgeRestrictions AgeRestrictions;
        Ticketing Ticketing;
        _links_event _links;
        _embedded_event _embedded;
}


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class _embedded_event {
        ArrayList < Venue > venues = new ArrayList < Venue > ();
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class _links_event {
        Self Self;
        ArrayList < Object > attractions = new ArrayList <  > ();
        ArrayList < Object > venues = new ArrayList <  > ();
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Ticketing {
        SafeTix SafeTix;
        AllInclusivePricing AllInclusivePricing;
        private String id;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class AllInclusivePricing {
        private boolean enabled;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class SafeTix {
        private boolean enabled;
        private boolean inAppOnlyEnabled;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class AgeRestrictions {
        private boolean legalAgeEnforced;
        private String id;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Accessibility {
        private String info;
        private float ticketLimit;
        private String id;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Seatmap {
        private String staticUrl;
        private String id;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Promoter {
        private String id;
        private String name;
        private String description;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Dates {
        Start Start;
        private String timezone;
        Status Status;
        private boolean spanMultipleDays;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Status {
        private String code;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Start {
        private String localDate;
        private String localTime;
        private String dateTime;
        private boolean dateTBD;
        private boolean dateTBA;
        private boolean timeTBA;
        private boolean noSpecificTime;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Sales {
        Public Public;
        ArrayList < Object > Presales;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Public {
        private String startDateTime;
        private boolean startTBD;
        private boolean startTBA;
        private String endDateTime;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class SubType {
        private String id;
        private String name;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Type {
        private String id;
        private String name;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class SubGenre {
        private String id;
        private String name;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Genre {
        private String id;
        private String name;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Segment {
        private String id;
        private String name;
}