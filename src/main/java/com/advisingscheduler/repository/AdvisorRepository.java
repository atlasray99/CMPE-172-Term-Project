package com.advisingscheduler.repository;

import org.springframework.jdbc.core.JdbcTemplate;
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
