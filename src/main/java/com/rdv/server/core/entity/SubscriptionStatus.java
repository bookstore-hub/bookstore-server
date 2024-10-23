package com.rdv.server.core.entity;

/**
 * @author davidgarcia
 */

public enum SubscriptionStatus {

    PENDING,
    CANCELED,
    ASSESSING,
    CONFIRMED,
    VERIFIED,
    ENDED,
    ENDED_BANISHED,
    TESTER,
    ADMIN;


    public static boolean isAllowedTesting(SubscriptionStatus status) {
        return status.equals(TESTER) || status.equals(ADMIN);
    }

}
