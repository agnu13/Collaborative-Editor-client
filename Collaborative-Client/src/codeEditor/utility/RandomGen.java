package codeEditor.utility;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomGen {

    private static final SecureRandom random = new SecureRandom();

    public static String getRandom() {
        return new BigInteger(50, random).toString(16);
    }
}
