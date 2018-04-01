-- データベースリンク用DDL
DEFINE linknm = PJLK_NEXTMI
DEFINE uname  = pjnavi
DEFINE passwd = pjnavi
DEFINE usstr  = localhost/pjnavidb

DROP DATABASE LINK &linknm;

CREATE DATABASE LINK &linknm
       CONNECT TO       &uname
       IDENTIFIED BY    &passwd
       USING '&usstr'
;
