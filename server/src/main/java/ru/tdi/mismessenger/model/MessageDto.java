package ru.tdi.mismessenger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    Integer id;
    String author;
    String subject;
    String text;
    String date;
    Boolean important;
    Boolean needConfirm;
    String needAnswer;
    Boolean readMark;
}
