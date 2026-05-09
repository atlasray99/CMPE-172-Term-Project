package com.advisingscheduler.service;

import com.advisingscheduler.model.AvailabilitySlot;
import com.advisingscheduler.repository.AdvisorRepository;
import com.advisingscheduler.repository.AvailabilitySlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AvailabilityService {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilityService.class);

    private final AvailabilitySlotRepository slotRepository;
    private final AdvisorRepository advisorRepository;

    public AvailabilityService(AvailabilitySlotRepository slotRepository,
                               AdvisorRepository advisorRepository) {
        this.slotRepository = slotRepository;
        this.advisorRepository = advisorRepository;
    }

    public List<AvailabilitySlot> getAvailableSlots() {
        logger.info("Fetching all available (unbooked) slots");
        return slotRepository.findAvailableSlots();
    }

    public Optional<AvailabilitySlot> getSlotById(int slotId) {
        return slotRepository.findById(slotId);
    }

    // Returns all slots for one specific advisor (used on /manage-slots)
    public List<AvailabilitySlot> getSlotsByAdvisor(int advisorId) {
        logger.info("Fetching all slots for advisorId={}", advisorId);
        return slotRepository.findAllByAdvisor(advisorId);
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

    // Validates advisor credentials and returns advisorId + full name on success
    public Optional<Map<String, Object>> login(String username, String password) {
        logger.info("Advisor login attempt for username={}", username);
        Optional<Map<String, Object>> result = advisorRepository.authenticate(username, password);
        if (result.isPresent()) {
            logger.info("Login successful for username={}", username);
        } else {
            logger.warn("Login failed for username={}", username);
        }
        return result;
    }
}
