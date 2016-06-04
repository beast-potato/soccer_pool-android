package com.plastic.bevslch.europool2016.endpoints;

import com.beastpotato.potato.api.Constants;
import com.beastpotato.potato.api.Endpoint;
import com.beastpotato.potato.api.HeaderParam;
import com.beastpotato.potato.api.UrlParam;

/**
 * PredictEndpoint
 *
 * @author PlasticMobile
 *         Created on 2016-06-03
 *         Copyright © 2015 RBC. All rights reserved.
 */
@Endpoint(httpMethod = Constants.Http.POST, relativeUrl = "/sample/predictgame", jsonExample = "{\"success\":true,\"errorCode\":0,\"errorMessage\":\"\"}")
public class PredictEndpoint {
    @HeaderParam("token")
    public String token;
    @UrlParam("gameID")
    public String gameId;
    @UrlParam("homeGoals")
    public String homeGoals;
    @UrlParam("awayGoals")
    public String awayGoals;
}
