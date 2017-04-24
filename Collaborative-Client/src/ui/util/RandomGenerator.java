/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String getRandom() {
        return new BigInteger(130, random).toString(32);
    }
}
