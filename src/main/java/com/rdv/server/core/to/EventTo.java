package com.rdv.server.core.to;

import com.rdv.server.core.entity.*;
import com.rdv.server.core.util.DateUtil;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.LocaleUtils;

import java.time.OffsetDateTime;
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
        @Parameter (description = "The event type")
        @Size(max = 30)
        @NotNull
        EventType type,
        @Parameter (description = "The target audience")
        @Size(max = 30)
        EventTargetAudience targetAudience,
        @Parameter (description = "The location (site)")
        @Size(max = 30)
        String site,
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
        @Parameter (description = "The category")
        @Size(max = 150)
        String category,
        List<EventDescriptionTo.CreationOrUpdate> descriptions
    ) {}


    /** Minimal Data **/
    public record MinimalData(String startDate, String endDate, String site, double cost, String poster, String title) {
        public MinimalData(Event event, Language language) {
            this(DateUtil.formatDateAndTime(event.getStartDate(), LocaleUtils.toLocale(language.name())),
                    DateUtil.formatDateAndTime(event.getEndDate(), LocaleUtils.toLocale(language.name())),
                    event.getSite(), event.getCost(), event.getPoster(), event.getTitle());
        }
    }

    /** Full Data **/
    public record FullData(String startDate, String endDate, EventType type, EventTargetAudience targetAudience, String site, String district,
                           double cost, String poster, String detailsLink, String ticketingLink, String title, String category) {
        public FullData(Event event, Language language) {
            this(DateUtil.formatDateAndTime(event.getStartDate(), LocaleUtils.toLocale(language.name())),
                    DateUtil.formatDateAndTime(event.getEndDate(), LocaleUtils.toLocale(language.name())),
                    event.getType(), event.getTargetAudience(), event.getSite(), event.getDistrict(), event.getCost(), event.getPoster(),
                    event.getDetailsLink(), event.getTicketingLink(), event.getTitle(), event.getCategory());
        }
    }

}
