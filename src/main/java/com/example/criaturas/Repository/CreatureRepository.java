package com.example.criaturas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.criaturas.Entity.Creature;

public interface CreatureRepository extends JpaRepository<Creature, Long>{
    //root user password: password
}
