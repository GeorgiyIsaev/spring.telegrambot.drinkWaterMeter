package spring.telegrambot.drinkWaterMeter.data.model.user;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "day_drunk")
public class DayDrinks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "date_recordings")
    private LocalDate date;
    @OneToMany(mappedBy = "dayDrink")
    private List<WaterDrink> waterDunks;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userInfo;

    public DayDrinks() {
    }

    public DayDrinks(
            Integer id,
            LocalDate date,
            List<WaterDrink> waterDunks,
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

        public List<WaterDrink> getWaterDunks() {
                return waterDunks;
        }

        public void setWaterDunks(List<WaterDrink> waterDunks) {
                this.waterDunks = waterDunks;
        }

        public User getUserInfo() {
                return userInfo;
        }

        public void setUserInfo(User userInfo) {
                this.userInfo = userInfo;
        }
}
