package com.sarbacane.entry_test.service;

import com.sarbacane.entry_test.json.ProcessRecipientsRequest;
import com.sarbacane.entry_test.json.Recipient;
import com.sarbacane.entry_test.model.RecipientEntity;
import com.sarbacane.entry_test.repository.RecipientRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipientServiceTest {

    @Mock
    private RecipientRepository recipientRepository;

    @InjectMocks
    private RecipientService tested;

    @Test
    public void shouldInsertUniqueNames() throws Exception {
        List<Recipient> recipients = new ArrayList<>();
        for (int i=0;i<10;i++) {
            recipients.add(createRecipient(i));
        }
        recipients.add(createRecipient(1));
        when(recipientRepository.existsByName("Name 1")).thenReturn(false).thenReturn(true);
        ProcessRecipientsRequest request = new ProcessRecipientsRequest();
        request.setRecipients(recipients);
        tested.processNewRecipients(request);
        verify(recipientRepository, times(10)).save(any());
    }

    @Test
    public void shouldEditExistingNames() throws Exception {
        List<Recipient> recipients = new ArrayList<>();
        for (int i=0;i<10;i++) {
            recipients.add(createRecipient(i));
        }
        RecipientEntity recipientEntity = Mockito.mock(RecipientEntity.class);
        when(recipientRepository.findByName("Name 1")).thenReturn(recipientEntity);
        ProcessRecipientsRequest request = new ProcessRecipientsRequest();
        request.setRecipients(recipients);
        tested.editRecipients(request);
        verify(recipientRepository, times(1)).save(any());
    }

    @Test
    public void shouldDeleteExistingNames() throws Exception {
        List<Recipient> recipients = new ArrayList<>();
        for (int i=0;i<10;i++) {
            recipients.add(createRecipient(i));
        }
        RecipientEntity recipientEntity = createRecipientEntity(1);
        when(recipientRepository.findByName("Name 1")).thenReturn(recipientEntity);
        ProcessRecipientsRequest request = new ProcessRecipientsRequest();
        request.setRecipients(recipients);
        tested.deleteRecipients(request);
        verify(recipientRepository, times(1)).deleteByName("Name 1");
    }

    @Test
    public void shouldReturnAllExistingRecipients() throws Exception {
        List<RecipientEntity> recipientEntities = Arrays.asList(createRecipientEntity(1), createRecipientEntity(2));
        when(recipientRepository.findAll()).thenReturn(recipientEntities);
        List<Recipient> response = tested.getRecipients();
        Assert.assertEquals(2, response.size());
    }

    private Recipient createRecipient(int i) {
        Recipient recipient = new Recipient();
        recipient.setRecipientName("Name "+i);
        recipient.setPhoneNumber("+3469612345"+i);
        recipient.setEmailAddress("sample"+i+"@gmail.com");
        return recipient;
    }

    private RecipientEntity createRecipientEntity(int i) {
        RecipientEntity recipient = Mockito.mock(RecipientEntity.class);
        when(recipient.getName()).thenReturn("Name "+i);
        when(recipient.getEmail()).thenReturn("sample"+i+"@gmail.com");
        when(recipient.getPhoneNumber()).thenReturn("+3469612345"+i);
        return recipient;
    }

}
