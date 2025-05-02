package dev.methawee.project;

import jakarta.persistence.*;

@Entity
public class Singer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int monthlyListener;

    private String name;
    private String genre;

    // Default constructor
    public Singer() {}

    // Constructor for convenience
    public Singer(String name) {
        this.name = name;
    }

    // Getters and setters
    public Long getId() { return id; }

    public int getMonthlyListener(){return monthlyListener;}

    public String getName() { return name; }

    public String getGenre() { return genre; }

    public void setMonthlyListener(int monthlyListener){this.monthlyListener = monthlyListener;}

    public void setName(String name) { this.name = name; }

    public void setGenre(String genre) { this.genre = genre; }

    @Override
    public String toString() {
        return "Singer{id=" + id + ", name='" + name + "'}";
    }
}
