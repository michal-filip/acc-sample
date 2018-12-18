package com.atlassian.codesample.michalfilip.recordapi.controller;

import com.atlassian.codesample.michalfilip.recordapi.exception.RecordAlreadyDeletedException;
import com.atlassian.codesample.michalfilip.recordapi.exception.RecordNotFoundException;
import com.atlassian.codesample.michalfilip.recordapi.model.RecordDTO;
import com.atlassian.codesample.michalfilip.recordapi.service.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(
    path = "/record",
    produces = APPLICATION_JSON_UTF8_VALUE
)
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    public RecordDTO create(@RequestBody String recordData) {
        return recordService.create(recordData);
    }

    @PatchMapping("/{id}")
    public RecordDTO patch(@PathVariable("id") String id, @RequestBody String recordData) {
        return recordService.update(id, recordData);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        recordService.delete(id);
    }

    @GetMapping
    public List<RecordDTO> list() {
        return this.recordService.list();
    }

    @GetMapping("/{id}")
    public RecordDTO get(@PathVariable("id") String id) {
        return recordService.get(id);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String notFoundHandler() {
        return "Not found";
    }

    @ExceptionHandler(RecordAlreadyDeletedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private String alreadyDeletedHandler() {
        return "Already deleted";
    }

}
