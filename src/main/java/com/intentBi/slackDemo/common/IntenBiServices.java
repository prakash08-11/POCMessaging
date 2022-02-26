package com.intentBi.slackDemo.common;

import com.intentBi.slackDemo.entity.response.ColumnName;
import com.intentBi.slackDemo.entity.response.DataSetName;

import com.intentBi.slackDemo.entity.request.ReportRequest;
import com.intentBi.slackDemo.entity.request.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.http.*;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.*;
import java.util.stream.Collectors;

@Component
public class IntenBiServices {

    private static final Logger log = LoggerFactory.getLogger(IntenBiServices.class);
    private static final String INTENT_BI_BASE_URL = "http://intentbitrial.southindia.cloudapp.azure.com/teamsorgs/";

    @Autowired
    Environment env;

    public byte[] getImage(ReportRequest request) {
        // once I have code available to fetch the details from kalyan.
        String url = INTENT_BI_BASE_URL+"getReportByDataset";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.put("datasetName",Collections.singletonList(request.getDataSetName()));
        map.put("queryDetails",Collections.singletonList(request.getQueryDetails()));
        map.put("triggerid",Collections.singletonList(""));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST,requestEntity,byte[].class);
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
        return  "xoxe.xoxb-1-MS0yLTI5NTk1NzA5MDg0ODYtMjk3MDI1MDI0Nzk1NC0yOTcwMjgwNDAzNTM5LTMwOTg5OTM3MzQ4MjAtMzNiZDA0MTAwOTY4NDFjOWQwNzAyNzYwOTc0OTE4M2RkNzdmNDYyZjk3NWM2NmI2NmQ5MWQ0OTIzMWFkNTQ1NA";
    }


    public  String getDatasetsAndDetails(String emailAddress) {
        String url = INTENT_BI_BASE_URL +"getDatasetsByEmailId";


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();

        map.put("emailid", Collections.singletonList(emailAddress));
        map.put("triggerid", Collections.singletonList(""));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<DataSetName[]> responseObject = restTemplate.exchange(url,HttpMethod.POST,request, DataSetName[].class);

        DataSetName[] dataSetNames = responseObject.getBody();

        return convertToStringSepearatedByLine(Arrays.stream(dataSetNames).map(DataSetName::getDatasetname).collect(Collectors.toList()));
    }

    private static String convertToStringSepearatedByLine(List<String> list) {
        String listString="";
        for (String s : list)
        {
            listString += s + "\n";
        }
        return listString;
    }


    public String getColumnsOfDataSets(String dataSetName) {
        String url = INTENT_BI_BASE_URL+"getColumnsByDataset";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();

        map.put("datasetname", Collections.singletonList(dataSetName));
        map.put("triggerid", Collections.singletonList(""));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<ColumnName[]> responseObject = restTemplate.exchange(url,HttpMethod.POST,request, ColumnName[].class);

        ColumnName[] columnNames = responseObject.getBody();

        return convertToStringSepearatedByLine(Arrays.stream(columnNames).map(ColumnName::getColumnname).collect(Collectors.toList()));
    }
}
