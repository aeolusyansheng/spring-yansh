-- テーブル用DDL
DROP TABLE PJTR_PJ_TSK_RESRC_DIST_PV CASCADE CONSTRAINTS;

CREATE TABLE PJTR_PJ_TSK_RESRC_DIST_PV
(
    TSK_UID                        NUMBER(15) NOT NULL ,
    RPT_BSDT                       DATE NOT NULL ,
    EMP_CD                         VARCHAR2(30) NOT NULL ,
    PLN_MNHOUR                     NUMBER(10,2) ,
    PRJ_CD                         CHAR(10) NOT NULL ,
    WEEK_PV                        NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    WEEK_PV_PRICE                  NUMBER(12) DEFAULT 0 NOT NULL ,
    CUMU_PV                        NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    CUMU_PV_PRICE                  NUMBER(12) DEFAULT 0 NOT NULL ,
    THIS_RSLT                      NUMBER(12,4) ,
    THIS_RSLT_REM                  NUMBER(12,4) ,
    AC                             NUMBER(10,2) ,
    AC_PRICE                       NUMBER(12) ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTR_PJ_TSK_RESRC_DIST_PV ADD CONSTRAINT PK_PJTR_PJ_TSK_RESRC_DIST_PV PRIMARY KEY (TSK_UID, RPT_BSDT, EMP_CD ) ;

COMMENT ON TABLE PJTR_PJ_TSK_RESRC_DIST_PV IS 'タスクリソース別PVトラン(ver.0.6)';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.TSK_UID IS 'タスクUID';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.RPT_BSDT IS '報告基準日';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.EMP_CD IS '従業員コード';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.PLN_MNHOUR IS '予定工数';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.PRJ_CD IS 'プロジェクトコード';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.WEEK_PV IS '週PV';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.WEEK_PV_PRICE IS '週PV額';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.CUMU_PV IS '累積PV';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.CUMU_PV_PRICE IS '累積PV額';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.THIS_RSLT IS '今回実績';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.THIS_RSLT_REM IS '今回実績残';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.AC IS 'AC';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.AC_PRICE IS 'AC額';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTR_PJ_TSK_RESRC_DIST_PV.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL
CREATE INDEX IX1_PJTR_PJ_TSK_RESRC_DIST_PV ON PJTR_PJ_TSK_RESRC_DIST_PV (
    PRJ_CD 
);

