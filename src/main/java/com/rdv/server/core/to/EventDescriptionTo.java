package com.rdv.server.core.to;

import com.rdv.server.core.entity.EventDescription;
import com.rdv.server.core.entity.Language;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class EventDescriptionTo {

    /** Creation or Update of Data **/
    public record CreationOrUpdate(

        @Parameter (description = "The description")
        @Size(max = 250)
        @NotNull
        String title,
        @Parameter (description = "The language")
        @Size(max = 10)
        @NotNull
        Language language
    ) {}

    /** Data **/
    public record Data(String title, Language language) {
        public Data(EventDescription eventDescription) {
            this(eventDescription.getDescription(), eventDescription.getLanguage());
        }
    }

}
