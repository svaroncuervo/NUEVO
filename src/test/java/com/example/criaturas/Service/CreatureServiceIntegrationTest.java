package com.example.criaturas.Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import com.example.criaturas.Entity.Creature;
import java.util.Optional;
import com.example.criaturas.Repository.CreatureRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreatureServiceIntegrationTest {

	@Autowired
	private CreatureService creatureService;

	@Autowired
	private CreatureRepository creatureRepository;

	@Test
	@Rollback(false)  // Evita que se revierta la transacción para verificar que la criatura se persiste
	void testCreateCreature_ShouldPersistInDatabase() {
		// Crear una nueva criatura
		Creature creature = new Creature();
		creature.setName("Unicornio");
		creature.setDangerLevel(5);

		// Guardar la criatura en la base de datos a través del servicio
		creatureService.createCreature(creature);

		// Buscar la criatura en la base de datos
		Optional<Creature> foundCreature = creatureRepository.findById(creature.getId());

		// Verificar que la criatura se haya guardado correctamente
		assertTrue(foundCreature.isPresent());
		assertEquals("Unicornio", foundCreature.get().getName());
		assertEquals(5, foundCreature.get().getDangerLevel());
	}
}
