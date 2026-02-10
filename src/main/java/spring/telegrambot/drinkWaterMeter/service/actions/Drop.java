package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

import java.util.ArrayList;
import java.util.List;

public class Drop implements Action{
    @Override
    public SendMessage generateRequest(MessageContract messageContract) {
        String text = "Вы действительно хотите удалить все данные о себе?";
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
        List<InlineKeyboardButton> lineButtons = new ArrayList<>();
        lineButtons.add(InlineKeyboardButton.builder()
                .text("Да удалить!")
                .callbackData("button_drop_yes")
                .build());
        lineButtons.add(InlineKeyboardButton.builder()
                .text("Нет не удалять!")
                .callbackData("button_drop_no")
                .build());
        buttons.add(lineButtons);

        return buttons;
    }
}
