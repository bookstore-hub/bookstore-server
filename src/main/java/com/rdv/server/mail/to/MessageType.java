package com.rdv.server.mail.to;

/**
 * @author davidgarcia
 */

public enum MessageType {

    CONTACT_US(""),
    SUBSCRIPTION ("001"),
    PASSWORD_RESET ("002"),
    WARNING_PROBLEMATIC_BEHAVIOR("003"),
    WARNING_PROBLEMATIC_ADVERT("004"),
    CLOSING_ACCOUNT ("005"),
    BANISHING_FOR_PROBLEMATIC_BEHAVIOR("006"),
    BANISHING_FOR_PROBLEMATIC_ADVERTS("007"),
    INVITATION("008"),
    EVENT_VALIDATED("009"),
    EVENT_NOT_VALIDATED("010"),
    ADVERT_REMOVAL("011"),
    ADVERT_VALIDATED("012"),
    ADVERT_VALIDATED_TO_PAY("013"),
    ADVERT_PAID("014"),
    ADVERT_NOT_VALIDATED("015");

    private String code;

    MessageType(String code) {
        this.code = code;
    }

    /**
     * Returns the code
     *
     * @return Returns the code
     */
    public String getCode() {
        return code;
    }

}
