package dev.methawee.project;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    private String location;

    @ManyToOne
    @JoinColumn(name = "singer_id")
    private Singer singer;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Singer getSinger() { return singer; }
    public void setSinger(Singer singer) { this.singer = singer; }
}
