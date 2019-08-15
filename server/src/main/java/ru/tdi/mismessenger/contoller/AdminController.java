package ru.tdi.mismessenger.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.tdi.mismessenger.entity.Message;
import ru.tdi.mismessenger.entity.MessageGroup;
import ru.tdi.mismessenger.entity.ReadMark;
import ru.tdi.mismessenger.model.ADGroup;
import ru.tdi.mismessenger.model.ADOrgUnit;
import ru.tdi.mismessenger.model.ADUser;
import ru.tdi.mismessenger.service.ADLdapService;
import ru.tdi.mismessenger.service.MessageService;
import ru.tdi.mismessenger.service.MessageGroupDAO;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ADLdapService adLdapService;

    @Autowired
    MessageGroupDAO messageGroupDAO;

    @Autowired
    MessageService messageService;

    @RequestMapping("/users")
    public List<ADUser> getADUsers() {
        return adLdapService.getADUsers();
    }

    @RequestMapping("/users/{guid}")
    public ADUser getADUserByGUID(@PathVariable String guid) {
        return adLdapService.getUserByGUID(guid);
    }

    @RequestMapping("/groups")
    public List<ADGroup> getADGroups() {
        return adLdapService.getADGroups();
    }

    @RequestMapping("/ou")
    public List<ADOrgUnit> getADOUs() {
        return adLdapService.getADOrgUnits();
    }

    @RequestMapping(value = "/msgGroups", method = RequestMethod.GET)
    public List<MessageGroup> getMessageGroups() {
        return messageGroupDAO.findAllGroups();
    }

    @RequestMapping(value = "/msgGroups/{id}", method = RequestMethod.GET)
    public MessageGroup getMessageGroup(@PathVariable Integer id) {
        return messageGroupDAO.findGroupById(id);
    }

    @RequestMapping(value = "/msgGroups/{id}", method = RequestMethod.DELETE)
    public void removeMessageGroup(@PathVariable Integer id) {
        messageGroupDAO.deleteGroupById(id);
    }

    @RequestMapping(value = "/msgGroups", method = RequestMethod.POST)
    public MessageGroup saveMessageGroup(@RequestBody MessageGroup mg) {
        return messageGroupDAO.saveGroup(mg);
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public List<Message> getMessages() {
        return messageService.findAllMessages();
    }

    @RequestMapping(value = "/messages/{id}", method = RequestMethod.GET)
    public Message getMessage(@PathVariable Integer id) {
        return messageService.findMessageById(id);
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public Message saveMessage(@RequestBody Message msg) {
        return messageService.saveMessage(msg);
    }

    @RequestMapping(value = "/messages/{id}", method = RequestMethod.DELETE)
    public void removeMessage(@PathVariable Integer id) {
        messageService.deleteMessageById(id);
    }

    @RequestMapping(value = "/messages/{id}/readMarks", method = RequestMethod.GET)
    public List<ReadMark> getMessageReadMarks(@PathVariable Integer id) {
        return messageService.getReadMarksForMessage(id);
    }

    @RequestMapping(value = "/readMarks/{id}", method = RequestMethod.DELETE)
    public void removeReadMark(@PathVariable Integer id) {
        messageService.deleteReadMarkById(id);
    }

}
