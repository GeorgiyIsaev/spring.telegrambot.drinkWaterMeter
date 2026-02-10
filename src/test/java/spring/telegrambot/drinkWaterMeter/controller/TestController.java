package spring.telegrambot.drinkWaterMeter.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.test.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.objects.Update;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestController {

    @Autowired
    TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/test002");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "0000");
    }


    @Test
    public void test0(){

    }

    @Test
    public void test01(){
        Update update = new Update();
        ResponseEntity<Void> response =
            restTemplate.postForEntity(
                    "/v1/api/telegram",
                    update,
                    Void.class
            );
    }

    @Test
    public void test02(){
        Update update = new Update();
        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "/v1/api/telegram",
                        update,
                        String.class
                );
    }







}
