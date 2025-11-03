package spring.telegrambot.drinkWaterMeter.data.model.user;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "water_drunk_recording")
public record WaterDrunk(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id,

        @Column(name = "fixation_time")
        LocalDateTime time,

        @Column(name = "count_water_ml")
        Integer countWaterMl,

        @ManyToOne
        @JoinColumn(name = "day_drink_id")
        WaterDrunksForDay dayDrink
       ) {
}
