package com.sarbacane.entry_test.controller;

import com.sarbacane.entry_test.json.ProcessRecipientsRequest;
import com.sarbacane.entry_test.json.Recipient;
import com.sarbacane.entry_test.service.RecipientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/v1")
public class RecipientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipientController.class);

    @Autowired
    private RecipientService recipientService;

    @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/process-recipients")
    public void processNewRecipients(@Valid @RequestBody ProcessRecipientsRequest request) throws Exception {
        try {
            LOGGER.info("========================== processNewRecipients begin ==========================");
            recipientService.processNewRecipients(request);
        } finally {
            LOGGER.info("=========================== processNewRecipients end ===========================");
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/process-recipients")
    public void editRecipients(@Valid @RequestBody ProcessRecipientsRequest request) throws Exception {
        try {
            LOGGER.info("========================== editRecipients begin ==========================");
            recipientService.editRecipients(request);
        } finally {
            LOGGER.info("=========================== editRecipients end ===========================");
        }
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/process-recipients")
    public void deleteRecipients(@Valid @RequestBody ProcessRecipientsRequest request) throws Exception {
        try {
            LOGGER.info("========================== deleteRecipients begin ==========================");
            recipientService.deleteRecipients(request);
        } finally {
            LOGGER.info("=========================== deleteRecipients end ===========================");
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/recipients")
    @ResponseBody
    public List<Recipient> getRecipients() throws Exception {
        try {
            LOGGER.info("========================== getRecipients begin ==========================");
            return recipientService.getRecipients();
        } finally {
            LOGGER.info("=========================== getRecipients end ===========================");
        }
    }
}
