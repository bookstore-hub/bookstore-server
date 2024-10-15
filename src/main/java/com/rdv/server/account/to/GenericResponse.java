package com.rdv.server.account.to;

/**
 * @author davidgarcia
 */

public class GenericResponse {

    public record GenericResponseData(boolean status, String message) {}

}
