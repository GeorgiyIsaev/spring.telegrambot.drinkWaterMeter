package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import spring.telegrambot.drinkWaterMeter.service.update.Message;

import java.util.ArrayList;
import java.util.List;

public class Height implements Action{
    @Override
    public SendMessage generateRequest(Message message) {
        String text = "Укажите ваш рост";
        String chatId = message.getChatId();
        List<List<InlineKeyboardButton>> buttons = buttonsGenerator();

        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }

    public   List<List<InlineKeyboardButton>> buttonsGenerator(){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        int height = 80;
        for(int i=0; i<6; i++){
            List<InlineKeyboardButton> lineButtons = new ArrayList<>();
            for(int j=0; j<5; j++){
                lineButtons.add(InlineKeyboardButton.builder()
                        .text(height + " см")
                        .callbackData("button_height_" + height)
                        .build());
                height +=5;
            }
            buttons.add(lineButtons);
        }
        return buttons;
    }
}
