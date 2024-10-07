package com.example.criaturas.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.criaturas.Entity.Zone;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

}

