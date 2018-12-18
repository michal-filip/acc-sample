package com.atlassian.codesample.michalfilip.recordapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RecordStatus {
    NEW("NEW"),
    UPDATED("UPDATED"),
    DELETED("DELETED");

    private static final Map<String, RecordStatus> valueMap = Stream.of(values()).collect(Collectors.toMap(RecordStatus::getValue, Function.identity()));
    private final String value;

    private RecordStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RecordStatus fromString(String value) {
        return valueMap.get(value);
    }
}
