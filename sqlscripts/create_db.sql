CREATE TABLE source_group
(
	id int not null generated always as identity (start with 1, increment by 1),
	name varchar(100) not null,
	PRIMARY KEY (id),
	UNIQUE (name)
);

INSERT INTO source_group(name) VALUES ('Реактиви, поживні середовища'), ('Меценат'), ('Від провізора'), ('Від дезінфектора');

CREATE TABLE store
(
	id int not null generated always as identity (start with 1, increment by 1),
	name varchar(255) not null,
	price double not null,
	amount double not null,
	source_id int not null,
	PRIMARY KEY (id),
	UNIQUE (name, price, source_id),
	FOREIGN KEY (source_id) REFERENCES source_group(id)
);

CREATE TABLE incomings
(
	id int not null generated always as identity (start with 1, increment by 1),
	amount double not null,
	incoming_date date not null,
	product_id int not null,
	FOREIGN KEY (product_id) REFERENCES store(id)
);

CREATE TABLE outcomings
(
	id int not null generated always as identity (start with 1, increment by 1),
	amount double not null,
	outcoming_date date not null,
	product_id int not null,
	FOREIGN KEY (product_id) REFERENCES store(id)
);