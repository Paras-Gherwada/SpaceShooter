 -- Oracle 18c --

CREATE TABLE SPACE_SHOOTER(
	"USERNAME" VARCHAR2(20 BYTE), 
	"PASSWORD" VARCHAR2(20 BYTE), 
	"POINTS" NUMBER, 
	"ASTEROIDS" NUMBER, 
	"DEATH_COUNT" NUMBER
)

Insert into APOPHIS.SPACE_SHOOTER (USERNAME,PASSWORD,POINTS,ASTEROIDS,DEATH_COUNT) values ('apophis','apophis',6500,21,4);
Insert into APOPHIS.SPACE_SHOOTER (USERNAME,PASSWORD,POINTS,ASTEROIDS,DEATH_COUNT) values ('Guest','Guest',0,0,0);
