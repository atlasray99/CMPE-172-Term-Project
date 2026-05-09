package com.advisingscheduler.service;

import com.advisingscheduler.model.AvailabilitySlot;
import com.advisingscheduler.repository.AvailabilitySlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityService {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilityService.class);

    private final AvailabilitySlotRepository slotRepository;

    public AvailabilityService(AvailabilitySlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public List<AvailabilitySlot> getAvailableSlots() {
        logger.info("Fetching all available (unbooked) slots");
        return slotRepository.findAvailableSlots();
    }

    public Optional<AvailabilitySlot> getSlotById(int slotId) {
        return slotRepository.findById(slotId);
    }

    public List<AvailabilitySlot> getAllSlots() {
        logger.info("Fetching all slots for advisor management");
        return slotRepository.findAll();
    }

    public void addSlot(int advisorId, LocalDateTime startTime, LocalDateTime endTime) {
        logger.info("Adding new slot: advisorId={} from {} to {}", advisorId, startTime, endTime);
        slotRepository.insert(advisorId, startTime, endTime);
    }

    public void deleteSlot(int slotId) {
        int rows = slotRepository.deleteById(slotId);
        if (rows == 0) {
            logger.warn("Could not delete slot {} — it may be booked or not found", slotId);
            throw new IllegalStateException("Cannot delete a booked slot.");
        }
        logger.info("Deleted slot {}", slotId);
    }
}
