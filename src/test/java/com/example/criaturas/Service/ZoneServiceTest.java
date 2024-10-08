package com.example.criaturas.Service;

import com.example.criaturas.Entity.Zone;
import com.example.criaturas.Repository.ZoneRepository;
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

class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneService zoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada prueba
    }

    @Test
    void createZone_ShouldReturnSavedZone() {
        // Datos de ejemplo
        Zone zone = new Zone();
        zone.setName("Zona 1");
        zone.setDescription("Zona segura");
        zone.setCapacity(50);

        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);

        Zone savedZone = zoneService.createZone(zone);

        assertNotNull(savedZone);
        assertEquals("Zona 1", savedZone.getName());
        assertEquals("Zona segura", savedZone.getDescription());
        assertEquals(50, savedZone.getCapacity());
    }

    @Test
    void getAllZones_ShouldReturnListOfZones() {
        // Datos de ejemplo
        List<Zone> zoneList = new ArrayList<>();
        Zone zone1 = new Zone();
        zone1.setName("Zona 1");
        zone1.setDescription("Zona segura");
        zone1.setCapacity(50);

        Zone zone2 = new Zone();
        zone2.setName("Zona 2");
        zone2.setDescription("Zona peligrosa");
        zone2.setCapacity(30);

        zoneList.add(zone1);
        zoneList.add(zone2);

        when(zoneRepository.findAll()).thenReturn(zoneList);

        List<Zone> result = zoneService.getAllZones();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Zona 1", result.get(0).getName());
        assertEquals("Zona 2", result.get(1).getName());
    }

    @Test
    void getZoneById_ShouldReturnZone_WhenZoneExists() throws Exception {
        // Datos de ejemplo
        Zone zone = new Zone();
        zone.setName("Zona 1");
        zone.setDescription("Zona segura");
        zone.setCapacity(50);

        when(zoneRepository.findById(any(Long.class))).thenReturn(Optional.of(zone));

        Zone result = zoneService.getZoneById(1l);

        assertNotNull(result);
        assertEquals("Zona 1", result.getName());
        assertEquals("Zona segura", result.getDescription());
        assertEquals(50, result.getCapacity());
    }

    @Test
    void getZoneById_ShouldThrowException_WhenZoneNotFound() {
        when(zoneRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            zoneService.getZoneById(1L);
        });

        assertEquals("Zone not found", exception.getMessage());
    }

    @Test
    void updateZone_ShouldReturnUpdatedZone() {
        // Datos de ejemplo de la zona existente
        Zone existingZone = new Zone();
        existingZone.setName("Zona 1");
        existingZone.setDescription("Zona segura");
        existingZone.setCapacity(50);

        // Datos de la zona actualizada
        Zone updatedZone = new Zone();
        updatedZone.setName("Zona Actualizada");
        updatedZone.setDescription("Zona actualizada y segura");
        updatedZone.setCapacity(70);

        when(zoneRepository.findById(any(Long.class))).thenReturn(Optional.of(existingZone));
        when(zoneRepository.save(any(Zone.class))).thenReturn(updatedZone);

        Zone result = zoneService.updateZone(1L, updatedZone);

        assertNotNull(result);
        assertEquals("Zona Actualizada", result.getName());
        assertEquals("Zona actualizada y segura", result.getDescription());
        assertEquals(70, result.getCapacity());
    }

    @Test
    void deleteZone_ShouldDeleteZone_WhenZoneExistsAndNoCreaturesAssigned() {
        Zone zone = new Zone();
        zone.setName("Zona 1");
        zone.setCapacity(50);
        zone.setCreatures(new ArrayList<>()); // Zona sin criaturas asignadas

        when(zoneRepository.findById(any(Long.class))).thenReturn(Optional.of(zone));
        doNothing().when(zoneRepository).delete(any(Zone.class));

        zoneService.deleteZone(1L);

        verify(zoneRepository, times(1)).delete(zone);
    }

    @Test
    void deleteZone_ShouldThrowException_WhenZoneHasCreaturesAssigned() {
        Zone zone = new Zone();
        zone.setName("Zona 1");
        zone.setCapacity(50);
        // Simulamos que hay criaturas asignadas a esta zona
        zone.setCreatures(List.of(new com.example.criaturas.Entity.Creature()));

        when(zoneRepository.findById(any(Long.class))).thenReturn(Optional.of(zone));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            zoneService.deleteZone(1L);
        });

        assertEquals("Cannot delete a zone that has assigned creatures.", exception.getMessage());
    }
}
