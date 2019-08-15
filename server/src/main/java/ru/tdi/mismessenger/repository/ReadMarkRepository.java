package ru.tdi.mismessenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tdi.mismessenger.entity.Message;
import ru.tdi.mismessenger.entity.ReadMark;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReadMarkRepository extends JpaRepository<ReadMark,Integer> {
    @Query("select new ru.tdi.mismessenger.entity.ReadMark(rm.id, rm.date, rm.guid, rm.userName, rm.computerName, rm.answer) from ReadMark rm where rm.message.id = ?1")
    List<ReadMark> findAllForMessageId(Integer id);

    @Query("select CASE WHEN COUNT(rm) > 0 THEN true ELSE false END from ReadMark rm where rm.message.id = ?1 and rm.guid=?2")
    Boolean checkReadMessage(Integer id, UUID guid);
}
