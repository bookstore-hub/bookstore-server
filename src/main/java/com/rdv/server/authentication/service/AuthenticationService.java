package com.rdv.server.authentication.service;

/**
 * @author david.garcia
 */

public interface AuthenticationService {

    boolean authenticate(String username, String password);

}
