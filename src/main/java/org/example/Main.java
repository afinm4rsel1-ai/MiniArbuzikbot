package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
        String botToken = "8415904817:AAF3_qdL9dpLsAnu_w8vMO15xDIaP5TJklo";

        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new MiniArbuzikBot(botToken));
            System.out.println("MiniArbuzikBot started!");
            Thread.currentThread().join();
        } catch (TelegramApiException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}