package com.sarbacane.entry_test.service;

import com.google.common.io.ByteStreams;
import com.sarbacane.entry_test.exceptions.CSVException;
import com.sarbacane.entry_test.feign.RecipientFeignClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CSVProcessorServiceTest {

    @Mock
    private RecipientFeignClient recipientFeignClient;

    @InjectMocks
    private CSVProcessorService tested;

    @Test
    public void shouldProcessNewRecipients() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "Sample.csv",
                ByteStreams.toByteArray(this.getClass().getClassLoader().getResourceAsStream("Sample.csv")));
        tested.processCsv(mockMultipartFile);
        verify(recipientFeignClient).processNewRecipients(any());
    }

    @Test(expected = CSVException.class)
    public void shouldThrowExceptionWhenEmailIsInvalid() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "Sample_WrongEmail.csv",
                ByteStreams.toByteArray(this.getClass().getClassLoader().getResourceAsStream("Sample_WrongEmail.csv")));
        tested.processCsv(mockMultipartFile);
        verify(recipientFeignClient, never()).processNewRecipients(any());
    }

    @Test(expected = CSVException.class)
    public void shouldThrowExceptionWhenPhoneIsInvalid() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "Sample_WrongPhone.csv",
                ByteStreams.toByteArray(this.getClass().getClassLoader().getResourceAsStream("Sample_WrongPhone.csv")));
        tested.processCsv(mockMultipartFile);
        verify(recipientFeignClient, never()).processNewRecipients(any());
    }

}
