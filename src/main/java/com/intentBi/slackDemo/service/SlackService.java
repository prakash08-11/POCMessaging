package com.intentBi.slackDemo.service;

import com.intentBi.slackDemo.common.IntenBiServices;
import com.intentBi.slackDemo.entity.request.ReportRequest;
import com.intentBi.slackDemo.entity.request.Request;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.files.FilesUploadRequest;

import com.slack.api.webhook.Payload;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SlackService {

    @Autowired
    IntenBiServices intentBiService;

    private static Logger log = LoggerFactory.getLogger(SlackService.class);

    public byte[] getImageFromIntentBi(ReportRequest request) {
        log.info("inside Method getImageFromIntentBi");

        return intentBiService.getImage(request);
    }

    @SneakyThrows
    public void queryProcessor(Request request) {
        log.info("inside method queryProcessor");
        String authToken = intentBiService.getValidateToken();
        Slack slack = Slack.getInstance();
        MethodsClient methodsClient = slack.methods();
        Payload response = Payload.builder()
                .text("Processing... the request"+"\n Data Set Name:::>> "+request.getProcessParameters().get(0)
                        + "\n Query name ::::> " + request.getProcessParameters().get(1))
                .build();
        slack.send(request.getResponseUrl(), response);

        ReportRequest reportRequest = new ReportRequest(request.getProcessParameters().get(0),
                request.getProcessParameters().get(1),request.getTriggerId());

        byte[] byteArrayImage = getImageFromIntentBi(reportRequest);
        FilesUploadRequest filesUploadRequest = FilesUploadRequest.builder()
                .channels(List.of(request.getChannelId()))
                .fileData(byteArrayImage)
                .filename(request.getProcessParameters().get(0)+"_"+request.getProcessParameters().get(1)+".png")
                .filetype("image/png")
                .token(authToken)
                .title(request.getProcessParameters().get(0)+"_"+request.getProcessParameters().get(1))
                .build();
        methodsClient.filesUpload(filesUploadRequest);
    }
}
