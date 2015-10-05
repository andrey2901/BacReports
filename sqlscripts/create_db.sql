CREATE TABLE store
(
	name varchar(40) not null,
	price double not null,
	amount double not null,
	CONSTRAINT productID PRIMARY KEY (name, price)
);

CREATE TABLE incomings
(
	name varchar(40) not null,
	price double not null,
	amount double not null,
	incoming_date date not null,
	CONSTRAINT incomingID PRIMARY KEY (name, price, incoming_date)
);

CREATE TABLE outcomings
(
	name varchar(40) not null,
	price double not null,
	amount double not null,
	outcoming_date date not null,
	CONSTRAINT outcomingID PRIMARY KEY (name, price, outcoming_date)
);