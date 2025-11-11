package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.service.update.Message;
import spring.telegrambot.drinkWaterMeter.service.utils.CalculatingDrinkingNorms;
import spring.telegrambot.drinkWaterMeter.service.utils.CalendarDrink;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

public class Recommended  implements Action{

    private final UserDAO userDao;
    public Recommended(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public SendMessage generateRequest(Message message) {
        String chatId = message.getChatId();
        String username = message.getUsername();
        User user = userDao.findOrCreate(chatId, username);

        String text = hider() + infoUser(user) + "\n\n" + drunkAllToday(user);

        return SendMessage.builder().chatId(chatId).text(text).build();
    }


    public String hider(){
        return "    Рекомендации:\n" +   "Эксперты ВОЗ отмечают, что нормы ежедневного потребления воды не существует. Количество выпитой жидкости за сутки может составлять от одного до пяти литров воды. Учитывается общий объем жидкости: вода, чай, кофе, компот, супы, соки (и фрукты). Чистой питьевой воды же рекомендуется употреблять 50-70% от общего объема всей жидкости. При физических нагрузках рекомендуется пить на 300-500мл больше обычного.\n";
    }

    public String infoUser(User user){
        Integer height = user.getHeight();
        Integer weight = user.getWeight();
        Boolean sex = user.getSex();
        if(weight==null || height==null){
            return "\nВы не указали вес и рост. Среднее рекомендуемое значение воды для человек составляет 2.5 л в сутки!";
        }
        int recommendedWeight = CalculatingDrinkingNorms.of().recommendedWeight(height, sex);

        return "\n" + recommendedWeight(height, sex) + massEstimate(recommendedWeight, weight);
    }

    public String recommendedWeight(Integer height,  Boolean sex){
        if(height==null){
            return "";
        }
        int recommendedWeight = CalculatingDrinkingNorms.of().recommendedWeight(height, sex);
        return "При росте " + height + " см рекомендуемой (идеальной) массы тела считается " + recommendedWeight + " кг. ";
    }

    public String massEstimate(int recommendedWeight,   Integer weight){
        if(weight==null){
            return waterDay(2500);
        }
        int percent = (int) calculatePercentageDifference(weight,recommendedWeight);
        if(percent < 20){
            return "Ваш вес в "+ weight+" кг находится в идеальном диапазоне до " +  percent + " %. " +
                    waterDay(CalculatingDrinkingNorms.of().drinkingNorms(recommendedWeight));
        }

        if(recommendedWeight < weight) {
            return "Ваш вес в "+weight+" кг превышает рекомендуемому норму, рекомендуется пить больше воды. "+
                    waterDay(CalculatingDrinkingNorms.of().drinkingOverweight(recommendedWeight));
        }
        else {
            return "Ваш вес в "+weight+" меньше рекомендуемой (вам нужно больше есть). "+
                    waterDay(CalculatingDrinkingNorms.of().drinkingNorms(recommendedWeight));
        }
    }

    public String waterDay(int ml){
        int litre = ml/1000;
        int mlilitre = (ml - litre * 1000) / 100;

        return "Рекомендуемое количестве воды в сутки  " + litre +"." + mlilitre + " литров! ";
    }


     public double calculatePercentageDifference(int weightE, int weightR) {
        double average = (weightE + weightR) / 2.0;
        if (average == 0) {
            throw new IllegalArgumentException("Negative value.");
        }
        return Math.abs((weightE - weightR) / average) * 100;
    }

    public String drunkAllToday(User user){
        List<List<WaterDrink>> days = CalendarDrink.make(user);
        if(days.isEmpty()){
            return "";
        }

        int countAllDrink = 0;
        for (WaterDrink drink : days.getLast()){
            countAllDrink +=drink.getCountWaterMl();
        }
        return "Всего за день выпито: " + countAllDrink + " мл воды.";
    }
}
