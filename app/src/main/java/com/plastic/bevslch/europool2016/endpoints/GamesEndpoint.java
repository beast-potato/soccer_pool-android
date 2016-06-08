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
        relativeUrl = "/test/games",
        jsonExample = "{\"success\":true,\"errorCode\":0,\"errorMessage\":\"\",\"data\":[{\"gameID\":\"0\",\"awayTeam\":{\"name\":\"France\",\"image\":\"http://104.131.118.14/images/France.png\"},\"homeTeam\":{\"name\":\"Ukraine\",\"image\":\"http://104.131.118.14/images/Ukraine.png\"},\"startTime\":1465617600,\"awayGoals\":0,\"homeGoals\":0,\"hasBeenPredicted\":false,\"prediction\":{\"awayGoals\":0,\"homeGoals\":0,\"points\":0},\"cutOffTime\":1465615800},{\"gameID\":\"1\",\"awayTeam\":{\"name\":\"France\",\"image\":\"http://104.131.118.14/images/France.png\"},\"homeTeam\":{\"name\":\"Spain\",\"image\":\"http://104.131.118.14/images/Spain.png\"},\"startTime\":1465531200,\"awayGoals\":0,\"homeGoals\":0,\"hasBeenPredicted\":false,\"prediction\":{\"awayGoals\":0,\"homeGoals\":0,\"points\":0},\"cutOffTime\":1465529400},{\"gameID\":\"2\",\"awayTeam\":{\"name\":\"Spain\",\"image\":\"http://104.131.118.14/images/Spain.png\"},\"homeTeam\":{\"name\":\"Ukraine\",\"image\":\"http://104.131.118.14/images/Ukraine.png\"},\"startTime\":1464840000,\"awayGoals\":3,\"homeGoals\":0,\"hasBeenPredicted\":false,\"prediction\":{\"awayGoals\":0,\"homeGoals\":0,\"points\":0},\"cutOffTime\":1464838200}]}")
public class GamesEndpoint {
    @HeaderParam("token")
    public String token;
}
