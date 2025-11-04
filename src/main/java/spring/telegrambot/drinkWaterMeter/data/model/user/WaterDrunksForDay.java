package spring.telegrambot.drinkWaterMeter.data.model.user;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "day_drunk")
public class WaterDrunksForDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "date_recordings")
    private LocalDate date;
    @OneToMany(mappedBy = "dayDrink")
    private List<WaterDrunk> waterDunks;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userInfo;

    public WaterDrunksForDay() {
    }

    public WaterDrunksForDay(
            Integer id,
            LocalDate date,
            List<WaterDrunk> waterDunks,
            User userInfo) {
        this.id = id;
        this.date = date;
        this.waterDunks = waterDunks;
        this.userInfo = userInfo;
    }

public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public LocalDate getDate() {
                return date;
        }

        public void setDate(LocalDate date) {
                this.date = date;
        }

        public List<WaterDrunk> getWaterDunks() {
                return waterDunks;
        }

        public void setWaterDunks(List<WaterDrunk> waterDunks) {
                this.waterDunks = waterDunks;
        }

        public User getUserInfo() {
                return userInfo;
        }

        public void setUserInfo(User userInfo) {
                this.userInfo = userInfo;
        }
}
