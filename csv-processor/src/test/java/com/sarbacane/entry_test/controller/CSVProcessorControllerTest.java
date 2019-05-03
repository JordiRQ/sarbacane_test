package com.sarbacane.entry_test.controller;

import com.sarbacane.entry_test.service.CSVProcessorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@WebMvcTest
public class CSVProcessorControllerTest {

    @MockBean
    private CSVProcessorService csvProcessorService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnStatusOkAndProcessCsv() throws Exception {
        MockMultipartFile csvFile = new MockMultipartFile("data", "filename.csv", "text/plain", "1,2,3".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/process-file")
                .file("csv_file", csvFile.getBytes()))
                .andExpect(status().is(200));
        Mockito.verify(csvProcessorService).processCsv(any());
    }
}
