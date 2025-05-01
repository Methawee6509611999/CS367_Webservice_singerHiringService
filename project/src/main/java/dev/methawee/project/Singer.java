package dev.methawee.project;

import jakarta.persistence.*;

@Entity
public class Singer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Default constructor
    public Singer() {}

    // Constructor for convenience
    public Singer(String name) {
        this.name = name;
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Singer{id=" + id + ", name='" + name + "'}";
    }
}
