package ru.tdi.mismessenger.model;

import lombok.Data;
import java.util.Set;

@Data
public class ADUser extends  ADObject{
    String sAMAccountName;
    private Set<String> members;
}
