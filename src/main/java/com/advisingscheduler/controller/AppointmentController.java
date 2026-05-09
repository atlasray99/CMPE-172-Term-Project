package com.advisingscheduler.controller;

import com.advisingscheduler.model.Appointment;
import com.advisingscheduler.model.AvailabilitySlot;
import com.advisingscheduler.service.AppointmentService;
import com.advisingscheduler.service.AvailabilityService;
import com.advisingscheduler.service.NotificationClient;
import com.advisingscheduler.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    private final AppointmentService appointmentService;
    private final AvailabilityService availabilityService;
    private final ServiceService serviceService;
    private final NotificationClient notificationClient;

    public AppointmentController(AppointmentService appointmentService,
                                 AvailabilityService availabilityService,
                                 ServiceService serviceService,
                                 NotificationClient notificationClient) {
        this.appointmentService = appointmentService;
        this.availabilityService = availabilityService;
        this.serviceService = serviceService;
        this.notificationClient = notificationClient;
    }

    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        logger.debug("GET /appointments — loading appointment list");
        List<Appointment> appointments = appointmentService.getAllAppointments();
        logger.info("GET /appointments — displaying {} appointment(s)", appointments.size());
        model.addAttribute("appointments", appointments);
        return "appointments";
    }

    @GetMapping("/book")
    public String showBookingForm(@RequestParam(required = false) Integer slotId, Model model) {
        logger.debug("GET /book — showing booking form (preselected slotId={})", slotId);
        List<AvailabilitySlot> availableSlots = availabilityService.getAvailableSlots();
        List<com.advisingscheduler.model.Service> services = serviceService.getAllServices();

        model.addAttribute("slots", availableSlots);
        model.addAttribute("services", services);
        model.addAttribute("selectedSlotId", slotId);
        return "book";
    }

    @PostMapping("/book")
    public String processBooking(@RequestParam int slotId,
                                 @RequestParam int serviceId,
                                 @RequestParam(defaultValue = "2") int clientId,
                                 @RequestParam(required = false) String notes,
                                 Model model) {
        logger.info("POST /book — slotId={} serviceId={} clientId={}", slotId, serviceId, clientId);
        try {
            int appointmentId = appointmentService.bookAppointment(clientId, slotId, serviceId, notes);
            logger.info("POST /book — booking succeeded, appointmentId={}", appointmentId);

            Optional<Appointment> appt = appointmentService.getAppointmentById(appointmentId);
            if (appt.isPresent()) {
                Appointment a = appt.get();
                DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
                DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");

                notificationClient.sendBookingConfirmation(
                        a.getAppointmentId(),
                        "client@example.com",
                        a.getClientName(),
                        a.getServiceName(),
                        a.getSlotStartTime().format(dateFmt),
                        a.getSlotStartTime().format(timeFmt) + " - " + a.getSlotEndTime().format(timeFmt)
                );
            }

            return "redirect:/confirmation?id=" + appointmentId;
        } catch (IllegalStateException e) {
            logger.warn("POST /book — booking failed for slotId={}: {}", slotId, e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("slots", availabilityService.getAvailableSlots());
            model.addAttribute("services", serviceService.getAllServices());
            return "book";
        }
    }

    @GetMapping("/confirmation")
    public String showConfirmation(@RequestParam int id, Model model) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        if (appointment.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("appointment", appointment.get());
        return "confirmation";
    }

    @PostMapping("/cancel")
    public String cancelAppointment(@RequestParam int appointmentId) {
        logger.info("POST /cancel — appointmentId={}", appointmentId);
        try {
            appointmentService.cancelAppointment(appointmentId);
            logger.info("POST /cancel — appointment {} cancelled successfully", appointmentId);
        } catch (IllegalStateException | IllegalArgumentException e) {
            logger.warn("POST /cancel — could not cancel appointment {}: {}", appointmentId, e.getMessage());
        }
        return "redirect:/appointments";
    }
}
