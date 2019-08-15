package ru.tdi.mismessenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tdi.mismessenger.entity.MessageGroup;

import java.util.List;

@Repository
public interface MessageGroupRepository extends JpaRepository<MessageGroup,Integer> {
    @Query("select new ru.tdi.mismessenger.entity.MessageGroup(mg.id,mg.name,mg.predefined,mg.comment) from MessageGroup mg where mg.predefined=true and mg.deleted=false")
    List<MessageGroup> findAllForListing();
}
