CREATE DATABASE IF NOT EXISTS tmd_dpbo;
USE tmd_dpbo;

CREATE TABLE IF NOT EXISTS tbenefit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    skor INT DEFAULT 0,
    peluru_meleset INT DEFAULT 0,
    peluru_sisa INT DEFAULT 0
);

INSERT INTO tbenefit (username, skor, peluru_meleset, peluru_sisa) VALUES
('Guest1', 50, 3, 1),
('Guest2', 120, 6, 2),
('Tester', 200, 10, 4)
ON DUPLICATE KEY UPDATE username=username;