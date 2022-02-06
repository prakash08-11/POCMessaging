package com.intentBi.slackDemo.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReportRequest {
    private String dataSetName;
    private String queryDetails;
    private String uniqueId;

    @Override
    public String toString() {
        return "ReportRequest:{" +
                "dataSetName='" + dataSetName + '\'' +
                ", queryDetails='" + queryDetails + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                '}';
    }
}
