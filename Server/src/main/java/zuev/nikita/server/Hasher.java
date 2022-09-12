package zuev.nikita.server;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class that hashes a string. Result is a HEX-string
 *
 */

public class Hasher {
    public static String getHashSHA384(String text){

        MessageDigest m= null;
        try {
            m = MessageDigest.getInstance("SHA-384");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        m.digest(text.getBytes());
        BigInteger bi = new BigInteger(1,m.digest());

        return bi.toString(16);
    }
}
