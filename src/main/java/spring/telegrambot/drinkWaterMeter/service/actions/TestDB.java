package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class TestDB implements Action{
        @Override
        public SendMessage generateRequest(String chatId) {



            String text ="ТEST" ;
            return SendMessage.builder().chatId(chatId).text(text).build();
        }
}
