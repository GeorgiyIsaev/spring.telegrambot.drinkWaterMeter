package spring.telegrambot.drinkWaterMeter.service.logger;

import spring.telegrambot.drinkWaterMeter.repository.model.log.Log;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PrintLog {


    public static void print(Log log) {
        String text = transform(
                dateTime(log.getTime()),
                log.getEvent(),
                log.getInformation()
        );
        System.out.println(text);
    }

    public static String FORMAT_PRINT = "%-19s %-16s %-1s";
    public static String transform(String time, String type, String information){
        return  String.format(FORMAT_PRINT, time, type, information);
    }


    public static String dateTime(Instant instant){
        int offset = 3;
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(offset));
        String data = dateTime.getYear() + "-" + dateTime.getMonthValue() + "-" + dateTime.getDayOfMonth();
        String time = dateTime.getHour() + ":" + dateTime.getMinute();
        return data + "-'" + time + "'";
    }
}
