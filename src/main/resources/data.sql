-- Seed data for development and demonstration
-- Uses INSERT OR IGNORE so re-runs on existing SQLite file are safe (no duplicates)
-- NOTE: passwords stored as plain text for demo only; production would use BCrypt

-- Users (explicit IDs so IGNORE triggers on PK conflict)
INSERT OR IGNORE INTO users (user_id, username, email, password_hash, first_name, last_name, role, phone) VALUES
(1, 'advisor_jane', 'jane.smith@example.com',  'jane123', 'Jane', 'Smith',   'ADVISOR', '408-555-0100'),
(2, 'advisor_mark', 'mark.johnson@example.com','mark123', 'Mark', 'Johnson', 'ADVISOR', '408-555-0199'),
(3, 'client_alice', 'alice@example.com',        'alice123','Alice','Chen',   'CLIENT',  '408-555-0201'),
(4, 'client_bob',   'bob@example.com',          'bob123',  'Bob', 'Torres',  'CLIENT',  '408-555-0302');

-- Advisor profiles
INSERT OR IGNORE INTO advisors (advisor_id, user_id, specialization, bio) VALUES
(1, 1, 'Business Strategy & Leadership',
 'Experienced CEO with 20+ years leading Fortune 500 companies. Specializes in startup growth strategy and executive leadership coaching.'),
(2, 2, 'Finance & Investment',
 'Former CFO with deep expertise in venture funding, financial modeling, and scaling startups from seed to Series B.');

-- Services offered by Jane Smith
INSERT OR IGNORE INTO services (service_id, advisor_id, service_name, description, duration_minutes) VALUES
(1, 1, 'Business Strategy',   'One-on-one session covering business model design, market analysis, and growth strategy.', 60),
(2, 1, 'Financial Planning',  'Review of business financials, funding strategies, and budgeting guidance.',               45),
(3, 1, 'Leadership Coaching', 'Executive coaching focused on team building, decision-making, and leadership skills.',      60),
(4, 1, 'Personal Development','Goal setting, time management, and personal effectiveness for entrepreneurs.',              30);

-- Services offered by Mark Johnson
INSERT OR IGNORE INTO services (service_id, advisor_id, service_name, description, duration_minutes) VALUES
(5, 2, 'Fundraising Strategy', 'Preparing pitch decks, investor outreach, and navigating term sheets.',                    60),
(6, 2, 'Financial Modeling',   'Building revenue models, cash flow projections, and scenario analysis.',                   60),
(7, 2, 'Investor Relations',   'Managing cap tables, reporting to investors, and preparing for board meetings.',            45),
(8, 2, 'Exit Planning',        'Structuring acquisitions, due diligence prep, and maximizing valuation.',                   60);

-- Availability slots for Jane Smith (advisor_id=1)
INSERT OR IGNORE INTO availability_slots (slot_id, advisor_id, start_time, end_time, is_booked) VALUES
(1, 1, '2026-05-19 09:00:00', '2026-05-19 10:00:00', 0),
(2, 1, '2026-05-19 10:30:00', '2026-05-19 11:30:00', 0),
(3, 1, '2026-05-19 13:00:00', '2026-05-19 14:00:00', 0),
(4, 1, '2026-05-20 09:00:00', '2026-05-20 10:00:00', 0),
(5, 1, '2026-05-20 11:00:00', '2026-05-20 12:00:00', 0),
(6, 1, '2026-05-21 14:00:00', '2026-05-21 15:00:00', 1);

-- Availability slots for Mark Johnson (advisor_id=2)
INSERT OR IGNORE INTO availability_slots (slot_id, advisor_id, start_time, end_time, is_booked) VALUES
(7, 2, '2026-05-19 10:00:00', '2026-05-19 11:00:00', 0),
(8, 2, '2026-05-19 14:00:00', '2026-05-19 15:00:00', 0),
(9, 2, '2026-05-20 13:00:00', '2026-05-20 14:00:00', 0),
(10,2, '2026-05-21 09:00:00', '2026-05-21 10:00:00', 1);

-- Existing appointments (for booked slots)
INSERT OR IGNORE INTO appointments (appointment_id, client_id, slot_id, service_id, status, notes) VALUES
(1, 3, 6,  1, 'BOOKED', 'Discuss Q2 expansion plans'),
(2, 4, 10, 5, 'BOOKED', 'Seed round fundraising prep');
