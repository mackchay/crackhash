package ru.haskov.worker.model;

import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.paukov.combinatorics.CombinatoricsFactory.createPermutationWithRepetitionGenerator;
import static org.paukov.combinatorics.CombinatoricsFactory.createVector;

@Component
public class BruteForce {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String HASH_TYPE = "MD5";
    private static final String ENCODING = "UTF-8";
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public List<String> hack(String hash, int maxLength, int partNumber, int partCount) {
        int startIndex = ALPHABET.length() / partCount * partNumber;
        int endIndex = ALPHABET.length() / partCount * (partNumber + 1);
        endIndex = (partNumber == partCount - 1) ? ALPHABET.length() : endIndex;
        ICombinatoricsVector<String> vector = createVector(List.of(ALPHABET.split("")));
        List<String> passwords = new ArrayList<>();

        for (int i = 1; i <= maxLength; i++) {
            Generator<String> gen = createPermutationWithRepetitionGenerator(vector, i - 1);
            for (String firstChar : ALPHABET.substring(startIndex, endIndex).split("")) {
                for (ICombinatoricsVector<String> perm : gen) {
                    String password = firstChar + String.join("", perm.getVector());
                    if (hash.equalsIgnoreCase(getHash(password))) {
                        passwords.add(password);
                    }
                }
            }
        }
        return passwords;
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
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }


}
