package com.greentech.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailTask implements Runnable {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "email-worker");
        t.setDaemon(true);
        return t;
    });

    private final String email;
    public EmailTask(String email) { this.email = email; }

    @Override
    public void run() {
        System.out.println(">> [Background Thread] Sending confirmation email to " + email);
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println(">> [Background Thread] Email sent!");
    }

    public static void sendEmailAsync(String email) {
        executor.submit(new EmailTask(email));
    }

    public static void shutdown() {
        executor.shutdown();
    }
}