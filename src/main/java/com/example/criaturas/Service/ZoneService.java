package com.example.criaturas.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.criaturas.Entity.Zone;
import com.example.criaturas.Repository.ZoneRepository;

import java.util.List;

@Service
public class ZoneService {
    private final ZoneRepository zoneRepository;

    @Autowired
    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public Zone createZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    public Zone getZoneById(Long id) throws Exception {
        return zoneRepository.findById(id).orElseThrow(() -> new Exception("Zone not found"));
    }

    public Zone updateZone(Long id, Zone updatedZone) {
        Zone zone = zoneRepository.findById(id).orElse(null);
        if (zone != null) {
            zone.setName(updatedZone.getName());
            zone.setDescription(updatedZone.getDescription());
            zone.setCapacity(updatedZone.getCapacity());
            return zoneRepository.save(zone);
        }
        return null;
    }

    public void deleteZone(Long id) {
        Zone zone = zoneRepository.findById(id).orElse(null);
        if (zone != null && zone.getCreatures().isEmpty()) {
            zoneRepository.delete(zone);
        } else {
            throw new IllegalStateException("Cannot delete a zone that has assigned creatures.");
        }
    }
}

