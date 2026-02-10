package spring.telegrambot.drinkWaterMeter.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.service.actions.*;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

import java.util.Map;
import java.util.TreeMap;

@Service
public class CommandsService {
    private final Map<String, Action> actions;
    private final UserDAO userDao;

    public CommandsService(UserDAO userDao) {
        this.userDao = userDao;
        this.actions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        fillingInActions();
    }

    private void fillingInActions() {
        addHelp();
        addHello();
        addDrink();
        addWeight();
        addDrop();
        addRecommended();
        addFull();
    }


    public void addHelp(){
        Help help = new Help();
        actions.put("/help",help);
        actions.put("хелп",help);
        actions.put("помощь",help);
    }

    public void addHello(){
        Hello hello = new Hello(userDao);
        actions.put("/hello",hello);
        actions.put("Привет",hello);
    }
    public void addRecommended(){
        Recommended recommended = new Recommended(userDao);
        actions.put("/recommended",recommended);
        actions.put("Рекомендации",recommended);
    }


    public void addDrink(){
        DrinkWater drinkWater = new DrinkWater();
        actions.put("/drink",drinkWater);
        actions.put("Выпил",drinkWater);
    }

    public void addWeight(){
        Weight weight = new Weight();
        actions.put("/weight",weight);
        actions.put("Вес",weight);
        Height height = new Height();
        actions.put("/height",height);
        actions.put("Рост",height);
        Sex sex = new Sex();
        actions.put("/sex",sex);
        actions.put("пол",sex);

        Time time = new Time();
        actions.put("/time",time);
    }

    public void addDrop(){
        Drop drop = new Drop();
        actions.put("/drop",drop);
    }
    public void addFull(){
        Full full = new Full(userDao);
        actions.put("/full",full);
    }

    public Action getAction(String command){
        if(command == null) return null;
        return actions.get(command.toLowerCase());
    }

    public SendMessage generateRequest(MessageContract messageContract){
        String command = messageContract.getText();
        Action action = getAction(command);
        if (action == null){
            String chatId = messageContract.getChatId();
            String text = "Команда "+ command + " не найдена";
            return SendMessage.builder().chatId(chatId).text(text).build();
        }
        return action.generateRequest(messageContract);
    }
}
