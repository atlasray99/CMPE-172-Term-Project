package com.advisingscheduler.repository;

import com.advisingscheduler.model.Appointment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppointmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Appointment> detailedRowMapper = (ResultSet rs, int rowNum) -> {
        Appointment appt = new Appointment();
        appt.setAppointmentId(rs.getInt("appointment_id"));
        appt.setClientId(rs.getInt("client_id"));
        appt.setSlotId(rs.getInt("slot_id"));
        appt.setServiceId(rs.getInt("service_id"));
        appt.setStatus(rs.getString("status"));
        appt.setBookedAt(rs.getTimestamp("booked_at").toLocalDateTime());
        appt.setNotes(rs.getString("notes"));
        appt.setClientName(rs.getString("username"));
        appt.setServiceName(rs.getString("service_name"));
        appt.setSlotStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        appt.setSlotEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        return appt;
    };

    private static final String DETAILED_SELECT =
            "SELECT a.*, u.username, s.service_name, sl.start_time, sl.end_time "
          + "FROM appointments a "
          + "JOIN users u ON a.client_id = u.user_id "
          + "JOIN services s ON a.service_id = s.service_id "
          + "JOIN availability_slots sl ON a.slot_id = sl.slot_id ";

    public List<Appointment> findAllDetailed() {
        String sql = DETAILED_SELECT + "ORDER BY sl.start_time DESC";
        return jdbcTemplate.query(sql, detailedRowMapper);
    }

    public Optional<Appointment> findByIdDetailed(int appointmentId) {
        String sql = DETAILED_SELECT + "WHERE a.appointment_id = ?";
        List<Appointment> results = jdbcTemplate.query(sql, detailedRowMapper, appointmentId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public int insert(int clientId, int slotId, int serviceId, String notes) {
        String sql = "INSERT INTO appointments (client_id, slot_id, service_id, notes) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, clientId, slotId, serviceId, notes);

        // SQLite-specific: last_insert_rowid() returns the PK of the row just inserted
        // on this connection — safe within a @Transactional boundary
        Integer id = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);
        return id != null ? id : -1;
    }

    public int updateStatus(int appointmentId, String newStatus) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ? AND status = 'BOOKED'";
        return jdbcTemplate.update(sql, newStatus, appointmentId);
    }
}
