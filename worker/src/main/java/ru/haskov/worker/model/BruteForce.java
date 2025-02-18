package ru.haskov.worker.model;

import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.paukov.combinatorics.CombinatoricsFactory.createPermutationWithRepetitionGenerator;
import static org.paukov.combinatorics.CombinatoricsFactory.createVector;

@Component
public class BruteForce {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String HASH_TYPE = "MD5";
    private static final String ENCODING = "UTF-8";

    public String hack(String hash, int maxLength, int partNumber, int partCount) {
        int startIndex = ALPHABET.length() / partCount * partNumber;
        int endIndex = ALPHABET.length() / partCount * (partNumber + 1);
        endIndex = (partNumber == partCount - 1) ? ALPHABET.length() : endIndex;
        ICombinatoricsVector<String> vector = createVector(List.of(ALPHABET.split("")));

        for (int i = 1; i <= maxLength; i++) {
            for (int j = startIndex; j < endIndex; j++) {
                Generator<String> gen = createPermutationWithRepetitionGenerator(vector, i - 1);
                for (ICombinatoricsVector<String> perm : gen) {
                    String password = ALPHABET.toCharArray()[j] + String.join("", perm.getVector());
                    String permHash = getHash(password);
                    if (hash.equalsIgnoreCase(permHash)) {
                        return password;
                    }
                }
            }
        }
        return "";
    }

    private String getHash(String password) {
        try {
            byte[] bytesOfMessage = password.getBytes(ENCODING);
            MessageDigest md = MessageDigest.getInstance(HASH_TYPE);
            byte[] theMD5digest = md.digest(bytesOfMessage);
            return bytesToHex(theMD5digest);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
