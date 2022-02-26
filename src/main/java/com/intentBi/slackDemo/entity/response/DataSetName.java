package com.intentBi.slackDemo.entity.response;

import lombok.Getter;

@Getter
public class DataSetName {
   private String datasetname;

    @Override
    public String toString() {
        return "DataSetName{" +
                "datasetname='" + datasetname + '\'' +
                '}';
    }
}
