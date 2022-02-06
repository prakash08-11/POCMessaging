package com.intentBi.slackDemo.common;

import com.intentBi.slackDemo.entity.request.ReportRequest;
import com.intentBi.slackDemo.entity.request.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class IntenBiServices {

    @Autowired
    Environment env;

    public byte[] getImage(ReportRequest request) {
        // once I have code available to fetch the details from kalyan.
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/getimage"; // change it hosted API
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("userName","password");
        HttpEntity<ReportRequest> requestHttpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url,HttpMethod.POST,requestHttpEntity,byte[].class);
        // for the testing the Application.
        return response.getBody();
    }


   /* Check and validate the incoming request.
   Documentation: https://api.slack.com/authentication/verifying-requests-from-slack
   */
    public boolean invalidateRequest(String requestToken) {

        String token = env.getProperty("slack.requestToken");
        return !requestToken.equals(token);
    }

    public Request mapRequest(MultiValueMap<String, String> parameters) {

        Request request = new Request();
        request.setToken(Objects.isNull(parameters.getFirst("token"))? "TokenNotAvailable": parameters.getFirst("token"));
        request.setChannelId(parameters.getFirst("channel_id"));
        request.setChannelName(parameters.getFirst("channel_name"));
        request.setCommand(parameters.getFirst("command"));
        request.setProcessParameters(preprocessParameters(Objects.requireNonNull(parameters.getFirst("text"))));
        request.setResponseUrl(parameters.getFirst("response_url"));
        request.setTriggerId(parameters.getFirst("trigger_id"));
        return request;
    }

    public List<String> preprocessParameters(String text) {
     return new ArrayList<>(Arrays.asList(text.split(",")));
    }

    public String getValidateToken() {
// Code to get the valid token
        return  "xoxe.xoxb-1-MS0yLTI5NTk1NzA5MDg0ODYtMjk3MDI1MDI0Nzk1NC0yOTcwMjgw" +
                "NDAzNTM5LTMwMzUwNzcwNzkyMzQtY2UzYzc1ODdjNDk4NTQ5MmQzMzU3NTQ3NjFiYWViZTYxOWRiMDE1OWJjZTY1Y2ViNDI1MWQxMzMyMjAxODgxNw";
    }


}
