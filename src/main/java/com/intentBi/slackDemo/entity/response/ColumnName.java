package com.intentBi.slackDemo.entity.response;

import lombok.Getter;

@Getter
public class ColumnName {
    private String columnname;

    @Override
    public String toString() {
        return "ColumnName{" +
                "columnname='" + columnname + '\'' +
                '}';
    }
}
