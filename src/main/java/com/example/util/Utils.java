package com.example.util;

import java.util.concurrent.TimeUnit;

public class Utils {

    private Utils() {
    }

    public static void sleepCurrentThread(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
