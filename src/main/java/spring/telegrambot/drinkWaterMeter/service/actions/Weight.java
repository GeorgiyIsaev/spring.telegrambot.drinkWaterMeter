package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

import java.util.ArrayList;
import java.util.List;

public class Weight implements Action{
    @Override
    public SendMessage generateRequest(MessageContract messageContract) {
        String text = "Укажите ваш вес";
        String chatId = messageContract.getChatId();
        List<List<InlineKeyboardButton>> buttons = buttonsGenerator();

        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }

    public   List<List<InlineKeyboardButton>> buttonsGenerator(){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        int weight = 40;
        for(int i=0; i<6; i++){
            List<InlineKeyboardButton> lineButtons = new ArrayList<>();
            for(int j=0; j<5; j++){
                lineButtons.add(InlineKeyboardButton.builder()
                        .text(weight + " кг")
                        .callbackData("button_weight_" + weight)
                        .build());
                weight +=5;
            }
            buttons.add(lineButtons);
        }
        return buttons;
    }
}
