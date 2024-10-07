package com.example.criaturas.Service;

import com.example.criaturas.Entity.Creature;
import com.example.criaturas.Entity.Zone;
import com.example.criaturas.Repository.CreatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreatureServiceTest {

    @Mock
    private CreatureRepository creatureRepository; // Usar @Mock para el repositorio

    @InjectMocks
    private CreatureService creatureService; // Inyecta los mocks en el servicio

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada prueba
    }

    @Test
    void testCreateCreature_ShouldReturnSavedCreature() {
        // Creamos la criatura de ejemplo
        Creature creature = new Creature();
        creature.setName("Fénix");
        creature.setSpecies("Ave Fénix");
        creature.setSize(5.5);
        creature.setDangerLevel(8);
        creature.setHealthStatus("healthy");

        when(creatureRepository.save(any(Creature.class))).thenReturn(creature);

        Creature savedCreature = creatureService.createCreature(creature);

        assertNotNull(savedCreature);
        assertEquals("Fénix", savedCreature.getName());
        assertEquals("Ave Fénix", savedCreature.getSpecies());
        assertEquals(5.5, savedCreature.getSize());
        assertEquals(8, savedCreature.getDangerLevel());
        assertEquals("healthy", savedCreature.getHealthStatus());
    }

    @Test
    void getAllCreatures_ShouldReturnListOfCreatures() {
        // Datos de ejemplo
        List<Creature> creatureList = new ArrayList<>();
        Creature creature1 = new Creature();
        creature1.setName("Fénix");
        creature1.setSpecies("Ave Fénix");
        creature1.setSize(5.5);
        creature1.setDangerLevel(8);
        creature1.setHealthStatus("healthy");

        Creature creature2 = new Creature();
        creature2.setName("Dragón");
        creature2.setSpecies("Dragón de fuego");
        creature2.setSize(10.0);
        creature2.setDangerLevel(10);
        creature2.setHealthStatus("critical");

        creatureList.add(creature1);
        creatureList.add(creature2);

        when(creatureRepository.findAll()).thenReturn(creatureList);

        List<Creature> result = creatureService.getAllCreatures();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Fénix", result.get(0).getName());
        assertEquals("Dragón", result.get(1).getName());
    }

    @Test
    void getCreatureById_ShouldReturnCreature_WhenCreatureExists() throws Exception {
        // Datos de ejemplo
        Creature creature = new Creature();
        creature.setName("Fénix");
        creature.setSpecies("Ave Fénix");
        creature.setSize(5.5);
        creature.setDangerLevel(8);
        creature.setHealthStatus("healthy");

        when(creatureRepository.findById(any(Long.class))).thenReturn(Optional.of(creature));

        Creature result = creatureService.getCreatureById(1L);

        assertNotNull(result);
        assertEquals("Fénix", result.getName());
        assertEquals("Ave Fénix", result.getSpecies());
        assertEquals(5.5, result.getSize());
        assertEquals(8, result.getDangerLevel());
        assertEquals("healthy", result.getHealthStatus());
    }

    @Test
    void getCreatureById_ShouldThrowException_WhenCreatureNotFound() {
        when(creatureRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            creatureService.getCreatureById(1L);
        });

        assertEquals("Creature not found", exception.getMessage());
    }

    @Test
    void updateCreature_ShouldReturnUpdatedCreature() {
        // Datos de ejemplo de criatura existente
        Creature existingCreature = new Creature();
        existingCreature.setName("Fénix");
        existingCreature.setSpecies("Ave Fénix");
        existingCreature.setSize(5.5);
        existingCreature.setDangerLevel(8);
        existingCreature.setHealthStatus("healthy");

        // Datos de la criatura actualizada
        Creature updatedCreature = new Creature();
        updatedCreature.setName("Fénix Actualizado");
        updatedCreature.setSpecies("Ave Mítica");
        updatedCreature.setSize(6.0);
        updatedCreature.setDangerLevel(9);
        updatedCreature.setHealthStatus("critical");

        when(creatureRepository.findById(any(Long.class))).thenReturn(Optional.of(existingCreature));
        when(creatureRepository.save(any(Creature.class))).thenReturn(updatedCreature);

        Creature result = creatureService.updateCreature(1L, updatedCreature);

        assertNotNull(result);
        assertEquals("Fénix Actualizado", result.getName());
        assertEquals("Ave Mítica", result.getSpecies());
        assertEquals(6.0, result.getSize());
        assertEquals(9, result.getDangerLevel());
        assertEquals("critical", result.getHealthStatus());
    }

    @Test
    void deleteCreature_ShouldDeleteCreature_WhenCreatureExistsAndNotCritical() {
        Creature creature = new Creature();
        creature.setName("Fénix");
        creature.setHealthStatus("healthy");

        when(creatureRepository.findById(any(Long.class))).thenReturn(Optional.of(creature));
        doNothing().when(creatureRepository).delete(any(Creature.class));

        creatureService.deleteCreature(1L);

        verify(creatureRepository, times(1)).delete(creature);
    }

    @Test
    void deleteCreature_ShouldThrowException_WhenCreatureHealthIsCritical() {
        Creature creature = new Creature();
        creature.setName("Fénix");
        creature.setHealthStatus("critical");

        when(creatureRepository.findById(any(Long.class))).thenReturn(Optional.of(creature));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            creatureService.deleteCreature(1L);
        });

        assertEquals("Cannot delete a creature in critical health", exception.getMessage());
    }
}
