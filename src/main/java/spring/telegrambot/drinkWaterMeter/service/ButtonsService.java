package spring.telegrambot.drinkWaterMeter.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.service.actions.*;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

import java.util.Map;
import java.util.TreeMap;

@Service
public class ButtonsService {
    private final Map<String, Action> actions;
    private final UserService userService;

    public ButtonsService(UserService userService) {
        this.userService = userService;
        this.actions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        fillingInActions();
    }

    private void fillingInActions() {
        addButtonsDrinkWater();
    }

    public void addButtonsDrinkWater() {
        for(int ml = 200; ml <= 600; ml+=50 ) {
            Action drinkWaterButton = new DrinkWaterButton(userService, ml);
            actions.put("button_index_" + ml+"ml", drinkWaterButton);
        }
    }




    public Action getAction(String command){
        if(command == null) return null;
        return actions.get(command.toLowerCase());
    }

    public SendMessage generateRequest(Update update){
        String dataButton = update.getCallbackQuery().getData();
        Action action = getAction(dataButton);
        if (action == null){
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String text = "Кнопка "+ dataButton + " не реализованна!";
            return SendMessage.builder().chatId(chatId).text(text).build();
        }
        return action.generateRequest(update);
    }
}
