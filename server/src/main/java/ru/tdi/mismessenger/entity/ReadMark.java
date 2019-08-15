package ru.tdi.mismessenger.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "readMark", indexes = {
        @Index(name = "IDX_MSG", columnList = "message_id"),
        @Index(name = "IDX_RM", columnList = "message_id, GUID")
    })
@Data
@NoArgsConstructor
public class ReadMark {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    @JoinColumn(name = "message_id")
    Message message;

    @Column(name="markDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime date;

    @Column(name="GUID", length=36)
    @org.hibernate.annotations.Type(type="uuid-char")
    UUID guid;

    @Column(name="userName")
    String userName;

    @Column(name="computerName")
    String computerName;

    @Column(name="answer")
    String answer;

    public ReadMark(Integer id, LocalDateTime date, UUID guid, String userName, String computerName, String answer) {
        this.id = id;
        this.date = date;
        this.guid = guid;
        this.userName = userName;
        this.computerName = computerName;
        this.answer = answer;
    }
}
