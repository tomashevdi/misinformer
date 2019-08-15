package ru.tdi.mismessenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.tdi.mismessenger.entity.*;
import ru.tdi.mismessenger.model.ADUser;
import ru.tdi.mismessenger.model.MessageDto;
import ru.tdi.mismessenger.repository.MessageGroupRepository;
import ru.tdi.mismessenger.repository.MessageRepository;
import ru.tdi.mismessenger.repository.ReadMarkRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessageGroupRepository messageGroupRepository;

    @Autowired
    ReadMarkRepository readMarkRepository;

    @Autowired
    ADLdapService adLdapService;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public List<Message> findAllMessages() {
        return messageRepository.findAllForListing();
    }

    public Message findMessageById(Integer id) { return messageRepository.findById(id).get();}


    public Message saveMessage(Message mg) {
        mg.setMsgGroup(messageGroupRepository.save(mg.getMsgGroup()));
        return messageRepository.save(mg);
    }

    public void deleteMessageById(Integer id) {
        Message msg = messageRepository.findById(id).get();
        msg.setDeleted(true);
        messageRepository.save(msg);
    }

    public List<ReadMark> getReadMarksForMessage(Integer id) {
        return readMarkRepository.findAllForMessageId(id);
    }

    public void deleteReadMarkById(Integer id) {
        readMarkRepository.deleteById(id);
    }

    public void markMessage(Integer id, String userName, String computerName, String answer) {
        ReadMark mark = new ReadMark();
        mark.setAnswer(answer);
        mark.setComputerName(computerName);
        mark.setUserName(userName);
        mark.setDate(LocalDateTime.now());
        mark.setMessage(messageRepository.findById(id).get());
        mark.setGuid(adLdapService.getUserByName(userName).getGuid());
        readMarkRepository.save(mark);
    }

    public List<MessageDto> getMessagesForUser(String userName) {
        ADUser u = adLdapService.getUserByName(userName);
        if (u==null) return new ArrayList<>();

        List<String> guids = new ArrayList<>();
        guids.add(u.getGuid().toString());
        guids.addAll(adLdapService.getADGroups().stream().filter( g -> u.getMembers().contains(g.getDn())).map( g-> g.getGuid().toString()).collect(Collectors.toList()));
        guids.addAll(adLdapService.getADOrgUnits().stream().filter( ou -> u.getDn().endsWith(ou.getDn())).map( ou -> ou.getGuid().toString()).collect(Collectors.toList()));

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("guids", guids);

        String queryP = "select distinct mgi.group_id from messageGroupItems mgi " +
                "left join messageGroupItems mgiN on mgi.group_id=mgiN.group_id AND mgiN.notFlag=1 AND mgiN.GUID in (:guids) " +
                "where (mgi.notFlag=0 or mgi.notFlag is null) and mgi.GUID in (:guids) and mgiN.id is null ";
        List<Integer> msgGroupsP = jdbcTemplate.queryForList(queryP,param,Integer.class);

        String queryN = "select distinct mgi.group_id from messageGroupItems mgi " +
                "where mgi.notFlag=1 and mgi.GUID in (:guids) ";
        List<Integer> msgGroupsN = jdbcTemplate.queryForList(queryN,param,Integer.class);
        if (msgGroupsN.isEmpty()) msgGroupsN.add(-1);

        if (msgGroupsP.isEmpty()) return new ArrayList<>();

        graphLookup(msgGroupsP, msgGroupsN);

        return messageRepository.findMessageByGroupIds(msgGroupsP).stream()
                .map( (msg) -> {
            MessageDto m = new MessageDto();
            m.setAuthor(msg.getAuthor());
            m.setId(msg.getId());
            m.setImportant(msg.getImportant());
            m.setNeedAnswer(msg.getNeedAnswer());
            m.setNeedConfirm(msg.getNeedConfirm());
            m.setSubject(msg.getSubject());
            m.setText(msg.getText());
            m.setDate(msg.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yy")));
            m.setReadMark(readMarkRepository.checkReadMessage(msg.getId(),u.getGuid()));
            return m;
        }).collect(Collectors.toList());

    }

    private void graphLookup(List<Integer> pos, List<Integer> neg) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("pos", pos);
        param.addValue("neg", neg);
        String query = "select distinct group_id from messageGroupItems mgi " +
                "where mgi.itemType='MSG_GROUP' and mgi.group_id NOT IN (:pos) and mgi.group_id NOT IN (:neg) and mgi.msgGroup_id IN (:pos)";
        List<Integer> msgGroups = jdbcTemplate.queryForList(query,param,Integer.class);
        if (!msgGroups.isEmpty()) {
            pos.addAll(msgGroups);
            msgGroups.stream().forEach((g) -> graphLookup(pos, neg));
        }
    }


}
