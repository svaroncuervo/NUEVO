package com.example.criaturas.Service;

import com.example.criaturas.Entity.Zone;
import com.example.criaturas.Repository.ZoneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ZoneServiceIntegrationTest {

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private ZoneRepository zoneRepository;

    @AfterEach
    public void cleanUp() {
        // Limpiar la base de datos después de cada test
        zoneRepository.deleteAll();
    }

    @Test
    @Rollback(false)
    void testCreateZone_ShouldPersistInDatabase() {
        // Crear una nueva zona
        Zone zone = new Zone();
        zone.setName("Selva Mágica");
        zone.setDescription("Una zona llena de magia y criaturas desconocidas");
        zone.setCapacity(100);

        // Guardar la zona en la base de datos a través del servicio
        zoneService.createZone(zone);

        // Buscar la zona en la base de datos
        Optional<Zone> foundZone = zoneRepository.findById(zone.getId());

        // Verificar que la zona se haya guardado correctamente
        assertTrue(foundZone.isPresent());
        assertEquals("Selva Mágica", foundZone.get().getName());
        assertEquals("Una zona llena de magia y criaturas desconocidas", foundZone.get().getDescription());
        assertEquals(100, foundZone.get().getCapacity());
    }

    @Test
    @Rollback(false)
    void testUpdateZone_ShouldUpdateInDatabase() {
        // Crear y guardar una nueva zona
        Zone zone = new Zone();
        zone.setName("Lago Misterioso");
        zone.setDescription("Zona tranquila alrededor de un lago");
        zone.setCapacity(50);
        zoneService.createZone(zone);

        // Actualizar la descripción y capacidad de la zona
        zone.setDescription("Zona tranquila pero con misterios ocultos");
        zone.setCapacity(60);
        zoneService.updateZone(zone.getId(), zone);

        // Verificar que la actualización se haya realizado correctamente
        Optional<Zone> updatedZone = zoneRepository.findById(zone.getId());
        assertTrue(updatedZone.isPresent());
        assertEquals("Zona tranquila pero con misterios ocultos", updatedZone.get().getDescription());
        assertEquals(60, updatedZone.get().getCapacity());
    }

    @Test
    @Rollback(false)
    void testDeleteZone_ShouldRemoveFromDatabase() {
        // Crear y guardar una nueva zona
        Zone zone = new Zone();
        zone.setName("Volcán Ardiente");
        zone.setDescription("Zona peligrosa cercana a un volcán activo");
        zone.setCapacity(20);
        zoneService.createZone(zone);

        // Eliminar la zona (si no tiene criaturas asignadas)
        zoneService.deleteZone(zone.getId());

        // Verificar que la zona haya sido eliminada de la base de datos
        Optional<Zone> deletedZone = zoneRepository.findById(zone.getId());
        assertFalse(deletedZone.isPresent());
    }
}
