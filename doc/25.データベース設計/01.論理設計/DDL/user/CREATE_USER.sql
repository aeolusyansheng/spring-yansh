-- ユーザ(スキーマ)用DDL
DEFINE uname  = pjnavi
DEFINE passwd = pjnavi
DEFINE defts  = USERS
DEFINE tmpts  = TEMP

--DROP USER &uname CASCADE;

CREATE USER &uname
       IDENTIFIED BY &passwd
--       DEFAULT TABLESPACE &defts
--       TEMPORARY TABLESPACE &tmpts
;

GRANT CONNECT,RESOURCE TO &uname;
GRANT CREATE VIEW,CREATE DATABASE LINK TO &uname;

