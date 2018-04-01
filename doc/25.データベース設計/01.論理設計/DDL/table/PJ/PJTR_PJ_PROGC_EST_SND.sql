-- テーブル用DDL
DROP TABLE PJTR_PJ_PROGC_EST_SND CASCADE CONSTRAINTS;

CREATE TABLE PJTR_PJ_PROGC_EST_SND
(
    PRJ_CD                         CHAR(10) NOT NULL ,
    PROG_UNT_NO_SND_DT             DATE ,
    SND_ISTR_FLG                   CHAR(1) DEFAULT '0' NOT NULL ,
    SND_TMS                        NUMBER(6) DEFAULT 0 NOT NULL ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTR_PJ_PROGC_EST_SND ADD CONSTRAINT PK_PJTR_PJ_PROGC_EST_SND PRIMARY KEY (PRJ_CD ) ;

COMMENT ON TABLE PJTR_PJ_PROGC_EST_SND IS '進行原価見積送信指示トラン(ver.0.6)';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.PRJ_CD IS 'プロジェクトコード';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.PROG_UNT_NO_SND_DT IS '進行単位番号送信日';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.SND_ISTR_FLG IS '送信指示フラグ';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.SND_TMS IS '送信回数';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTR_PJ_PROGC_EST_SND.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL
