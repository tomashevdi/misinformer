package ru.tdi.mismessenger.model;

import lombok.Data;

import java.util.UUID;

@Data
public class ADObject {
    UUID guid;
    String dn;
    String name;
}
