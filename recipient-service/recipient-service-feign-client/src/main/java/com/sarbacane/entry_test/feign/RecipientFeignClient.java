package com.sarbacane.entry_test.feign;

import com.sarbacane.entry_test.json.ProcessRecipientsRequest;
import com.sarbacane.entry_test.json.Recipient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "recipient-service",
        path = "/recipient-service/v1")
public interface RecipientFeignClient {

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/process-recipients")
    void processNewRecipients(@Valid @RequestBody ProcessRecipientsRequest request);

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/process-recipients")
    void editRecipients(@Valid @RequestBody ProcessRecipientsRequest request);

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/process-recipients")
    void deleteRecipients(@Valid @RequestBody ProcessRecipientsRequest request);

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/recipients")
    @ResponseBody
    List<Recipient> getRecipients();

}
