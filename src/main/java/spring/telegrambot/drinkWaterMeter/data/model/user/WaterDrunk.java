package spring.telegrambot.drinkWaterMeter.data.model.user;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "water_drunk_recording")
public class WaterDrunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "fixation_time")
    private LocalDateTime time;
    @Column(name = "count_water_ml")
    private Integer countWaterMl;
    @ManyToOne
    @JoinColumn(name = "day_drink_id")
    private WaterDrunksForDay dayDrink;

    public WaterDrunk(){}

    public WaterDrunk(
            Integer id,

            LocalDateTime time,

            Integer countWaterMl,

            WaterDrunksForDay dayDrink
    ) {
        this.id = id;
        this.time = time;
        this.countWaterMl = countWaterMl;
        this.dayDrink = dayDrink;
    }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public LocalDateTime getTime() {
                return time;
        }

        public void setTime(LocalDateTime time) {
                this.time = time;
        }

        public Integer getCountWaterMl() {
                return countWaterMl;
        }

        public void setCountWaterMl(Integer countWaterMl) {
                this.countWaterMl = countWaterMl;
        }

        public WaterDrunksForDay getDayDrink() {
                return dayDrink;
        }

        public void setDayDrink(WaterDrunksForDay dayDrink) {
                this.dayDrink = dayDrink;
        }

        @Override
    public String toString() {
        return "WaterDrunk[" +
                "id=" + id + ", " +
                "time=" + time + ", " +
                "countWaterMl=" + countWaterMl + ", " +
                "dayDrink=" + dayDrink + ']';
    }

}
