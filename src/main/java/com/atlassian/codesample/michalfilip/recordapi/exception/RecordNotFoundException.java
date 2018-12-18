package com.atlassian.codesample.michalfilip.recordapi.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String id) {
        super("Record could not be found: " + id);
    }
}
