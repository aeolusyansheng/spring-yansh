-- テーブル用DDL
DROP TABLE PJTW_PJ_DTCL_WBS_KEY CASCADE CONSTRAINTS;

CREATE TABLE PJTW_PJ_DTCL_WBS_KEY
(
    TSK_UID                        NUMBER(15) NOT NULL ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTW_PJ_DTCL_WBS_KEY ADD CONSTRAINT PK_PJTW_PJ_DTCL_WBS_KEY PRIMARY KEY (TSK_UID ) ;

COMMENT ON TABLE PJTW_PJ_DTCL_WBS_KEY IS 'データクリーニング対象WBSキー情報ワーク(ver.0.1)';
COMMENT ON COLUMN PJTW_PJ_DTCL_WBS_KEY.TSK_UID IS 'タスクUID';
COMMENT ON COLUMN PJTW_PJ_DTCL_WBS_KEY.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTW_PJ_DTCL_WBS_KEY.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTW_PJ_DTCL_WBS_KEY.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTW_PJ_DTCL_WBS_KEY.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTW_PJ_DTCL_WBS_KEY.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTW_PJ_DTCL_WBS_KEY.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTW_PJ_DTCL_WBS_KEY.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTW_PJ_DTCL_WBS_KEY.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL
