package spring.telegrambot.drinkWaterMeter.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;
import spring.telegrambot.drinkWaterMeter.service.actions.Help;
import spring.telegrambot.drinkWaterMeter.service.actions.Return;

import java.util.Map;
import java.util.TreeMap;

@Service
public class ActionsService {
    private final Map<String, Action> actions;

    public ActionsService() {
        this.actions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        fillingInActions();
    }

    private void fillingInActions() {
        addButtonCreateCommand();
        addHelp();
    }



    public void addButtonCreateCommand(){
        Return returnAction = new Return();
        actions.put("ретюрн",returnAction);
        actions.put("возврат",returnAction);
        actions.put("/return",returnAction);
    }



    public void addHelp(){
        Help help = new Help();
        actions.put("/help",help);
        actions.put("хелп",help);
        actions.put("помощь",help);
    }

    public Action getAction(String command){
        if(command == null) return null;
        return actions.get(command.toLowerCase());
    }

    public SendMessage generateRequest(String command, String chatID){
        Action action = getAction(command);
        if (action == null){
            String text = "Команда "+ command + " не найдена";
            return SendMessage.builder().chatId(chatID).text(text).build();
        }
        return action.generateRequest(chatID);
    }
}
