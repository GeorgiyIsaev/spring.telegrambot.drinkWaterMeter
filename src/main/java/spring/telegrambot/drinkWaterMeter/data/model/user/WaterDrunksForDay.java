package spring.telegrambot.drinkWaterMeter.data.model.user;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "day_drunk")
public record WaterDrunksForDay(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id,

        @Column(name = "date_recordings")
        LocalDate date,

        @OneToMany(mappedBy = "dayDrink")
        List<WaterDrunk> waterDunks,

        @ManyToOne
        @JoinColumn(name = "user_id")
        User userInfo) {
}
