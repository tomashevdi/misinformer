package ru.tdi.mismessenger;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Util {
    public static UUID bytesToUUID(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }

    public static byte[] UUIDtoBytes(UUID uuid)
    {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static String UUIDtoLdapRequest(UUID uuid)
    {
        String s = uuid.toString().replaceAll("-","");
        StringBuilder sb  = new StringBuilder("\\");
        for (int i = 0; i < s.length(); i++) {
            sb.append(s.charAt(i));
            if (i%2==1 && i!=s.length()-1) sb.append("\\");
        }
        return sb.toString();
    }
}
