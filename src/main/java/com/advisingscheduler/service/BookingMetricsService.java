package com.advisingscheduler.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

/**
 * Centralises all Micrometer metrics for the booking workflow.
 *
 * Exposes three counters and one timer:
 *   bookings.completed  — incremented each time a slot is successfully booked
 *   bookings.conflicts  — incremented when optimistic locking detects a double-book attempt
 *   bookings.cancelled  — incremented each time an appointment is cancelled
 *   bookings.duration   — records how long the full bookAppointment() call takes
 *
 * All metrics are visible at GET /actuator/metrics/<name>.
 */
@Service
public class BookingMetricsService {

    private final Counter bookingsCompleted;
    private final Counter bookingConflicts;
    private final Counter bookingsCancelled;
    private final Timer   bookingDuration;

    public BookingMetricsService(MeterRegistry registry) {
        this.bookingsCompleted = Counter.builder("bookings.completed")
                .description("Number of appointments successfully booked")
                .register(registry);

        this.bookingConflicts = Counter.builder("bookings.conflicts")
                .description("Number of optimistic-lock conflicts detected during booking")
                .register(registry);

        this.bookingsCancelled = Counter.builder("bookings.cancelled")
                .description("Number of appointments cancelled")
                .register(registry);

        this.bookingDuration = Timer.builder("bookings.duration")
                .description("Time taken to complete the full booking transaction")
                .register(registry);
    }

    public void recordBookingCompleted()  { bookingsCompleted.increment(); }
    public void recordBookingConflict()   { bookingConflicts.increment(); }
    public void recordBookingCancelled()  { bookingsCancelled.increment(); }

    /** Wrap a booking attempt so its wall-clock duration is recorded. */
    public <T> T timeBooking(java.util.concurrent.Callable<T> action) throws Exception {
        return bookingDuration.recordCallable(action);
    }
}
