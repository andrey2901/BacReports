ALTER TABLE store DROP CONSTRAINT productID;
DROP TABLE store;
ALTER TABLE incomings DROP CONSTRAINT incomingID;
DROP TABLE incomings;
ALTER TABLE outcomings DROP CONSTRAINT outcomingID;
DROP TABLE outcomings;