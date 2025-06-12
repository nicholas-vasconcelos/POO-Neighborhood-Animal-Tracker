-- CREATE the DB first and then USE
CREATE SCHEMA animaltracker;
USE animaltracker;

-- DROP TABLES Users, Animals, ReportLikes, AnimalLikes, Dogs, Cats, Capybaras, Reports;

-- CREATE the tables
-- Table User
CREATE TABLE Users (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    condominium VARCHAR(255)
);

-- Table Animal
CREATE TABLE Animals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nick_name VARCHAR(255) NOT NULL,
    color VARCHAR(50) NOT NULL,
    size VARCHAR(50) NOT NULL
);

-- Create Sub-Table Cats
CREATE TABLE Cats (
    id INT PRIMARY KEY,
    breed VARCHAR(255),
    scratches_people BOOLEAN,
    likes_being_pet BOOLEAN,
    is_vaccinated BOOLEAN,
    is_castrated BOOLEAN,
    FOREIGN KEY (id) REFERENCES Animals(id)
);

-- Create Sub-Table Dogs
CREATE TABLE Dogs (
    id INT PRIMARY KEY,
    breed VARCHAR(255),
	tail_length VARCHAR(50),
	chases_cars BOOLEAN,
    likes_being_pet BOOLEAN,
    is_vaccinated BOOLEAN,
    is_castrated BOOLEAN,
    FOREIGN KEY (id) REFERENCES Animals(id)
);

-- Create Sub-Table Capybaras
CREATE TABLE Capybaras (
	id INT PRIMARY KEY,
    social_group_size INT,
	eats_grass BOOLEAN,
    FOREIGN KEY (id) REFERENCES Animals(id)
);

-- Create Table Reports
CREATE TABLE Reports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description TEXT,
    street_name VARCHAR(255) NOT NULL,
    created_by_user_id INT NOT NULL,    
    reported_animal_id INT NOT NULL UNIQUE, 
    FOREIGN KEY (created_by_user_id) REFERENCES Users(id),
    FOREIGN KEY (reported_animal_id) REFERENCES Animals(id)
);

-- Create Table AnimalLikes, that holds likes by Users on Animals
CREATE TABLE AnimalLikes (
	user_id INT NOT NULL,
    animal_id INT NOT NULL,
    PRIMARY KEY (user_id, animal_id),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (animal_id) REFERENCES Animals(id)
);

-- Create Table AnimalLikes, that holds likes by Users on Reports
CREATE TABLE ReportLikes (
	user_id INT NOT NULL,
    report_id INT NOT NULL,
    PRIMARY KEY (user_id, report_id),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (report_id) REFERENCES Reports(id)
);