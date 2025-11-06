package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Sex implements Action{
    @Override
    public SendMessage generateRequest(Update update) {
        String message = "Укажите ваш пол";
        String chatId = update.getMessage().getChatId().toString();
        List<List<InlineKeyboardButton>> buttons = buttonsGenerator();

        return SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }

    public   List<List<InlineKeyboardButton>> buttonsGenerator(){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> lineButtons = new ArrayList<>();
        lineButtons.add(InlineKeyboardButton.builder()
                .text("Мужской")
                .callbackData("button_man")
                .build());
        lineButtons.add(InlineKeyboardButton.builder()
                .text("Женский")
                .callbackData("button_woman")
                .build());
        buttons.add(lineButtons);


        return buttons;
    }
}
