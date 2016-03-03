use bgm;
DROP TABLE IF EXISTS MUSICREC;

CREATE TABLE MUSICREC (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  customer    VARCHAR(50),
  acrid       VARCHAR(500),
  title       VARCHAR(500),
  label       VARCHAR(500),
  duration    LONG,
  releaseDate DATE,
  album   VARCHAR(500),
  genres      VARCHAR(500),
  artists     VARCHAR(500),
  playOffset LONG,
  isrcCode     VARCHAR(500),
  upcCode     VARCHAR(500),
  recDate     TIMESTAMP
);
