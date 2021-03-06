package com.plastic.bevslch.europool2016.endpoints;

import com.beastpotato.potato.api.Constants;
import com.beastpotato.potato.api.Endpoint;
import com.beastpotato.potato.api.HeaderParam;
import com.beastpotato.potato.api.UrlParam;

/**
 * Created by Oleksiy on 6/2/2016.
 */

@Endpoint(httpMethod = Constants.Http.POST, relativeUrl = "/login", jsonExample = "{\"success\":true,\"errorCode\":0,\"errorMessage\":\"\",\"token\":\"longstringhere\",\"user\":{\"email\":\"someemail\",\"password\":\"somepassword\",\"name\":\"Oleksiy Martynov\",\"photo\":\"someurlhere\"}}")
public abstract class LoginEndpoint {
    @HeaderParam("Content-Type")
    String contentType;

    @UrlParam("email")
    String email;

    @UrlParam("password")
    String password;

    @UrlParam("signup")
    String signup;
}
