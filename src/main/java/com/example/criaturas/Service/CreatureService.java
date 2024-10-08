package com.example.criaturas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.criaturas.Entity.Creature;
import com.example.criaturas.Repository.CreatureRepository;

@Service
public class CreatureService{
    private final CreatureRepository creatureRepository;

    @Autowired
    public CreatureService(CreatureRepository creatureRepository) {
        this.creatureRepository = creatureRepository;
    }

    public Creature createCreature(Creature creature) {
        System.out.println(creature.getDangerLevel());
        return creatureRepository.save(creature);
    }
    public List<Creature> getAllCreatures() {
        return creatureRepository.findAll();
    }

    public Creature getCreatureById(Long id) throws Exception{
        return creatureRepository.findById(id).orElseThrow(() -> new Exception("Creature not found"));
    }

    public Creature updateCreature(Long id, Creature updatedCreature){
        Creature creature = this.creatureRepository.findById(id).orElse(null);
        creature.setName(updatedCreature.getName());
        creature.setSpecies(updatedCreature.getSpecies());
        creature.setSize(updatedCreature.getSize());
        creature.setDangerLevel(updatedCreature.getDangerLevel());
        creature.setHealthStatus(updatedCreature.getHealthStatus());
        return creatureRepository.save(creature);
    }

    public void deleteCreature(Long id) {
        Creature creature = this.creatureRepository.findById(id).orElse(null);
        if (!"critical".equals(creature.getHealthStatus())) {
            creatureRepository.delete(creature);
        } else {
            throw new IllegalStateException("Cannot delete a creature in critical health");
        }
    }

}
