package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

public class MiniArbuzikBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    public MiniArbuzikBot(String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendWelcomeMessage(chatId);
            } else {
                sendMessage(chatId, "Я получил ваше сообщение: " + messageText);
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            handleCallbackQuery(callbackData, chatId);
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            long chatId = update.getMessage().getChatId();
            String fileId = update.getMessage().getPhoto().get(0).getFileId();
            System.out.println("Received photo with file_id: " + fileId);
            sendMessage(chatId, "Я получил фото! Его file_id: " + fileId);
        }
    }

    private void sendWelcomeMessage(long chatId) {
        List<InlineKeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(new InlineKeyboardRow(
                InlineKeyboardButton.builder().text("Открыть поздравление").callbackData("GREETING").build(),
                InlineKeyboardButton.builder().text("Наши прогулки").callbackData("WALKS").build()
        ));
        keyboard.add(new InlineKeyboardRow(
                InlineKeyboardButton.builder().text("Наши подарки").callbackData("GIFTS").build()
        ));


        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboard(keyboard).build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Привет! Я твой личный бот-открытка и шкатулка воспоминаний. Выбери, что хочешь посмотреть:")
                .replyMarkup(inlineKeyboardMarkup)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallbackQuery(String callbackData, long chatId) {
        switch (callbackData) {
            case "GREETING":
                sendGreeting(chatId);
                break;
            case "WALKS":
                sendWalks(chatId);
                break;
            case "GIFTS":
                sendGifts(chatId);
                break;
            default:
                sendMessage(chatId, "Неизвестная команда.");
        }
    }

    private void sendGreeting(long chatId) {
        sendMessage(chatId, "Катя, поздравляю тебя с днём твоего семнадцатилетия и желаю тебе, чтобы твои мечты наконец перестали быть просто мечтами и начали сбываться\n" +
                "Чтобы ты почаще улыбалась и кайфовала от жизни\n" +
                "Я тебя очень люблю, целую и обнимаю!\n" +
                "Твой парень Марсель)\n" +
                "(да мы встречаемся xD)");
        sendWelcomeMessage(chatId);
    }

    private void sendWalks(long chatId) {
        sendMessage(chatId, "Вот несколько фото с наших прогулок:");
        sendPhoto(chatId, "AgACAgIAAxkBAAMxafdOwFHWzvxtagjVNY3ToYyBJIIAAo0UaxtkSsFLYMa25xci-jEBAAMCAANzAAM7BA", "Наша первая встреча и совместная фотка, ля такие красивые тут получились я прям балдею :З");
        sendPhoto(chatId, "AgACAgIAAxkBAAMzafdO5oND2nhtBbizNGZAhw8jIe4AAo8UaxtkSsFLSmtn4kJ-eJMBAAMCAANzAAM7BA", "Великая голубиная депутатская дума *_*");
        sendPhoto(chatId, "AgACAgIAAxkBAAM1afdO6aMvkr_RT1goQjHXr8yW0eQAApAUaxtkSsFLnn8bW0C3ffEBAAMCAANzAAM7BA", "Родничок, это еще где чуть не убились в лестницы xD");
        sendPhoto(chatId, "AgACAgIAAxkBAAM3afdO63bnAf3o0VueKy02OkEOsAMAApEUaxtkSsFLp9wgUvqWkd4BAAMCAANzAAM7BA", "Красивший вид на волгу, пока гуляли по огромному бордюру бррр");
        sendPhoto(chatId, "AgACAgIAAxkBAAM5afdO9csrKqRBvy1MalgRLYY09nsAApIUaxtkSsFLSZazxfCI22QBAAMCAANzAAM7BA", "Прекрасный сырой лес, где понял зачем дам пропускают вперед)");
        sendPhoto(chatId, "AgACAgIAAxkBAAM7afdO_DLG2S2i-onM9UxtTJsWiWEAApMUaxtkSsFLDSQKbvMI4T8BAAMCAANzAAM7BA", "Тут я еще не знал что зайдем к те домой.. Ахахахах Я прям балдею когда вижу какая ты счастливая и прям светишься вся)");
        sendWelcomeMessage(chatId);
    }

    private void sendGifts(long chatId) {
        sendMessage(chatId, "Наши подарки:");
        sendPhoto(chatId, "AgACAgIAAxkBAAMTafdHOQYGbQlOrIzynTS-0J2mV-8AAmsUaxtkSsFLf6pHoDK9y60BAAMCAANzAAM7BA", "Это проста так милаааа :>");
        sendPhoto(chatId, "AgACAgIAAxkBAAMRafdF7GQHcm0iylj65SmQGtyyQnIAAmYUaxtkSsFLimXON2Bf_H8BAAMCAANzAAM7BA", "Хехе, духи до сих пор не выветрились..)");
        sendPhoto(chatId, "AgACAgIAAxkBAAMbafdLA02o2eWwKT5NF4jXR_wrmKsAAoIUaxtkSsFLlIGvcm_ZlgEBAAMCAANzAAM7BA", "моя открыточка на нашу первую встречу ^^");
        sendPhoto(chatId, "AgACAgIAAxkBAAMVafdHoHhvkkXC0-SqchKHtrxIjMcAAm4UaxtkSsFLaew7g0jch2IBAAMCAANzAAM7BA", "Помню как разрывался от любопытства когда после встречи с тобой летел на такси домой и так хотел прочитать открытку, кстати чай очень вкусный спасибки :з");
        sendPhoto(chatId, "AgACAgIAAxkBAAMXafdHzV5QiKyaFQnzkwABFnQTffu-AAJvFGsbZErBS9HZmYoEgIoIAQADAgADcwADOwQ", "В первый раз понял что люблю тебя");
        sendPhoto(chatId, "AgACAgIAAxkBAAMZafdIwPvPZs_mP4hTnwABw5dF4HmXAAJ4FGsbZErBS4eh-Jmh7Vf_AQADAgADcwADOwQ", "Тут должны были быть сырки но к моменту созданию подарка я их уже съел :ъ была вкусна СПС");
        sendWelcomeMessage(chatId);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(long chatId, String fileId, String caption) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(fileId))
                .caption(caption)
                .build();
        try {
            telegramClient.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}