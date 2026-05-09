package com.advisingscheduler.controller;

import com.advisingscheduler.model.AvailabilitySlot;
import com.advisingscheduler.service.AvailabilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AvailabilityController {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilityController.class);

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // ── Client view: available slots only ────────────────────────────────────
    @GetMapping("/slots")
    public String viewAvailableSlots(Model model) {
        logger.debug("GET /slots — fetching available slots for display");
        List<AvailabilitySlot> slots = availabilityService.getAvailableSlots();
        logger.info("GET /slots — returned {} available slot(s)", slots.size());
        model.addAttribute("slots", slots);
        return "slots";
    }

    // ── Provider view: all slots + add/delete management ─────────────────────
    @GetMapping("/manage-slots")
    public String manageSlots(@RequestParam(required = false) String success,
                              @RequestParam(required = false) String error,
                              Model model) {
        logger.debug("GET /manage-slots — loading advisor slot management page");
        List<AvailabilitySlot> allSlots = availabilityService.getAllSlots();
        logger.info("GET /manage-slots — {} total slot(s)", allSlots.size());
        model.addAttribute("slots", allSlots);
        if (success != null) model.addAttribute("success", success);
        if (error   != null) model.addAttribute("error",   error);
        return "manage-slots";
    }

    @PostMapping("/manage-slots/add")
    public String addSlot(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            Model model) {
        logger.info("POST /manage-slots/add — startTime={} endTime={}", startTime, endTime);
        try {
            if (!endTime.isAfter(startTime)) {
                throw new IllegalArgumentException("End time must be after start time.");
            }
            availabilityService.addSlot(1, startTime, endTime); // advisor_id=1 (seeded advisor)
            return "redirect:/manage-slots?success=Slot+added+successfully";
        } catch (Exception e) {
            logger.warn("Failed to add slot: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("slots", availabilityService.getAllSlots());
            return "manage-slots";
        }
    }

    @PostMapping("/manage-slots/delete")
    public String deleteSlot(@RequestParam int slotId) {
        logger.info("POST /manage-slots/delete — slotId={}", slotId);
        try {
            availabilityService.deleteSlot(slotId);
            return "redirect:/manage-slots?success=Slot+deleted";
        } catch (IllegalStateException e) {
            logger.warn("Could not delete slot {}: {}", slotId, e.getMessage());
            return "redirect:/manage-slots?error=" + e.getMessage().replace(" ", "+");
        }
    }
}
