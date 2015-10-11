CREATE TABLE source_group
(
	id int not null generated always as identity (start with 1, increment by 1),
	name varchar(100) not null,
	PRIMARY KEY (id),
	UNIQUE (name)
);

INSERT INTO source_group(name) VALUES ('��������, ������ ����������'), ('�������'), ('³� ��������'), ('³� �����������');

CREATE TABLE store
(
	name varchar(150) not null,
	price double not null,
	amount double not null,
	source int not null,
	CONSTRAINT productID PRIMARY KEY (name, price),
	FOREIGN KEY (source) REFERENCES source_group(id)
);

CREATE TABLE incomings
(
	name varchar(150) not null,
	price double not null,
	amount double not null,
	incoming_date date not null,
	source int not null,
	FOREIGN KEY (source) REFERENCES source_group(id)
);

CREATE TABLE outcomings
(
	name varchar(150) not null,
	price double not null,
	amount double not null,
	outcoming_date date not null,
	source int not null,
	FOREIGN KEY (source) REFERENCES source_group(id)
);