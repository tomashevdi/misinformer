package ru.tdi.mismessenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import ru.tdi.mismessenger.entity.MessageGroup;
import ru.tdi.mismessenger.repository.MessageGroupRepository;

import java.util.List;

@Service
public class MessageGroupDAO {

    @Autowired
    MessageGroupRepository messageGroupRepository;

    public List<MessageGroup> findAllGroups() {
        return messageGroupRepository.findAllForListing();
    }

    public MessageGroup findGroupById(Integer id) { return messageGroupRepository.findById(id).get();}

    public MessageGroup saveGroup(MessageGroup mg) { return messageGroupRepository.save(mg);}

    public void deleteGroupById(Integer id) {
        MessageGroup mg  = messageGroupRepository.findById(id).get();
        mg.setDeleted(true);
        messageGroupRepository.save(mg);
    }

}
