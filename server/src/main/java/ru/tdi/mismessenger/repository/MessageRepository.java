package ru.tdi.mismessenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tdi.mismessenger.entity.Message;

import java.util.Collection;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("select new ru.tdi.mismessenger.entity.Message(msg.id,msg.author,msg.subject,msg.text,msg.date,msg.important,msg.needConfirm,msg.needAnswer,msg.startDate,msg.stopDate, msg.generated) from Message msg where msg.deleted=false order by msg.date desc")
    List<Message> findAllForListing();

    @Query("select new ru.tdi.mismessenger.entity.Message(msg.id,msg.author,msg.subject,msg.text,msg.date,msg.important,msg.needConfirm,msg.needAnswer,msg.startDate,msg.stopDate, msg.generated) from Message msg " +
            "where msg.msgGroup.id IN (?1) and msg.deleted=false and " +
            "(msg.startDate IS NULL OR CURRENT_DATE >= msg.startDate) and " +
            "(msg.stopDate IS NULL OR CURRENT_DATE  <= msg.stopDate) " +
            "order by msg.date asc")
    List<Message> findMessageByGroupIds(Collection<Integer> groups);
}
