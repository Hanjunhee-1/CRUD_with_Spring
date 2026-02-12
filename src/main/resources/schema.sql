create table users (
	id int auto_increment primary key,
	nickname varchar(255) not null,
	password varchar(255) not null
);

create table boards (
	id int auto_increment primary key,
	title varchar(255) not null,
	content text not null,
	created_at timestamp default current_timestamp,
	updated_at timestamp,
	user_id int,
	constraint fk_users_boards
	foreign key (userId)
	references users (userId)
);