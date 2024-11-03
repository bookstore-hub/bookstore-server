package com.rdv.server.datacollect.mapper.ticketmaster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Venue {
        private String name;
        private String type;
        private String id;
        private boolean test;
        private String url;
        private String locale;
        ArrayList < Object > images = new ArrayList <  > ();
        private String postalCode;
        private String timezone;
        City City;
        State State;
        Country Country;
        Address Address;
        Location Location;
        ArrayList < Object > markets = new ArrayList <  > ();
        ArrayList < Object > dmas = new ArrayList <  > ();
        BoxOfficeInfo BoxOfficeInfo;
        private String parkingDetail;
        private String accessibleSeatingDetail;
        UpcomingEvents UpcomingEvents;
        Ada Ada;
        _links_venue _links;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class _links_venue {
        Self Self;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Ada {
        private String adaPhones;
        private String adaCustomCopy;
        private String adaHours;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class UpcomingEvents {
        private float ticketmaster;
        private float _total;
        private float _filtered;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class BoxOfficeInfo {
        private String phoneNumberDetail;
        private String openHoursDetail;
        private String acceptedPaymentDetail;
        private String willCallDetail;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Location {
        private String longitude;
        private String latitude;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Address {
        private String line1;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Country {
        private String name;
        private String countryCode;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class State {
        private String name;
        private String stateCode;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class City {
        private String name;
}

