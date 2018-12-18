package com.atlassian.codesample.michalfilip.recordapi.service;

import com.atlassian.codesample.michalfilip.recordapi.model.RecordDTO;

import java.util.List;

public interface RecordService {

    RecordDTO create(String recordData);

    RecordDTO update(String id, String recordData);

    void delete(String id);

    RecordDTO get(String id);

    List<RecordDTO> list();

}
