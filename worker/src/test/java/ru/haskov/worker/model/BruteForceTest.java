package ru.haskov.worker.model;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BruteForceTest {

    @Test
    public void test() {
        BruteForce bf = new BruteForce();
        String hash = "8c247609d4ea0437745a3af160f1bbb2";
        List<String> passwords = bf.hack(hash, 5, 0, 1);
        System.out.println(passwords);
        assertTrue(passwords.contains("abxy"));
    }
}