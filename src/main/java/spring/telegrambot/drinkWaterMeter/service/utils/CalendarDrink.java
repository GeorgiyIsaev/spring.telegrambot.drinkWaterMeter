package spring.telegrambot.drinkWaterMeter.service.utils;

import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class CalendarDrink {

    public static List<List<WaterDrink>> make(User user){
        if(user.getWaterDunks() == null){
             return new ArrayList<>();
        }

        List<WaterDrink> waterDrinks = user.getWaterDunks();
        List<List<WaterDrink>> days = new ArrayList<>();
        List<WaterDrink> day = new ArrayList<>();

        long timezone=3600 * 3;
        if(user.getTimeShift() != null) {
            timezone = user.getTimeShift() * 3600;
        }

        LocalDate dateTemp = LocalDate.MIN;
        for (WaterDrink waterDrink : waterDrinks){
            Instant time = waterDrink.getTime().plusSeconds(timezone);
            LocalDateTime localTime = LocalDateTime.ofInstant(time, ZoneOffset.UTC);
            LocalDate date = localTime.toLocalDate();
            if(!date.equals(dateTemp)){
                dateTemp = date;
                day = new ArrayList<>();
                days.add(day);
            }
            day.add(waterDrink);
        }
        return days;
    }
}
