package com.sarbacane.entry_test.service;

import com.sarbacane.entry_test.exceptions.CSVException;
import com.sarbacane.entry_test.feign.RecipientFeignClient;
import com.sarbacane.entry_test.json.ProcessRecipientsRequest;
import com.sarbacane.entry_test.json.Recipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVProcessorService {

    @Autowired
    private RecipientFeignClient recipientFeignClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVProcessorService.class);

    private static final String phoneRegex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
    private static final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+";

    @PostConstruct
    public void init() {
        StrRegEx.registerMessage(phoneRegex, "should be a valid phone number");
        StrRegEx.registerMessage(emailRegex, "should be a valid email address");
    }

    private static final CellProcessor[] cellProcessor = new CellProcessor[]{
            new StrMinMax(0, 100), // Name
            new StrRegEx(phoneRegex), // Phone
            new StrRegEx(emailRegex) // Email
    };

    public void processCsv(MultipartFile csvFile) throws Exception {
        ICsvBeanReader beanReader;
        try {
            beanReader = new CsvBeanReader(new InputStreamReader(csvFile.getInputStream()), CsvPreference.STANDARD_PREFERENCE);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while trying to read input file.");
            throw new CSVException("Unexpected error occurred.");
        }

        try {
            final String[] header = beanReader.getHeader(true);
            List<Recipient> recipientList = new ArrayList<>();
            Recipient recipient;
            while( (recipient = beanReader.read(Recipient.class, header, cellProcessor)) != null ) {
                LOGGER.debug(String.format("Parsing lineNo=%s, rowNo=%s, recipient=%s", beanReader.getLineNumber(),
                        beanReader.getRowNumber(), recipient));
                recipientList.add(recipient);
            }
            ProcessRecipientsRequest processRecipientsRequest = new ProcessRecipientsRequest();
            processRecipientsRequest.setRecipients(recipientList);
            recipientFeignClient.processNewRecipients(processRecipientsRequest);
        } catch (SuperCsvException e) {
            LOGGER.error("Unable to parse line " + e.getCsvContext() + ". " + e.getMessage());
            throw new CSVException("Parse error at CSV line " + beanReader.getLineNumber() + ": " + e.getMessage());
        } finally {
            beanReader.close();
        }
    }
}
