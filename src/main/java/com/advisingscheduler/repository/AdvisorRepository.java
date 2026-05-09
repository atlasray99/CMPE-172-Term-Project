package com.advisingscheduler.repository;

import com.advisingscheduler.model.Advisor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AdvisorRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdvisorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Advisor> advisorRowMapper = (rs, rowNum) -> {
        Advisor a = new Advisor();
        a.setAdvisorId(rs.getInt("advisor_id"));
        a.setFullName(rs.getString("full_name"));
        a.setSpecialization(rs.getString("specialization"));
        return a;
    };

    // All advisors — used to populate the advisor picker on the booking form
    public List<Advisor> findAll() {
        String sql =
            "SELECT a.advisor_id, (u.first_name || ' ' || u.last_name) AS full_name, a.specialization " +
            "FROM advisors a " +
            "JOIN users u ON a.user_id = u.user_id " +
            "ORDER BY u.first_name";
        return jdbcTemplate.query(sql, advisorRowMapper);
    }

    /**
     * Validates advisor credentials and returns their advisor_id and full name.
     * NOTE: Plain-text password comparison for demo purposes only.
     *       A production system would use BCrypt.
     */
    public Optional<Map<String, Object>> authenticate(String username, String password) {
        String sql =
            "SELECT a.advisor_id, u.first_name, u.last_name " +
            "FROM users u " +
            "JOIN advisors a ON u.user_id = a.user_id " +
            "WHERE u.username = ? AND u.password_hash = ? AND u.role = 'ADVISOR'";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, username, password);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
