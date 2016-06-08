package com.plastic.bevslch.europool2016.endpoints;

import com.beastpotato.potato.api.Constants;
import com.beastpotato.potato.api.Endpoint;
import com.beastpotato.potato.api.HeaderParam;

/**
 * Created by Oleksiy on 6/1/2016.
 */
@Endpoint(httpMethod = Constants.Http.GET, relativeUrl = "/pool", jsonExample = "{\"success\":true,\"errorCode\":0,\"errorMessage\":\"\",\"data\":[{\"name\":\"Brian\",\"points\":10, \"photo\": \"url\"}]}")
public abstract class PoolEndpoint {
    @HeaderParam("Content-Type")
    String contentType;
    @HeaderParam("token")
    String token;
}
