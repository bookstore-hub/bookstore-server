package com.rdv.server.core.to;

import com.rdv.server.core.entity.*;
import com.rdv.server.core.util.DateUtil;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.List;


public class EventTo {


    /** Creation or Update of Data **/
    public record CreationOrUpdate(
        @Parameter (description = "The event title")
        @Size(max = 70)
        @NotNull
        String title,
        @Parameter (description = "The start date")
        @NotNull
        OffsetDateTime startDate,
        @Parameter (description = "The end date")
        @NotNull
        OffsetDateTime endDate,
        @Parameter (description = "The event category")
        @Size(max = 50)
        @NotNull
        EventCategory category,
        @Parameter (description = "The target audience")
        @Size(max = 30)
        EventTargetAudience targetAudience,
        @Parameter (description = "The location (venue)")
        @Size(max = 30)
        String venue,
        @Parameter (description = "The location (district)")
        @Size(max = 30)
        String district,
        @Parameter (description = "The cost")
        double cost,
        @Parameter (description = "The promotional poster")
        @Size(max = 150)
        String poster,
        @Parameter (description = "The extra details link")
        @Size(max = 150)
        String detailsLink,
        @Parameter (description = "The ticketing link")
        @Size(max = 150)
        String ticketingLink,
        List<EventDescriptionTo.CreationOrUpdate> descriptions
    ) {}


    /** Minimal Data **/
    public record MinimalData(Long id, String title, String poster) {
        public MinimalData(Event event) {
            this(event.getId(), event.getTitle(), event.getPoster());
        }
    }

    /** Listing Data **/
    public record ListingData(Long id, String startDate, String endDate, String venue, double cost, String poster, String title, EventState state, String additionalInfo) {
        public ListingData(Event event, Language language) {
            this(event.getId(), DateUtil.formatDateAndTime(event.getStartDate(), LocaleUtils.toLocale(language.name())),
                    DateUtil.formatDateAndTime(event.getEndDate(), LocaleUtils.toLocale(language.name())),
                    event.getVenue(), event.getCost(), event.getPoster(), event.getTitle(),
                    determineEventStateDisplayed(event.getStartDate(), event.getEndDate(), event.getState()), determineAdditionalInfo(event));
        }
    }

    /** Full Data **/
    public record FullData(Long id, String startDate, String endDate, EventCategory type, EventTargetAudience targetAudience, String venue, String district,
                           double cost, String poster, String detailsLink, String ticketingLink, String title, EventState state,
                           EventValidationStatus validationStatus, boolean editable) {
        public FullData(Event event, Language language, boolean editable) {
            this(event.getId(), DateUtil.formatDateAndTime(event.getStartDate(), LocaleUtils.toLocale(language.name())),
                    DateUtil.formatDateAndTime(event.getEndDate(), LocaleUtils.toLocale(language.name())),
                    event.getCategory(), event.getTargetAudience(), event.getVenue(), event.getDistrict(), event.getCost(), event.getPoster(),
                    event.getDetailsLink(), event.getTicketingLink(), event.getTitle(),
                    determineEventStateDisplayed(event.getStartDate(), event.getEndDate(), event.getState()), event.getValidationStatus(), editable);
        }
    }


    private static EventState determineEventStateDisplayed(OffsetDateTime startDate, OffsetDateTime endDate, EventState state) {
        EventState stateDisplayed = state;
        if (!EventState.CANCELLED.equals(state)) {
            OffsetDateTime now = OffsetDateTime.now();
            if(now.isAfter(startDate) && now.isBefore(endDate)) {
                stateDisplayed = EventState.ONGOING;
            }
        }
        return stateDisplayed;
    }

    private static String determineAdditionalInfo(Event event) {
        String additionalInfo = StringUtils.EMPTY;

        //If the event takes place over several days, a fraction is used to indicate on which specific day of the event we are
        if(!event.getStartDate().isEqual(event.getEndDate())) {
            LocalDate startDateAsLocalDate = event.getStartDate().toLocalDate();
            LocalDate endDateAsLocalDate = event.getEndDate().toLocalDate();
            LocalDate currentDate = LocalDate.now();

            Period daysFromStartDateToCurrentDate = Period.between(startDateAsLocalDate, currentDate);
            Period daysFromStartDateToEndDate = Period.between(startDateAsLocalDate, endDateAsLocalDate);
            int numberOfDays1 = Math.abs(daysFromStartDateToCurrentDate.getDays());
            int numberOfDays2 = Math.abs(daysFromStartDateToEndDate.getDays());
            return numberOfDays1 + "/" + numberOfDays2;
        }

        return additionalInfo;
    }


    /** Mapping of new Event **/
    public static Event mapNewEvent(CreationOrUpdate eventData) {
        Event event = new Event();
        event.setCreationDate(OffsetDateTime.now());
        event.setState(EventState.PLANNED);
        mapUpdatedEvent(event, eventData);

        return event;
    }

    /** Mapping of updated Event **/
    public static void mapUpdatedEvent(Event event, CreationOrUpdate eventData) {
        event.setTitle(eventData.title());
        event.setStartDate(eventData.startDate());
        event.setEndDate(eventData.endDate());
        event.setCategory(eventData.category());
        event.setTargetAudience(eventData.targetAudience());
        event.setVenue(eventData.venue());
        event.setDistrict(eventData.district());
        event.setCost(eventData.cost());
        event.setPoster(eventData.poster());
        event.setDetailsLink(eventData.detailsLink());
        event.setTicketingLink(eventData.ticketingLink());
        event.setValidationStatus(EventValidationStatus.ASSESSING);
    }

}
