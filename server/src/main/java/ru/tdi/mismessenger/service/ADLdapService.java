package ru.tdi.mismessenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.HardcodedFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;
import ru.tdi.mismessenger.Util;
import ru.tdi.mismessenger.entity.ItemType;
import ru.tdi.mismessenger.entity.MessageGroup;
import ru.tdi.mismessenger.entity.MessageGroupItem;
import ru.tdi.mismessenger.model.ADGroup;
import ru.tdi.mismessenger.model.ADObject;
import ru.tdi.mismessenger.model.ADOrgUnit;
import ru.tdi.mismessenger.model.ADUser;
import ru.tdi.mismessenger.repository.MessageGroupRepository;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.*;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class ADLdapService {

    @Autowired
    LdapTemplate ldapTemplate;

    @Autowired
    ContextSource contextSource;

    @Autowired
    MessageGroupRepository messageGroupRepository;

    @Cacheable(cacheNames = "adObjects")
    public ADObject getADObject(UUID guid) {
        List<ADObject> result =  ldapTemplate.search("", new HardcodedFilter("(objectGUID=" + Util.UUIDtoLdapRequest(guid) + ")").encode(),
                (AttributesMapper<ADObject>) (attrs) -> {
                    ADObject adObject = new ADObject();
                    adObject.setGuid(Util.bytesToUUID((byte[]) attrs.get("objectGuid").get()));
                    adObject.setDn(attrs.get("distinguishedName").get().toString());
                    if (attrs.get("cn")!=null) adObject.setName(attrs.get("cn").get().toString());
                    return adObject;
                });
        if (result.size()==1) return result.get(0);
        return null;
    }

    @Cacheable(cacheNames = "adDict", key="#root.methodName")
    public List<ADUser> getADUsers() {
        return ldapTemplate.search(
                query().where("objectcategory").is("person").and("objectclass").is("user"),
                (AttributesMapper<ADUser>) (attrs) -> {
                    ADUser user = new ADUser();
                    user.setDn(attrs.get("distinguishedName").get().toString());
                    user.setName(attrs.get("cn").get().toString());
                    user.setSAMAccountName(attrs.get("sAMAccountName").get().toString());
                    user.setGuid(Util.bytesToUUID((byte[]) attrs.get("objectGuid").get()));
                    return user;
        });
    }

    @Cacheable(cacheNames = "adDict", key="#root.methodName")
    public List<ADGroup> getADGroups() {
        return ldapTemplate.search(
                query().where("objectclass").is("group"),
                (AttributesMapper<ADGroup>) (attrs) -> {
                    ADGroup group = new ADGroup();
                    group.setGuid(Util.bytesToUUID((byte[]) attrs.get("objectGuid").get()));
                    group.setDn(attrs.get("distinguishedName").get().toString());
                    group.setName(attrs.get("cn").get().toString());
                    return group;
                });
    }

    @Cacheable(cacheNames = "adDict", key="#root.methodName")
    public List<ADOrgUnit> getADOrgUnits() {
        return ldapTemplate.search(
                query().where("objectclass").is("organizationalunit"),
                (AttributesMapper<ADOrgUnit>) (attrs) -> {
                    ADOrgUnit ou = new ADOrgUnit();
                    ou.setGuid(Util.bytesToUUID((byte[]) attrs.get("objectGuid").get()));
                    ou.setDn(attrs.get("distinguishedName").get().toString());
                    ou.setName(attrs.get("ou").get().toString());
                    return ou;
                });
    }

    @Cacheable(cacheNames = "adUsers")
    public ADUser getUserByName(String username) {
        List<ADUser> result =  ldapTemplate.search(
                query().where("objectcategory").is("person").and("objectclass").is("user").and("sAMAccountName").is(username),
                new ADUserMapper());
        if (result.size()==1) return result.get(0);
        return null;
    }

    @Cacheable(cacheNames = "adUsers")
    public ADUser getUserByGUID(String guid) {
        List<ADUser> result = ldapTemplate.search("", new HardcodedFilter("(objectGUID=" + Util.UUIDtoLdapRequest(UUID.fromString(guid)) + ")").encode(),
                new ADUserMapper());
        if (result.size()==1) return result.get(0);
        return null;
    }

}

class ADUserMapper implements AttributesMapper<ADUser> {
    @Override
    public ADUser mapFromAttributes(Attributes attrs) throws NamingException {
        ADUser user = new ADUser();
        user.setGuid(Util.bytesToUUID((byte[]) attrs.get("objectGuid").get()));
        user.setDn(attrs.get("distinguishedName").get().toString());
        user.setName(attrs.get("cn").get().toString());
        user.setSAMAccountName(attrs.get("sAMAccountName").get().toString());
        user.setMembers(new HashSet());
        Collections.list(attrs.get("memberOf").getAll()).stream().forEach((group) -> user.getMembers().add(group.toString()));
        return user;
    }
}
