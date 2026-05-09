-- Seed data for development and demonstration
-- Uses INSERT OR IGNORE so re-runs on existing SQLite file are safe (no duplicates)

-- Users (explicit IDs so IGNORE triggers on PK conflict)
INSERT OR IGNORE INTO users (user_id, username, email, password_hash, role, phone) VALUES
(1, 'advisor_jane', 'jane.ceo@example.com', '$2a$10$hashedpassword1', 'ADVISOR', '408-555-0100'),
(2, 'client_alice',  'alice@example.com',   '$2a$10$hashedpassword2', 'CLIENT',  '408-555-0201'),
(3, 'client_bob',    'bob@example.com',     '$2a$10$hashedpassword3', 'CLIENT',  '408-555-0302');

-- Advisor profile
INSERT OR IGNORE INTO advisors (advisor_id, user_id, specialization, bio) VALUES
(1, 1, 'Business Strategy & Leadership',
 'Experienced CEO with 20+ years leading Fortune 500 companies. Specializes in startup growth strategy and executive leadership coaching.');

-- Services offered
INSERT OR IGNORE INTO services (service_id, advisor_id, service_name, description, duration_minutes) VALUES
(1, 1, 'Business Strategy',   'One-on-one session covering business model design, market analysis, and growth strategy.', 60),
(2, 1, 'Financial Planning',  'Review of business financials, funding strategies, and budgeting guidance.',               45),
(3, 1, 'Leadership Coaching', 'Executive coaching focused on team building, decision-making, and leadership skills.',      60),
(4, 1, 'Personal Development','Goal setting, time management, and personal effectiveness for entrepreneurs.',              30);

-- Availability slots (is_booked: 0 = available, 1 = booked)
INSERT OR IGNORE INTO availability_slots (slot_id, advisor_id, start_time, end_time, is_booked) VALUES
(1, 1, '2026-05-15 09:00:00', '2026-05-15 10:00:00', 0),
(2, 1, '2026-05-15 10:30:00', '2026-05-15 11:30:00', 0),
(3, 1, '2026-05-15 13:00:00', '2026-05-15 14:00:00', 0),
(4, 1, '2026-05-16 09:00:00', '2026-05-16 10:00:00', 0),
(5, 1, '2026-05-16 11:00:00', '2026-05-16 12:00:00', 0),
(6, 1, '2026-05-17 14:00:00', '2026-05-17 15:00:00', 0),
(7, 1, '2026-05-18 09:00:00', '2026-05-18 10:00:00', 1),
(8, 1, '2026-05-18 10:30:00', '2026-05-18 11:30:00', 0);

-- One existing appointment (for slot 7 which is marked as booked)
INSERT OR IGNORE INTO appointments (appointment_id, client_id, slot_id, service_id, status, notes) VALUES
(1, 2, 7, 1, 'BOOKED', 'Discuss Q2 expansion plans');
