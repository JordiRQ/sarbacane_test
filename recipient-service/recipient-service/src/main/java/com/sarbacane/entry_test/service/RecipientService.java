package com.sarbacane.entry_test.service;

import com.sarbacane.entry_test.exceptions.RecipientServiceException;
import com.sarbacane.entry_test.json.ProcessRecipientsRequest;
import com.sarbacane.entry_test.json.Recipient;
import com.sarbacane.entry_test.model.RecipientEntity;
import com.sarbacane.entry_test.repository.RecipientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RecipientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipientService.class);

    @Autowired
    private RecipientRepository recipientRepository;

    public void processNewRecipients(ProcessRecipientsRequest request) throws RecipientServiceException {
        for (Recipient recipient: request.getRecipients()) {
            try {
                if (recipientRepository.existsByName(recipient.getRecipientName())) {
                    LOGGER.warn(String.format("Bypassing request to insert existing recipient %s", recipient.getRecipientName()));
                } else {
                    LOGGER.debug(String.format("Processing recipient %s", recipient.getRecipientName()));
                    RecipientEntity recipientEntity = new RecipientEntity();
                    recipientEntity.setName(recipient.getRecipientName());
                    recipientEntity.setEmail(recipient.getEmailAddress());
                    recipientEntity.setPhoneNumber(recipient.getPhoneNumber());
                    recipientRepository.save(recipientEntity);
                }
            } catch (InvalidDataAccessApiUsageException | DataIntegrityViolationException e) {
                LOGGER.error("Bypassing request to store invalid data.", e);
            } catch (Exception e) {
                LOGGER.error("Exception occurred while trying to save entity.", e);
                throw new RecipientServiceException("Unexpected error occurred.");
            }
        }
    }

    public void editRecipients(ProcessRecipientsRequest request) throws RecipientServiceException {
        for (Recipient recipient: request.getRecipients()) {
            try {
                RecipientEntity recipientEntity = recipientRepository.findByName(recipient.getRecipientName());
                if (Objects.isNull(recipientEntity)) {
                    LOGGER.warn(String.format("Bypassing request to edit non-existing recipient %s", recipient.getRecipientName()));
                } else {
                    LOGGER.debug(String.format("Processing recipient %s", recipient.getRecipientName()));
                    recipientEntity.setEmail(recipient.getEmailAddress());
                    recipientEntity.setPhoneNumber(recipient.getPhoneNumber());
                    recipientRepository.save(recipientEntity);
                }
            } catch (InvalidDataAccessApiUsageException | DataIntegrityViolationException e) {
                LOGGER.error("Bypassing request to store invalid data.", e);
            } catch (Exception e) {
                LOGGER.error("Exception occurred while trying to save entity.", e);
                throw new RecipientServiceException("Unexpected error occurred.");
            }
        }
    }

    public void deleteRecipients(ProcessRecipientsRequest request) throws RecipientServiceException {
        for (Recipient recipient: request.getRecipients()) {
            try {
                RecipientEntity recipientEntity = recipientRepository.findByName(recipient.getRecipientName());
                if (Objects.isNull(recipientEntity)) {
                    LOGGER.warn(String.format("Bypassing request to delete non-existing recipient %s", recipient.getRecipientName()));
                } else {
                    LOGGER.debug(String.format("Processing recipient %s", recipient.getRecipientName()));
                    recipientRepository.deleteByName(recipient.getRecipientName());
                }
            } catch (InvalidDataAccessApiUsageException | DataIntegrityViolationException e) {
                LOGGER.error("Bypassing request to store invalid data.", e);
            } catch (Exception e) {
                LOGGER.error("Exception occurred while trying to delete entity.", e);
                throw new RecipientServiceException("Unexpected error occurred.");
            }
        }
    }

    public List<Recipient> getRecipients() throws RecipientServiceException {
        Iterable<RecipientEntity> recipientEntities;
        try {
            recipientEntities = recipientRepository.findAll();
        } catch (Exception e) {
            LOGGER.error("Exception occurred while trying to find entities.", e);
            throw new RecipientServiceException("Unexpected error occurred.");
        }
        List<Recipient> recipients = new ArrayList<>();
        for (RecipientEntity recipientEntity: recipientEntities) {
            LOGGER.debug(String.format("Mapping recipient %s", recipientEntity.getName()));
            Recipient recipient = new Recipient();
            recipient.setEmailAddress(recipientEntity.getEmail());
            recipient.setPhoneNumber(recipientEntity.getPhoneNumber());
            recipient.setRecipientName(recipientEntity.getName());
            recipients.add(recipient);
        }
        return recipients;
    }
}
