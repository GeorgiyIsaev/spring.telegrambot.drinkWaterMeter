package spring.telegrambot.drinkWaterMeter.repository.model.user;

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
    @Column(name = "time_shift")
    private Integer timeShift;
    @Column(name = "height")
    private Integer height;
    @Column(name = "sex")
    private Boolean sex;
//
//    @OneToMany(mappedBy = "userInfo")
//    private List<DayDrinks> calendarWaterDrunk;

    @OneToMany(mappedBy = "user")
    private List<WaterDrink> waterDunks;


    public User() {
    }

    public User(Integer id, String chatId, String username, Integer weight, Integer timeShift, Integer height, Boolean sex, List<WaterDrink> waterDunks) {
        this.id = id;
        this.chatId = chatId;
        this.username = username;
        this.weight = weight;
        this.timeShift = timeShift;
        this.height = height;
        this.sex = sex;
        this.waterDunks = waterDunks;
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

    public Integer getTimeShift() {
        return timeShift;
    }

    public void setTimeShift(Integer timeShift) {
        this.timeShift = timeShift;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public List<WaterDrink> getWaterDunks() {
        return waterDunks;
    }

    public void setWaterDunks(List<WaterDrink> waterDunks) {
        this.waterDunks = waterDunks;
    }
}
