package spring.telegrambot.drinkWaterMeter.data.model.user;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user_info")
public record User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id,

        @Column(name = "name_user")
        String name,

        @Column(name = "weight")
        int weight,

        @OneToMany(mappedBy = "userInfo")
        List<WaterDrunksForDay> calendarWaterDrunk
){
    public String toString (){
        return "Пользователь" + name() + " Вес: " + weight();
    }
}
