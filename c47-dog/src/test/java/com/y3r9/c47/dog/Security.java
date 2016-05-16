package com.y3r9.c47.dog;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Signature;
import java.util.Map;
import org.junit.Test;

/**
 * The class Security.
 *
 * @version 1.0
 */
public final class Security {

    @Test
    public void test1() throws Exception {

        for (Provider p : java.security.Security.getProviders()) {
            System.out.println(p);
            for (Map.Entry<Object, Object> entry : p.entrySet()) {
                System.out.println(entry.getKey());
            }
        }

        byte[] input = "sha".getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA");
        sha.update(input);
        byte[] output = sha.digest();

    }

    @Test
    public void test2() throws Exception {
        AlgorithmParameters ap = AlgorithmParameters.getInstance("DES");
        ap.init(new BigInteger("1905987943858347593").toByteArray());
        byte[] b = ap.getEncoded();
        System.out.println(new BigInteger(b).toString());
    }

    @Test
    public void test3() throws Exception {
        Signature signature = Signature.getInstance("md5withrsa");
    }



}
