package spring.telegrambot.drinkWaterMeter.repository.model.log;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "logger")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "time")
    private Instant time;
    @Column(name = "event")
    private String event;
    @Column(name = "information")
    private String information;

    public Log() {
    }

    public Log(Integer id, Instant time, String event, String information) {
        this.id = id;
        this.time = time;
        this.event = event;
        this.information = information;
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

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
