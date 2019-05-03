package com.sarbacane.entry_test.controller;

import com.sarbacane.entry_test.service.CSVProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/v1")
public class CSVProcessorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVProcessorController.class);

    @Autowired
    private CSVProcessorService CSVProcessorService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/process-file")
    public void processCsv(@RequestParam(value = "csv_file") MultipartFile csvFile) throws Exception {
        try {
            LOGGER.info("========================== processCsv begin ==========================");
            CSVProcessorService.processCsv(csvFile);
        } finally {
            LOGGER.info("=========================== processCsv end ===========================");
        }
    }
}
