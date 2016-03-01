DROP TABLE MUSICREC;
DROP SEQUENCE musicreq_seq;

CREATE TABLE MUSICREC (
  id          INT PRIMARY KEY,
  customer    VARCHAR(50),
  label       VARCHAR(500),
  releaseDate DATE,
  title       VARCHAR(500),
  duration    INT,
  albumName   VARCHAR(500),
  acrId       VARCHAR(500),
  genres      VARCHAR(500),
  artists     VARCHAR(500),
  recDate     TIMESTAMP
);


CREATE SEQUENCE musicreq_seq;
CREATE OR REPLACE TRIGGER musicreq_trg
BEFORE INSERT ON MUSICREC
FOR EACH ROW
  BEGIN
    SELECT musicreq_seq.NEXTVAL
    INTO :new.id
    FROM dual;
  END;