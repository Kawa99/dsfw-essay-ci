package com.team_proj.dsfw_team_proj.manager;
import jakarta.persistence.*;

@Entity
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int results;

    // Constructors
    public Manager() {}
    public Manager(String name, int results) {
        this.name = name;
        this.results = results;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getResults() { return results; }
    public void setResults(int results) { this.results = results; }
}
