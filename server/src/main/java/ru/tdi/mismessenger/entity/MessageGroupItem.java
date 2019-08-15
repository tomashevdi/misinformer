package ru.tdi.mismessenger.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "messageGroupItems", indexes = {
        @Index(name = "IDX_IT", columnList = "itemType"),
        @Index(name = "IDX_MG", columnList = "msgGroup_id"),
        @Index(name = "IDX_GUID", columnList = "guid"),
        @Index(name = "IDX_NF", columnList = "notFlag")  })
@Data
public class MessageGroupItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name="itemType", length=10)
    ItemType itemType;

    @Column(name="GUID", length=36)
    @org.hibernate.annotations.Type(type="uuid-char")
    UUID guid;

    @Column(name="msgGroup_id")
    Integer msgGroupId;

    @Column(name="notFlag")
    Boolean notFlag = false;
}
