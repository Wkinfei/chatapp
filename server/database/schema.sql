drop database if exists chat_app;
CREATE database chat_app;
use chat_app;

drop table if exists user_profiles;

create table user_profiles(
	user_id int auto_increment,
	login_username varchar(64),
    login_password varchar(16),
    display_name varchar(64),
    email varchar(50),
    image_url varchar(4000),
    constraint pk_id primary key (user_id)
);

drop table if exists user_relationship;
create table user_relationship(
	chat_id int auto_increment,
	user_id1 int,
    user_id2 int,
    constraint pk_id primary key (chat_id)
);

INSERT INTO user_profiles (login_username, login_password, display_name,email,image_url) 
VALUES ("Fred123", "fred123", "FredFred","fred@gmail.com","https://robohash.org/jz2xPMAWki1.png?set=set4&size=100x100");
INSERT INTO user_profiles (login_username, login_password, display_name,email,image_url) 
VALUES ("Ted123", "ted123", "Ted","ted@gmail.com","https://robohash.org/jz2xPMAWki2.png?set=set4&size=100x100");
INSERT INTO user_profiles (login_username, login_password, display_name,email,image_url) 
VALUES ("Jame123", "Jame123", "Jame","Jame@gmail.com","https://robohash.org/jz2xPMAWki3.png?set=set4&size=100x100");
INSERT INTO user_profiles (login_username, login_password, display_name,email,image_url) 
VALUES ("John123", "John123", "John","John@gmail.com","https://robohash.org/jz2xPMAWki4.png?set=set4&size=100x100");
INSERT INTO user_profiles (login_username, login_password, display_name,email,image_url) 
VALUES ("Tom123", "Tom123", "Tom","Tom@gmail.com","https://robohash.org/jz2xPMAWki5.png?set=set4&size=100x100");

INSERT INTO user_relationship (user_id1,user_id2) VALUES(1,2);
INSERT INTO user_relationship (user_id1,user_id2) VALUES(1,3);
INSERT INTO user_relationship (user_id1,user_id2) VALUES(1,4);
INSERT INTO user_relationship (user_id1,user_id2) VALUES(1,5);
INSERT INTO user_relationship (user_id1,user_id2) VALUES(2,3);
INSERT INTO user_relationship (user_id1,user_id2) VALUES(2,5);
INSERT INTO user_relationship (user_id1,user_id2) VALUES(3,4);