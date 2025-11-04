package spring.telegrambot.drinkWaterMeter.data.model.user;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user_info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "chat_id")
    private String chatId;
    @Column(name = "username")
    private String username;
    @Column(name = "weight")
    private Integer weight;
    @OneToMany(mappedBy = "userInfo")
    private List<WaterDrunksForDay> calendarWaterDrunk;

    public User(
            Integer id,
            String chatId,
            String username,
            Integer weight,
            List<WaterDrunksForDay> calendarWaterDrunk
    ) {
        this.id = id;
        this.chatId = chatId;
        this.username = username;
        this.weight = weight;
        this.calendarWaterDrunk = calendarWaterDrunk;
    }

    public User() {
    }

    public String toString() {
        return "Пользователь " + username + " Вес: " + weight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public List<WaterDrunksForDay> getCalendarWaterDrunk() {
        return calendarWaterDrunk;
    }

    public void setCalendarWaterDrunk(List<WaterDrunksForDay> calendarWaterDrunk) {
        this.calendarWaterDrunk = calendarWaterDrunk;
    }
}
