package com.plastic.bevslch.europool2016.endpoints;

import com.beastpotato.potato.api.Validation;

/**
 * Created by Oleksiy on 6/2/2016.
 */

public class LoginValidator {
    @Validation(fieldName = "email")
    public static boolean isEmailValid(String email) {
        return email != null;
    }

    @Validation(fieldName = "password")
    public static boolean isPasswordValid(String password) {
        return password != null;
    }
}
