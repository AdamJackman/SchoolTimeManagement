/*
 * The tables we have in our database. For reference.
 */
CREATE TABLE "course" (
	cid CHAR(6) PRIMARY KEY
);

CREATE TABLE "users" ( /* 'user' is a reserved keyword */
	name VARCHAR(20) NOT NULL,
	email VARCHAR(50) PRIMARY KEY,
	password VARCHAR (20) NOT NULL
);

CREATE TABLE "enrolled" (
	cid CHAR(6) REFERENCES course(cid) ON DELETE CASCADE,
	email VARCHAR(50) REFERENCES users(email) ON DELETE CASCADE,
	PRIMARY KEY (cid, email)
);

CREATE TABLE "lecture" (
	lid SERIAL PRIMARY KEY, /* We do not set this number, it is serial */
	cid CHAR(6) REFERENCES course(cid) ON DELETE CASCADE,
	title VARCHAR(20) NOT NULL,
	days VARCHAR(20) NOT NULL,
	months VARCHAR(30) NOT NULL,
	year INT NOT NULL,
	start_time CHAR(5) NOT NULL, /* Formatted as 00:00 */
	end_time CHAR(5) NOT NULL,
	description VARCHAR(100) NOT NULL
);

CREATE TABLE "assignment" (
	aid SERIAL PRIMARY KEY, /* We do not set this number, it is serial */
	cid CHAR(6) REFERENCES course(cid) ON DELETE CASCADE,
	title VARCHAR(20) NOT NULL,
   	due_date CHAR(10) NOT NULL, /* DD,MM,YYYY */
   	due_time CHAR(5) NOT NULL,
	description VARCHAR(100) NOT NULL,
	weight INT NOT NULL CHECK (weight >= 0 AND weight <= 100) /* Assignment weight must be between 0 and 100 */
	votes INT DEFAULT 0;
	verified VARCHAR(5) DEFAULT 'false';
	changed VARCHAR(100) DEFAULT '';

);

CREATE TABLE "personal" (
	pid SERIAL PRIMARY KEY, /* We do not set this number, it is serial */
	title VARCHAR(20) NOT NULL,
	event_date CHAR(10) NOT NULL,
	start_time CHAR(5) NOT NULL,
	end_time CHAR(5) NOT NULL,
	description VARCHAR(100) NOT NULL,
	email VARCHAR(50) REFERENCES users(email) ON DELETE CASCADE
);

CREATE TABLE "generated" (
	cid CHAR(6) REFERENCES course(cid) ON DELETE CASCADE,
	String VARCHAR(50),
	assignment INT REFERENCES assignment(aid),
	PRIMARY KEY (cid, assignment)
);
