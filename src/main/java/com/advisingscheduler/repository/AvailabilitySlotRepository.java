package com.advisingscheduler.repository;

import com.advisingscheduler.model.AvailabilitySlot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AvailabilitySlotRepository {

    private final JdbcTemplate jdbcTemplate;

    public AvailabilitySlotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<AvailabilitySlot> rowMapper = (ResultSet rs, int rowNum) -> {
        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setSlotId(rs.getInt("slot_id"));
        slot.setAdvisorId(rs.getInt("advisor_id"));
        slot.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        slot.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        slot.setBooked(rs.getBoolean("is_booked"));
        slot.setVersion(rs.getInt("version"));
        return slot;
    };

    public List<AvailabilitySlot> findAvailableSlots() {
        String sql = "SELECT * FROM availability_slots WHERE is_booked = FALSE ORDER BY start_time";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<AvailabilitySlot> findById(int slotId) {
        String sql = "SELECT * FROM availability_slots WHERE slot_id = ?";
        List<AvailabilitySlot> results = jdbcTemplate.query(sql, rowMapper, slotId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public int markAsBooked(int slotId, int expectedVersion) {
        String sql = "UPDATE availability_slots SET is_booked = TRUE, version = version + 1 "
                   + "WHERE slot_id = ? AND is_booked = FALSE AND version = ?";
        return jdbcTemplate.update(sql, slotId, expectedVersion);
    }

    public int markAsAvailable(int slotId) {
        String sql = "UPDATE availability_slots SET is_booked = FALSE, version = version + 1 "
                   + "WHERE slot_id = ? AND is_booked = TRUE";
        return jdbcTemplate.update(sql, slotId);
    }

    public List<AvailabilitySlot> findAll() {
        String sql = "SELECT * FROM availability_slots ORDER BY start_time";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void insert(int advisorId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        String sql = "INSERT INTO availability_slots (advisor_id, start_time, end_time) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, advisorId, startTime, endTime);
    }

    public int deleteById(int slotId) {
        // Only allow deletion of unbooked slots
        String sql = "DELETE FROM availability_slots WHERE slot_id = ? AND is_booked = FALSE";
        return jdbcTemplate.update(sql, slotId);
    }
}
