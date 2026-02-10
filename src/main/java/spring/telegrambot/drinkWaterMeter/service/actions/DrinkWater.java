package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

import java.util.ArrayList;
import java.util.List;

public class DrinkWater implements Action{
    @Override
    public SendMessage generateRequest(MessageContract messageContract) {
        String text = "Сколько воды вы выпили?";
        text += "(Обычная чайная кружка вмещает 300мл)";
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

        List<InlineKeyboardButton> lineButtons1 = new ArrayList<>();

        lineButtons1.add(InlineKeyboardButton.builder()
                .text("200_мл")
                .callbackData("button_index_" + "200ml")
                .build());
        lineButtons1.add(InlineKeyboardButton.builder()
                .text("300_мл")
                .callbackData("button_index_" + "300ml")
                .build());
        lineButtons1.add(InlineKeyboardButton.builder()
                .text("400_мл")
                .callbackData("button_index_" + "400ml")
                .build());
        buttons.add(lineButtons1);

        List<InlineKeyboardButton> lineButtons2 = new ArrayList<>();

        lineButtons2.add(InlineKeyboardButton.builder()
                .text("250 мл")
                .callbackData("button_index_" + "250ml")
                .build());
        lineButtons2.add(InlineKeyboardButton.builder()
                .text("350 мл")
                .callbackData("button_index_" + "350ml")
                .build());
        lineButtons2.add(InlineKeyboardButton.builder()
                .text("450 мл")
                .callbackData("button_index_" + "450ml")
                .build());
        buttons.add(lineButtons2);
        return buttons;
    }
}
