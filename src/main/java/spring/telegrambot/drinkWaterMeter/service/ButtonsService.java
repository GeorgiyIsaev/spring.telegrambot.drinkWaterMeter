package spring.telegrambot.drinkWaterMeter.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.service.actions.*;
import spring.telegrambot.drinkWaterMeter.service.buttons.*;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQuery;

import java.util.Map;
import java.util.TreeMap;

@Service
public class ButtonsService {
    private final Map<String, Button> button;
    private final UserDAO userDao;

    public ButtonsService(UserDAO userDao) {
        this.userDao = userDao;
        this.button = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        fillingInActions();
    }

    private void fillingInActions() {
        addButtonsDrinkWater();
        addButtonsWeight();
        addButtonsDrop();
        addSexChange();
    }

    public void addButtonsDrinkWater() {
        for(int ml = 200; ml <= 600; ml+=50 ) {
            Button drinkWaterButton = new DrinkWaterButton(userDao, ml);
            button.put("button_index_" + ml+"ml", drinkWaterButton);
        }
    }

    public void addButtonsWeight() {
        for(int kg = 40; kg <= 190; kg+=5 ) {
            Button weightButton = new WeightButton(userDao, kg);
            button.put("button_weight_" + kg, weightButton);
        }
        for(int cm = 80; cm <= 250; cm+=5 ) {
            Button heightButton = new HeightButton(userDao,  cm);
            button.put("button_height_" + cm, heightButton);
        }
    }

    public void addButtonsDrop() {
        Button dropButtonNo = new DropButtonNo();
        button.put("button_drop_no", dropButtonNo);
        Button dropButtonYes = new DropButtonYes(userDao);
        button.put("button_drop_yes", dropButtonYes);
    }

    public void addSexChange() {
        Button sexButtonWoman = new SexButton(userDao, true);
        button.put("button_woman", sexButtonWoman);
        Button sexButtonMan = new SexButton(userDao, false);
        button.put("button_man", sexButtonMan);
    }




    public Button getAction(String command){
        if(command == null) return null;
        return button.get(command.toLowerCase());
    }

    public SendMessage generateRequest(CallbackQuery callbackQuery){
        String dataButton = callbackQuery.getData();
        Button action = getAction(dataButton);
        if (action == null){
            String chatId = callbackQuery.getChatId();
            String text = "Кнопка "+ dataButton + " не реализованна!";
            return SendMessage.builder().chatId(chatId).text(text).build();
        }
        return action.generateRequest(callbackQuery);
    }
}
