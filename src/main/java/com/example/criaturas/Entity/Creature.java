package com.example.criaturas.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Creature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private double size;
    private int dangerLevel;
    private String healthStatus;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;




    public Creature(String name, String species, double size, int dangerLevel, String healthStatus, Zone zone) {
        this.name = name;
        this.species = species;
        this.size = size;
        this.dangerLevel = dangerLevel;
        this.healthStatus = healthStatus;
        this.zone = zone;
    }

    
}
