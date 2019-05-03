package com.sarbacane.entry_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sarbacane.entry_test.json.ProcessRecipientsRequest;
import com.sarbacane.entry_test.json.Recipient;
import com.sarbacane.entry_test.service.RecipientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipientControllerTest {

    @MockBean
    private RecipientService recipientService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnStatusOkAndRecipientList() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Recipient> recipients = Arrays.asList(new Recipient(), new Recipient());
        Mockito.when(recipientService.getRecipients()).thenReturn(recipients);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/recipients")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(recipients)));
    }

    @Test
    public void shouldProcessNewRecipientsWhenMethodIsPut() throws Exception {
        String requestJson = getRequestJson();

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/process-recipients")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(recipientService).processNewRecipients(any());
    }

    @Test
    public void shouldEditRecipientsWhenMethodIsPost() throws Exception {
        String requestJson = getRequestJson();

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/process-recipients")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(recipientService).editRecipients(any());
    }

    @Test
    public void shouldDeleteRecipientsWhenMethodIsDelete() throws Exception {
        String requestJson = getRequestJson();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/process-recipients")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(recipientService).deleteRecipients(any());
    }

    private String getRequestJson() throws Exception {
        Recipient recipient = new Recipient();
        recipient.setRecipientName("Name");
        recipient.setPhoneNumber("+34696123456");
        recipient.setEmailAddress("email@domain.com");
        ProcessRecipientsRequest request = new ProcessRecipientsRequest();
        request.setRecipients(Arrays.asList(recipient));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }

}
