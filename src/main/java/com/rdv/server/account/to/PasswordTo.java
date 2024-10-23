package com.rdv.server.account.to;


public class PasswordTo {

    public record ChangeData(String oldPassword, String newPassword) {}

    public record ResetData(String token, String password, String confirmPassword) {}

}