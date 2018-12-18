package com.atlassian.codesample.michalfilip.recordapi.service;

import com.atlassian.codesample.michalfilip.recordapi.exception.RecordAlreadyDeletedException;
import com.atlassian.codesample.michalfilip.recordapi.exception.RecordNotFoundException;
import com.atlassian.codesample.michalfilip.recordapi.model.RecordDTO;
import com.atlassian.codesample.michalfilip.recordapi.model.RecordInfoDTO;
import com.atlassian.codesample.michalfilip.recordapi.model.RecordStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class RecordServiceImpl implements RecordService {

    private static final Object recordStorageLock = new Object();
    private static File recordStorage = new File("C:\\DEV\\recordapi-data\\records.txt");
    static {
        synchronized (recordStorageLock) {
            if (!recordStorage.exists()) {
                throw new RuntimeException("Record storage file does not exist!");
            }
        }
    }

    private final ObjectMapper objectMapper;

    public RecordServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public RecordDTO create(String recordData) {
        synchronized (recordStorageLock) {
            RecordDTO recordDTO = new RecordDTO();
            recordDTO.setRecordId(UUID.randomUUID().toString());
            RecordInfoDTO recordInfoDTO = new RecordInfoDTO();
            recordInfoDTO.setCreated(new Date());
            recordInfoDTO.setUpdated(Collections.emptyList());
            recordInfoDTO.setRecordStatus(RecordStatus.NEW);
            recordInfoDTO.setRecordData(recordData);
            recordDTO.setInfo(recordInfoDTO);

            try (BufferedWriter writer = createStorageWriter(true)) {
                writer.write(objectMapper.writeValueAsString(recordDTO));
                writer.newLine();
                return recordDTO;
            } catch (IOException e) {
                throw new RuntimeException("Record storage file writing has failed!", e);
            }
        }
    }

    @Override
    public RecordDTO update(String id, String recordData) {
        return modify(id, recordDTO -> {
            RecordInfoDTO info = recordDTO.getInfo();
            info.setRecordData(recordData);
            info.getUpdated().add(new Date());
            info.setRecordStatus(RecordStatus.UPDATED);
        });
    }

    @Override
    public void delete(String id) {
        modify(id, recordDTO -> {
            RecordInfoDTO info = recordDTO.getInfo();
            info.setDeleted(new Date());
            info.setRecordStatus(RecordStatus.DELETED);
        });
    }

    private RecordDTO modify(String id, Consumer<RecordDTO> recordModifier) {
        List<RecordDTO> allRecords = list();
        Optional<RecordDTO> deletedRecord = allRecords.stream().filter(record -> record.getRecordId().equals(id)).findFirst();
        if (!deletedRecord.isPresent()) {
            throw new RecordNotFoundException(id);
        } else if (deletedRecord.get().getInfo().getRecordStatus() == RecordStatus.DELETED) {
            throw new RecordAlreadyDeletedException(id);
        }

        synchronized (recordStorageLock) {
            try (BufferedWriter writer = createStorageWriter(false)) {
                for (RecordDTO recordDTO : allRecords) {
                    if (recordDTO.getRecordId().equals(id)) {
                        recordModifier.accept(recordDTO);
                    }
                    writer.write(objectMapper.writeValueAsString(recordDTO));
                    writer.newLine();
                };
            } catch (IOException e) {
                throw new RuntimeException("Record storage file writing has failed!", e);
            }
        }

        return deletedRecord.get();
    }

    @Override
    public RecordDTO get(String id) {
        synchronized (recordStorageLock) {
            try (BufferedReader reader = createStorageReader()) {
                while (reader.ready()) {
                    RecordDTO recordDTO = objectMapper.readValue(reader.readLine(), RecordDTO.class);
                    if (recordDTO.getRecordId().equals(id)) {
                        return recordDTO;
                    }
                }
                throw new RecordNotFoundException(id);
            } catch (IOException e) {
                throw new RuntimeException("Record storage file reading has failed!", e);
            }
        }
    }

    @Override
    public List<RecordDTO> list() {
        synchronized (recordStorageLock) {
            try (BufferedReader reader = createStorageReader()) {
                List<RecordDTO> list = new LinkedList<>();
                while (reader.ready()) {
                    list.add(
                        objectMapper.readValue(reader.readLine(), RecordDTO.class)
                    );
                }
                return list;
            } catch (IOException e) {
                throw new RuntimeException("Record storage file reading has failed!", e);
            }
        }
    }

    protected BufferedReader createStorageReader() {
        try {
            return new BufferedReader(new FileReader(recordStorage));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Record storage file has gone missing!", e);
        }
    }

    protected BufferedWriter createStorageWriter(boolean append) throws IOException {
        try {
            return new BufferedWriter(new FileWriter(recordStorage, append));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Record storage file has gone missing!", e);
        }
    }
}
