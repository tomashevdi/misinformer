package ru.tdi.mismessenger.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.tdi.mismessenger.model.MessageDto;
import ru.tdi.mismessenger.repository.ReadMarkRepository;
import ru.tdi.mismessenger.service.ADLdapService;
import ru.tdi.mismessenger.service.MessageService;
import java.util.List;

@RestController
@RequestMapping("/app")
public class CliController {

    @Autowired
    MessageService messageService;

    @Autowired
    ReadMarkRepository readMarkRepository;

    @Autowired
    ADLdapService adLdapService;

    @RequestMapping(value="messages", method = RequestMethod.GET)
    public List<MessageDto> getMessages(@RequestParam String user, @RequestParam String computer) {
        return messageService.getMessagesForUser(user);
    }

    @RequestMapping(value="messages/{id}", method = RequestMethod.GET)
    public String getMessages(@RequestParam String user,@RequestParam String computer, @RequestParam(defaultValue = "") String answer, @PathVariable("id") Integer id) {
        messageService.markMessage(id,user,computer,answer);
        return "success";
    }
}
