package com.plastic.bevslch.europool2016.endpoints;

import com.beastpotato.potato.api.Constants;
import com.beastpotato.potato.api.Endpoint;
import com.beastpotato.potato.api.HeaderParam;

/**
 * GamesEndpoint
 *
 * @author PlasticMobile
 *         Created on 2016-06-03
 *         Copyright © 2015 RBC. All rights reserved.
 */
@Endpoint(httpMethod = Constants.Http.GET,
        relativeUrl = "/games",
        jsonExample = "{\"success\":true,\"errorCode\":0,\"errorMessage\":\"\",\"data\":[{\"gameID\":\"149855\",\"awayTeam\":{\"name\":\"Romania\",\"image\":\"http://104.131.118.14/images/Romania.png\"},\"homeTeam\":{\"name\":\"France\",\"image\":\"http://104.131.118.14/images/France.png\"},\"state\":\"complete\",\"startTime\":1465585200,\"awayGoals\":1,\"homeGoals\":2,\"hasBeenPredicted\":true,\"requiresWinner\":true,\"prediction\":{\"gameID\":\"149855\",\"awayGoals\":0,\"homeGoals\":3,\"points\":2},\"cutOffTime\":1465583400}]}")
public class GamesEndpoint {
    @HeaderParam("token")
    public String token;
}
