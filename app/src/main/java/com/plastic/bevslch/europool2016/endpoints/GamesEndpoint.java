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
        relativeUrl = "/sample/games",
        jsonExample = "{\"success\":true,\"errorCode\":0,\"errorMessage\":\"\",\"data\":[{\"gameID\":\"0\",\"awayTeam\":{\"name\":\"Romania\",\"flag\":\"https://upload.wikimedia.org/wikipedia/commons/7/73/Flag_of_Romania.svg\"},\"homeTeam\":{\"name\":\"France\",\"flag\":\"https://upload.wikimedia.org/wikipedia/en/c/c3/Flag_of_France.svg\"},\"startTime\":\"2016-06-11T18:00:00Z\",\"awayGoals\":0,\"homeGoals\":0,\"prediction\":{\"awayGoals\":0,\"homeGoals\":0}}]}")
public class GamesEndpoint {
    @HeaderParam("token")
    public String token;
}
