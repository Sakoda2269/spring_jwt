DROP TABLE IF EXISTS message;

CREATE TABLE IF NOT EXISTS message(
	id varchar,
	content varchar
);

DROP TABLE IF EXISTS person;

CREATE TABLE IF NOT EXISTS person(
	id VARCHAR(36) PRIMARY KEY,
	username VARCHAR(255),
	password VARCHAR(255),
	email VARCHAR(255),
	enabled BOOLEAN,
	roles VARCHAR(255)
);

INSERT INTO person VALUES 
('79de0f50-5113-43ec-83c5-c16671d01cf4', 'test1', '$2a$08$7563MWncbW/oenHWfZSKE.jWeMARqdO/WZajbstas2xUamOBXDWz6', 'test1@example.com', 'true', 'ROLE_ADMIN,ROLE_GENERAL');
INSERT INTO person VALUES
('b5c05ffc-0464-4074-83d7-e582e80c63b3', 'test2', '$2a$08$7563MWncbW/oenHWfZSKE.jWeMARqdO/WZajbstas2xUamOBXDWz6', 'test1@example.com', 'true', 'ROLE_GENERAL');