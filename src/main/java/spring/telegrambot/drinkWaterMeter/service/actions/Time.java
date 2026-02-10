package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

import java.util.ArrayList;
import java.util.List;

public class Time implements Action{
    @Override
    public SendMessage generateRequest(MessageContract messageContract) {
        String text = "Укажите ваш часовой пояс";
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

        int timeNegative = -12;
        int timePositive = 14;
        while (timeNegative <= timePositive) {
                List<InlineKeyboardButton> lineButtons = new ArrayList<>();
                for (int j = 0; j < 7; j++) {
                    String time = timeNegative>=0 ? "+" : "";
                    time += timeNegative;

                    lineButtons.add(InlineKeyboardButton.builder()
                            .text(time)
                            .callbackData("button_time_" + timeNegative)
                            .build());
                    timeNegative++;
                    if(timeNegative > timePositive){
                        break;
                    }
                }
                buttons.add(lineButtons);
        }
        return buttons;
    }
}
