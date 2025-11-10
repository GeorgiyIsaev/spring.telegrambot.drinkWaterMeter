package spring.telegrambot.drinkWaterMeter.service.update;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;

public class CallbackQuery {
    private String chatId;
    private String username;
    private Instant time;
    private Integer messageId;
    private String dataCallbackQuery;

    public CallbackQuery() {
    }

    public CallbackQuery(Update update) {
        this.chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        this.username = update.getCallbackQuery().getFrom().getUserName();
        this.time = Instant.ofEpochSecond(update.getCallbackQuery().getMessage().getDate());
        this.messageId = update.getCallbackQuery().getMessage().getMessageId();
        this.dataCallbackQuery= update.getCallbackQuery().getData();
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getData() {
        return dataCallbackQuery;
    }

    public void setDataCallbackQuery(String dataCallbackQuery) {
        this.dataCallbackQuery = dataCallbackQuery;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }
}
