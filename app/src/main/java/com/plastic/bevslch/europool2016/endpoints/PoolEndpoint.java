package com.plastic.bevslch.europool2016.endpoints;

import com.beastpotato.potato.api.Constants;
import com.beastpotato.potato.api.Endpoint;
import com.beastpotato.potato.api.HeaderParam;

/**
 * Created by Oleksiy on 6/1/2016.
 */
@Endpoint(httpMethod = Constants.Http.GET, relativeUrl = "/test/pool", jsonExample = "{\"success\":true,\"errorCode\":0,\"errorMessage\":\"\",\"data\":[{\"name\":\"Brian\",\"points\":10}]}")
public abstract class PoolEndpoint {
    @HeaderParam("Content-Type")
    String contentType;
}
