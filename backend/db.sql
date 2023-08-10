-- create DB
DROP DATABASE IF EXISTS mcheap;

CREATE DATABASE mcheap CHARACTER
SET
  utf8;

-- move to mcheap
USE mcheap;

-- create tables
CREATE TABLE
  search (
    id INT AUTO_INCREMENT PRIMARY KEY,
    keyword VARCHAR(10) NOT NULL,
    carrot INT NOT NULL,
    thunder INT NOT NULL,
    joongna INT NOT NULL,
    collected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ) ENGINE = InnoDB;
