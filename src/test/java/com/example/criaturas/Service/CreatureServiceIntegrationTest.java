package com.example.criaturas.Service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import com.example.criaturas.Entity.Creature;
import com.example.criaturas.Entity.Zone;
import com.example.criaturas.Repository.CreatureRepository;
import com.example.criaturas.Repository.ZoneRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class CreatureServiceIntegrationTest {

	@Autowired
	private CreatureService creatureService;

	@Autowired
	private CreatureRepository creatureRepository;

	@Autowired
	private ZoneRepository zoneRepository;

	@AfterEach
	public void cleanUp() {
		// Limpia los datos después de cada test
		creatureRepository.deleteAll();
		zoneRepository.deleteAll();
	}

	@Test
	@Rollback(false)
	void testCreateCreature_ShouldPersistInDatabase() {
		// Crear una nueva zona
		Zone zone = new Zone();
		zone.setName("Bosque Encantado");
		zoneRepository.save(zone);

		// Crear una nueva criatura
		Creature creature = new Creature();
		creature.setName("Unicornio");
		creature.setSpecies("Mitológica");
		creature.setSize(2.3);
		creature.setDangerLevel(5);
		creature.setHealthStatus("Saludable");
		creature.setZone(zone);

		// Guardar la criatura en la base de datos a través del servicio
		creatureService.createCreature(creature);

		// Buscar la criatura en la base de datos
		Optional<Creature> foundCreature = creatureRepository.findById(creature.getId());

		// Verificar que la criatura se haya guardado correctamente
		assertTrue(foundCreature.isPresent());
		assertEquals("Unicornio", foundCreature.get().getName());
		assertEquals("Mitológica", foundCreature.get().getSpecies());
		assertEquals(2.3, foundCreature.get().getSize());
		assertEquals(5, foundCreature.get().getDangerLevel());
		assertEquals("Saludable", foundCreature.get().getHealthStatus());
		assertEquals("Bosque Encantado", foundCreature.get().getZone().getName());
	}

	@Test
	@Rollback(false)
	void testUpdateCreature_ShouldUpdateInDatabase() {
		// Crear una nueva zona
		Zone zone = new Zone();
		zone.setName("Valle Místico");
		zoneRepository.save(zone);

		// Crear una nueva criatura
		Creature creature = new Creature();
		creature.setName("Grifo");
		creature.setSpecies("Mitológica");
		creature.setSize(3.0);
		creature.setDangerLevel(7);
		creature.setHealthStatus("Herido");
		creature.setZone(zone);
		creatureService.createCreature(creature);

		// Actualizar el nivel de peligro y el estado de salud de la criatura
		creature.setDangerLevel(10);
		creature.setHealthStatus("Recuperado");
		creatureService.updateCreature(1L, creature);

		// Verificar que la actualización se haya realizado correctamente
		Optional<Creature> updatedCreature = creatureRepository.findById(creature.getId());
		assertTrue(updatedCreature.isPresent());
		assertEquals(10, updatedCreature.get().getDangerLevel());
		assertEquals("Recuperado", updatedCreature.get().getHealthStatus());
	}

	@Test
	@Rollback(false)
	void testDeleteCreature_ShouldRemoveFromDatabase() {
		// Crear una nueva zona
		Zone zone = new Zone();
		zone.setName("Cueva del Dragón");
		zoneRepository.save(zone);

		// Crear una nueva criatura
		Creature creature = new Creature();
		creature.setName("Dragón");
		creature.setSpecies("Mitológica");
		creature.setSize(10.0);
		creature.setDangerLevel(9);
		creature.setHealthStatus("Crítico");
		creature.setZone(zone);
		creatureService.createCreature(creature);

		// Eliminar la criatura
		creatureService.deleteCreature(creature.getId());

		// Verificar que la criatura haya sido eliminada de la base de datos
		Optional<Creature> deletedCreature = creatureRepository.findById(creature.getId());
		assertFalse(deletedCreature.isPresent());
	}
}
