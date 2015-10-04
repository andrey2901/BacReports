CREATE TABLE store
(
	name varchar(40) not null, 
	price varchar(15) not null, 
	amount varchar(15) not null,
	CONSTRAINT st_productID PRIMARY KEY (name,price)
)