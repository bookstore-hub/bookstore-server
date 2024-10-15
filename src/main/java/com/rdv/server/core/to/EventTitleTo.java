package com.rdv.server.core.to;

import com.rdv.server.core.entity.*;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class EventTitleTo {

    /** Creation or Update of Data **/
    public record EventTitleCreationOrUpdate(

        @Parameter (description = "The title")
        @Size(max = 70)
        @NotNull
        String title,
        @Parameter (description = "The language")
        @Size(max = 10)
        @NotNull
        Language language
    ) {}

    /** Data **/
    public record Data(String title, Language language) {
        public Data(EventTitle eventTitle) {
            this(eventTitle.getTitle(), eventTitle.getLanguage());
        }
    }

}
