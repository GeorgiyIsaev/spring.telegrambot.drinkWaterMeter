package spring.telegrambot.drinkWaterMeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DrinkWaterMeterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrinkWaterMeterApplication.class, args);
	}

}
