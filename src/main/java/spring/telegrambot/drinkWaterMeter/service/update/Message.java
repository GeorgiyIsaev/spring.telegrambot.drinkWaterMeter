package spring.telegrambot.drinkWaterMeter.service.update;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;

public class Message {
    private String chatId;
    private String username;
    private Instant time;
    private Integer messageId;
    private String text;


    public Message() {
    }
    public Message(Update update) {
        this.chatId = update.getMessage().getChatId().toString();
        this.username = update.getMessage().getFrom().getUserName();
        this.time = Instant.ofEpochSecond(update.getMessage().getDate());
        this.messageId = update.getMessage().getMessageId();
        this.text = update.getMessage().getText();
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

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
