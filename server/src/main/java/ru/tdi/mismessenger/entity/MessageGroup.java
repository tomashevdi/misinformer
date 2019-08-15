package ru.tdi.mismessenger.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messageGroup")
@Data
@NoArgsConstructor
public class MessageGroup {

    public MessageGroup(Integer id, String name, Boolean predefined, String comment) {
        this.id = id;
        this.name = name;
        this.predefined = predefined;
        this.comment = comment;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @Column(name="name", length=50)
    String name;

    @Column(name="predefined")
    Boolean predefined = false;

    @Column(name="comment", length=250)
    String comment = "";

    @Column(name="deleted")
    Boolean deleted = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "group_id")
    List<MessageGroupItem> items = new ArrayList<>();
}
