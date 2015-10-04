CREATE TABLE store
(
	name varchar(40) not null,
	price varchar(15) not null,
	amount varchar(15) not null,
	CONSTRAINT productID PRIMARY KEY (name, price)
);

CREATE TABLE incomings
(
	name varchar(40) not null,
	price varchar(15) not null,
	amount varchar(15) not null,
	incoming_date timestamp not null,
	CONSTRAINT incomingID PRIMARY KEY (name, price, incoming_date)
);

CREATE TABLE outcomings
(
	name varchar(40) not null,
	price varchar(15) not null,
	amount varchar(15) not null,
	outcoming_date timestamp not null,
	CONSTRAINT outcomingID PRIMARY KEY (name, price, outcoming_date)
);