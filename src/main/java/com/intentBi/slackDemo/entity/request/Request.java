package com.intentBi.slackDemo.entity.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Request {
    private String token;
    private String channelId;
    private String channelName;
    private String command;
    private List<String> processParameters;
    private String responseUrl;
    private String triggerId;

    @Override
    public String toString() {
        return "Request{" +
                "token='" + token + '\'' +
                ", channelId='" + channelId + '\'' +
                ", channelName='" + channelName + '\'' +
                ", command='" + command + '\'' +
                ", processParameters=" + processParameters +
                ", responseUrl='" + responseUrl + '\'' +
                ", triggerId='" + triggerId + '\'' +
                '}';
    }
}
