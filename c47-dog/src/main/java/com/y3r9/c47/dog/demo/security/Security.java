package com.y3r9.c47.dog.demo.security;

import java.security.MessageDigest;
import java.security.Provider;
import java.util.Map;

/**
 * The class Security.
 *
 * @version 1.0
 */
final class Security {

    public static void main(String[] args) throws Exception {

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
}
