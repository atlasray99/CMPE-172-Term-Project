-- Users table: stores all system users (clients and advisors)
CREATE TABLE IF NOT EXISTS users (
    user_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50),
    role          VARCHAR(10)  NOT NULL CHECK (role IN ('CLIENT', 'ADVISOR')),
    phone         VARCHAR(20),
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Advisors table: extends users for advisor-specific attributes (ISA specialization)
CREATE TABLE IF NOT EXISTS advisors (
    advisor_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id        INTEGER NOT NULL UNIQUE,
    specialization VARCHAR(100),
    bio            TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Services table: types of advising sessions offered by an advisor
CREATE TABLE IF NOT EXISTS services (
    service_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    advisor_id       INTEGER      NOT NULL,
    service_name     VARCHAR(100) NOT NULL,
    description      TEXT,
    duration_minutes INTEGER      NOT NULL,
    FOREIGN KEY (advisor_id) REFERENCES advisors(advisor_id) ON DELETE CASCADE
);

-- Availability slots: discrete time blocks when an advisor is available
CREATE TABLE IF NOT EXISTS availability_slots (
    slot_id    INTEGER PRIMARY KEY AUTOINCREMENT,
    advisor_id INTEGER   NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time   TIMESTAMP NOT NULL,
    is_booked  INTEGER   NOT NULL DEFAULT 0,
    version    INTEGER   NOT NULL DEFAULT 0,
    FOREIGN KEY (advisor_id) REFERENCES advisors(advisor_id) ON DELETE CASCADE
);

-- Appointments: a booked advising session between a client and an advisor
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    client_id      INTEGER     NOT NULL,
    slot_id        INTEGER     NOT NULL UNIQUE,
    service_id     INTEGER     NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'BOOKED' CHECK (status IN ('BOOKED', 'CANCELLED', 'COMPLETED')),
    booked_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes          TEXT,
    FOREIGN KEY (client_id)  REFERENCES users(user_id),
    FOREIGN KEY (slot_id)    REFERENCES availability_slots(slot_id),
    FOREIGN KEY (service_id) REFERENCES services(service_id)
);
