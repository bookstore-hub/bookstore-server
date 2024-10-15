package com.rdv.server.account.to;


public class PasswordTo {

    public record PasswordChangeData(String oldPassword, String newPassword) {}

    public record PasswordResetData(String token, String password, String confirmPassword) {}

}