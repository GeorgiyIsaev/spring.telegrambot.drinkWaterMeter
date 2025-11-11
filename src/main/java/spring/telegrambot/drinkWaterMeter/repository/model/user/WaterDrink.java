package spring.telegrambot.drinkWaterMeter.repository.model.user;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;


@Entity
@Table(name = "water_drink_recording")
public class WaterDrink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "fixation_time")
    private Instant time;
    @Column(name = "count_water_ml")
    private Integer countWaterMl;
//    @ManyToOne
//    @JoinColumn(name = "day_drink_id")
//    private DayDrinks dayDrink;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public WaterDrink(){}

    public WaterDrink(Integer id, Instant time, Integer countWaterMl, User user) {
        this.id = id;
        this.time = time;
        this.countWaterMl = countWaterMl;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Integer getCountWaterMl() {
        return countWaterMl;
    }

    public void setCountWaterMl(Integer countWaterMl) {
        this.countWaterMl = countWaterMl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
