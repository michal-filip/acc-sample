package com.atlassian.codesample.michalfilip.recordapi.model;

import java.util.Date;
import java.util.List;

public class RecordInfoDTO {
    private RecordStatus recordStatus;
    private Date created;
    private List<Date> updated;
    private Date deleted;
    private String recordData;

    public RecordStatus getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<Date> getUpdated() {
        return updated;
    }

    public void setUpdated(List<Date> updated) {
        this.updated = updated;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public String getRecordData() {
        return recordData;
    }

    public void setRecordData(String recordData) {
        this.recordData = recordData;
    }
}
