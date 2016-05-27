/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.utils;

import java.math.BigInteger;
import java.security.*;

/**
 *
 * @author Hamed Ara
 */
public class HashUtil {
    public static String md5(String input) {
        String md5 = null;
        if(null == input) return null;
        try {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(input.getBytes(), 0, input.length());
        md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        }
        return md5;
    }
}
