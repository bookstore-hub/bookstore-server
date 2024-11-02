package com.rdv.server.datacollect.mapper;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class TicketmasterMapping {
    _embedded _embeddedObject;
    _links _linksObject;
    Page PageObject;
}

@Getter
@Setter
class Page {
    private float size;
    private float totalElements;
    private float totalPages;
    private float number;
}

@Getter
@Setter
class _links {
    First FirstObject;
    Self SelfObject;
    Next NextObject;
    Last LastObject;
}

@Getter
@Setter
class Last {
    private String href;
}

@Getter
@Setter
class Next {
    private String href;
}

@Getter
@Setter
class Self {
    private String href;
}

@Getter
@Setter
class First {
    private String href;
}

@Getter
@Setter
class _embedded {
    ArrayList < TicketMasterEvent > events = new ArrayList< >();
}

@Getter
@Setter
class TicketMasterEvent {
        private String name;
        private String type;
        private String id;
        private boolean test;
        private String url;
        private String locale;
        ArrayList < Image > images = new ArrayList < Image > ();
        Sales SalesObject;
        Dates DatesObject;
        ArrayList < Classification > classifications = new ArrayList < Classification > ();
        Promoter PromoterObject;
        ArrayList < Object > promoters = new ArrayList < Object > ();
        private String info;
        ArrayList < PriceRange > priceRanges = new ArrayList < PriceRange > ();
        ArrayList < Object > products = new ArrayList < Object > ();
        Seatmap SeatmapObject;
        Accessibility AccessibilityObject;
        AgeRestrictions AgeRestrictionsObject;
        Ticketing TicketingObject;
        _links_event _linksObject;
        _embedded_event _embeddedObject;
}

@Getter
@Setter
class _embedded_event {
    ArrayList < Venue > venues = new ArrayList < Venue > ();
    ArrayList < Object > attractions = new ArrayList < Object > ();
}

@Getter
@Setter
class _links_event {
    Self SelfObject;
    ArrayList < Object > attractions = new ArrayList < Object > ();
    ArrayList < Object > venues = new ArrayList < Object > ();
}

@Getter
@Setter
class Ticketing {
    SafeTix SafeTixObject;
    AllInclusivePricing AllInclusivePricingObject;
    private String id;
}

@Getter
@Setter
class AllInclusivePricing {
        private boolean enabled;
}

@Getter
@Setter
class SafeTix {
        private boolean enabled;
        private boolean inAppOnlyEnabled;
}

@Getter
@Setter
class AgeRestrictions {
        private boolean legalAgeEnforced;
        private String id;
}

@Getter
@Setter
class Accessibility {
        private String info;
        private float ticketLimit;
        private String id;
}

@Getter
@Setter
class Seatmap {
        private String staticUrl;
        private String id;
}

@Getter
@Setter
class Promoter {
        private String id;
        private String name;
        private String description;
}

@Getter
@Setter
class Dates {
        Start StartObject;
        private String timezone;
        Status StatusObject;
        private boolean spanMultipleDays;
}

@Getter
@Setter
class Status {
        private String code;
}

@Getter
@Setter
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
class Sales {
        Public PublicObject;
}

@Getter
@Setter
class Public {
        private String startDateTime;
        private boolean startTBD;
        private boolean startTBA;
        private String endDateTime;
}

@Getter
@Setter
class Image {
    private String ratio;
    private String url;
    private float width;
    private float height;
    private boolean fallback;
}

@Getter
@Setter
class Classification {
    private boolean primary;
    Segment SegmentObject;
    Genre GenreObject;
    SubGenre SubGenreObject;
    Type TypeObject;
    SubType SubTypeObject;
    private boolean family;
}

@Getter
@Setter
class SubType {
    private String id;
    private String name;
}

@Getter
@Setter
class Type {
    private String id;
    private String name;
}

@Getter
@Setter
class SubGenre {
    private String id;
    private String name;
}

@Getter
@Setter
class Genre {
    private String id;
    private String name;
}

@Getter
@Setter
class Segment {
    private String id;
    private String name;
}

@Getter
@Setter
class PriceRange {
    private String type;
    private String currency;
    private float min;
    private float max;
}

@Getter
@Setter
class Venue {
    private String name;
    private String type;
    private String id;
    private boolean test;
    private String url;
    private String locale;
    ArrayList < Object > images = new ArrayList < Object > ();
    private String postalCode;
    private String timezone;
    City CityObject;
    State StateObject;
    Country CountryObject;
    Address AddressObject;
    Location LocationObject;
    ArrayList < Object > markets = new ArrayList < Object > ();
    ArrayList < Object > dmas = new ArrayList < Object > ();
    BoxOfficeInfo BoxOfficeInfoObject;
    private String parkingDetail;
    private String accessibleSeatingDetail;
    UpcomingEvents UpcomingEventsObject;
    Ada AdaObject;
    _links_venue _linksObject;
}

@Getter
@Setter
class _links_venue {
    Self SelfObject;
}

@Getter
@Setter
class Ada {
    private String adaPhones;
    private String adaCustomCopy;
    private String adaHours;
}

@Getter
@Setter
class UpcomingEvents {
    private float ticketmaster;
    private float _total;
    private float _filtered;
}

@Getter
@Setter
class BoxOfficeInfo {
    private String phoneNumberDetail;
    private String openHoursDetail;
    private String acceptedPaymentDetail;
    private String willCallDetail;
}

@Getter
@Setter
class Location {
    private String longitude;
    private String latitude;
}

@Getter
@Setter
class Address {
    private String line1;
}

@Getter
@Setter
class Country {
    private String name;
    private String countryCode;
}

@Getter
@Setter
class State {
    private String name;
    private String stateCode;
}

@Getter
@Setter
class City {
    private String name;
}