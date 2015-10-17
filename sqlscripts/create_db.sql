CREATE TABLE units
(
	id int not null generated always as identity (start with 1, increment by 1),
	unit varchar(100) not null,
	PRIMARY KEY (id),
	UNIQUE (unit)
);

INSERT INTO units(unit) VALUES ('амп'), ('флак'), ('шт'), ('гр'), ('кг'), ('набір'), ('уп'), ('кор'), ('пар');

CREATE TABLE source_group
(
	id int not null generated always as identity (start with 1, increment by 1),
	source varchar(100) not null,
	PRIMARY KEY (id),
	UNIQUE (source)
);

INSERT INTO source_group(source) VALUES ('Реактиви, поживні середовища'), ('Меценат'), ('Від провізора'), ('Від дезінфектора');

CREATE TABLE store
(
	id int not null generated always as identity (start with 1, increment by 1),
	name varchar(255) not null,
	price double not null,
	amount double not null,
	source_id int not null,
	unit_id int not null,
	PRIMARY KEY (id),
	UNIQUE (name, price, source_id, unit_id),
	FOREIGN KEY (source_id) REFERENCES source_group(id),
	FOREIGN KEY (unit_id) REFERENCES units(id)
);

CREATE TABLE incomings
(
	id int not null generated always as identity (start with 1, increment by 1),
	amount double not null,
	incoming_date date not null,
	product_id int not null,
	PRIMARY KEY (id),
	FOREIGN KEY (product_id) REFERENCES store(id)
);

CREATE TABLE outcomings
(
	id int not null generated always as identity (start with 1, increment by 1),
	amount double not null,
	outcoming_date date not null,
	product_id int not null,
	PRIMARY KEY (id),
	FOREIGN KEY (product_id) REFERENCES store(id)
);