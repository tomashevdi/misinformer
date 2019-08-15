package ru.tdi.mismessenger.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "message", indexes = { @Index(name = "IDX_DT", columnList = "dt"), @Index(name = "IDX_STARTDT", columnList = "startDate"), @Index(name = "IDX_STOPDT", columnList = "stopDate")  })
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @Column(name="author", length=50)
    String author;

    @Column(name="subject", length=150)
    String subject;

    @Column(name="message", columnDefinition = "LONGTEXT")
    String text;

    @Column(name="dt")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate date;

    @Column(name="important")
    Boolean important;

    @Column(name="needConfirm")
    Boolean needConfirm;

    @Column(name="needAnswer")
    String needAnswer;

    @Column(name="startDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate;

    @Column(name="stopDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate stopDate;

    @Column(name="deleted")
    Boolean deleted = false;

    @Column(name="generated")
    Boolean generated = false;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    MessageGroup msgGroup;


    public Message(Integer id, String author, String subject, String text, LocalDate date, Boolean important, Boolean needConfirm, String needAnswer, LocalDate startDate, LocalDate stopDate, Boolean generated) {
        this.id = id;
        this.author = author;
        this.subject = subject;
        this.text = text;
        this.date = date;
        this.important = important;
        this.needConfirm = needConfirm;
        this.needAnswer = needAnswer;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.generated = generated;
    }

}
