package com.advisingscheduler.repository;

import com.advisingscheduler.model.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class ServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    public ServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Service> rowMapper = (ResultSet rs, int rowNum) -> {
        Service svc = new Service();
        svc.setServiceId(rs.getInt("service_id"));
        svc.setAdvisorId(rs.getInt("advisor_id"));
        svc.setServiceName(rs.getString("service_name"));
        svc.setDescription(rs.getString("description"));
        svc.setDurationMinutes(rs.getInt("duration_minutes"));
        return svc;
    };

    public List<Service> findAll() {
        String sql = "SELECT * FROM services ORDER BY service_name";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Service> findByAdvisorId(int advisorId) {
        String sql = "SELECT * FROM services WHERE advisor_id = ? ORDER BY service_name";
        return jdbcTemplate.query(sql, rowMapper, advisorId);
    }
}
