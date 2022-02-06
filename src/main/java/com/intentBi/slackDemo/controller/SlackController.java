package com.intentBi.slackDemo.controller;

import com.intentBi.slackDemo.common.IntenBiServices;
import com.intentBi.slackDemo.entity.request.Request;
import com.intentBi.slackDemo.service.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/slack")
public class SlackController {

    @Autowired
    SlackService slackService;

    @Autowired
    IntenBiServices intenBiServices;

    @PostMapping("/service")
    public String health() {
        return "Service is available";
    }

    @PostMapping("/query")
    public  String query(
            @RequestHeader Map<String,String> headers,
            @RequestBody MultiValueMap<String, String> parameters) {
        //Map the Values to request entity
        Request request = intenBiServices.mapRequest(parameters);
        //Validate the incoming request
        if (intenBiServices.invalidateRequest(request.getToken())){
            return "invalid Request Check with the admin";
        }
        else if (request.getProcessParameters().size()!=2){
            return "Please use command /query to get the reports images from IntentBi below is the example " +
                    "\n Example query: \"/query data set details,query details\"";
        } else {
            ExecutorService executor = Executors.newFixedThreadPool(3);
            // Making the code async
            executor.execute(()->
                //Send the request to the serviceLayer
                slackService.queryProcessor(request)
            );
            return "processing the request";
        }
    }

}
