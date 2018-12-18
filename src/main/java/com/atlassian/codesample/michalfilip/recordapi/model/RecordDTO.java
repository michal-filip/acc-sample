package com.atlassian.codesample.michalfilip.recordapi.model;

public class RecordDTO {
    private String recordId;
    private RecordInfoDTO info;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public RecordInfoDTO getInfo() {
        return info;
    }

    public void setInfo(RecordInfoDTO info) {
        this.info = info;
    }
}
