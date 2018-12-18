package com.atlassian.codesample.michalfilip.recordapi.exception;

public class RecordAlreadyDeletedException extends RuntimeException {

    public RecordAlreadyDeletedException(String id) {
        super("Record has already been deleted: " + id);
    }
}
