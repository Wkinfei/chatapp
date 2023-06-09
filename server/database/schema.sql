drop database if exists chat_app;
CREATE database chat_app;
use chat_app;

drop table if exists user_profiles;

create table user_profiles(
	user_id int auto_increment,
    username varchar(64),
    password varchar(128),
    email varchar(50),
    image_url varchar(4000),
    role varchar(64) NOT NULL,
	enabled tinyint DEFAULT NULL,
    constraint pk_id primary key (user_id)
);

drop table if exists user_relationship;
create table user_relationship(
	chat_id int auto_increment,
	user_id1 int,
    user_id2 int,
    constraint pk_id primary key (chat_id)
);

