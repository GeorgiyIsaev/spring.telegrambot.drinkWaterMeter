package spring.telegrambot.drinkWaterMeter.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.service.actions.*;
import spring.telegrambot.drinkWaterMeter.service.buttons.*;
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
        addButtonsWeight();
        addButtonsDrop();
        addSexChange();
    }

    public void addButtonsDrinkWater() {
        for(int ml = 200; ml <= 600; ml+=50 ) {
            Action drinkWaterButton = new DrinkWaterButton(userService, ml);
            actions.put("button_index_" + ml+"ml", drinkWaterButton);
        }
    }

    public void addButtonsWeight() {
        for(int kg = 40; kg <= 180; kg+=5 ) {
            Action weightButton = new WeightButton(userService, kg);
            actions.put("button_weight_" + kg, weightButton);
        }
        for(int cm = 90; cm <= 250; cm+=5 ) {
            Action heightButton = new HeightButton(userService,  cm);
            actions.put("button_height_" + cm, heightButton);
        }
    }

    public void addButtonsDrop() {
        Action dropButtonNo = new DropButtonNo();
        actions.put("button_drop_no", dropButtonNo);
        Action dropButtonYes = new DropButtonYes(userService);
        actions.put("button_drop_yes", dropButtonYes);
    }

    public void addSexChange() {
        SexButton sexButtonWoman = new SexButton(userService, true);
        actions.put("button_woman", sexButtonWoman);
        SexButton sexButtonMan = new SexButton(userService, false);
        actions.put("button_man", sexButtonMan);
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
