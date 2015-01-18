DROP DATABASE MovieDB;
CREATE DATABASE MovieDB;
	use MovieDB;
	CREATE TABLE movies (
		id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
		title VARCHAR(100) NOT NULL default '',
		year INT NOT NULL,
		director VARCHAR(100) NOT NULL default '',
		banner_url VARCHAR(200),
		trailer_url VARCHAR(200)
		);
	CREATE TABLE stars (
		id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
		first_name VARCHAR(50) NOT NULL default '',
		last_name VARCHAR(50) NOT NULL default '',
		dob DATE,
		photo_url VARCHAR(200)
		);
	CREATE TABLE stars_in_movies (
		star_id INT NOT NULL,
		movie_id INT NOT NULL,
		CONSTRAINT fk_starmovie1 FOREIGN KEY(star_id) REFERENCES stars(id) ON DELETE CASCADE ON UPDATE CASCADE,
		CONSTRAINT fk_starmovie2 FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE
		);
	CREATE TABLE genres (
		id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
		name VARCHAR(32) NOT NULL default ''
		);
	CREATE TABLE genres_in_movies (
		genre_id INT NOT NULL,
		movie_id INT NOT NULL,
		CONSTRAINT fk_genresmovie1 FOREIGN KEY(genre_id) REFERENCES genres(id) ON DELETE CASCADE ON UPDATE CASCADE,
		CONSTRAINT fk_genresmovie2 FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE
		);
	CREATE TABLE creditcards (
		id VARCHAR(20) NOT NULL PRIMARY KEY,
		first_name VARCHAR(50) NOT NULL,
		last_name VARCHAR(50) NOT NULL,
		expiration DATE NOT NULL
		);
	CREATE TABLE customers (
		id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
		first_name VARCHAR(50) NOT NULL,
		last_name VARCHAR(50) NOT NULL,
		cc_id VARCHAR(20) NOT NULL,
		address VARCHAR(200) NOT NULL,
		email VARCHAR(50) NOT NULL,
		password VARCHAR(20) NOT NULL,
		CONSTRAINT fk_customercreditcard FOREIGN KEY(cc_id) REFERENCES creditcards(id) ON DELETE CASCADE ON UPDATE CASCADE
		);
	CREATE TABLE sales (
		id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
		customer_id INT NOT NULL,
		movie_id INT NOT NULL,
		sale_date DATE NOT NULL,
		CONSTRAINT fk_salescustomer FOREIGN KEY(customer_id) REFERENCES customers(id) ON DELETE CASCADE ON UPDATE CASCADE,
		CONSTRAINT fk_salesmovie FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE		
		);