-- テーブル用DDL
DROP TABLE PJTR_PJ_WBS_TSK_TPL_DEP CASCADE CONSTRAINTS;

CREATE TABLE PJTR_PJ_WBS_TSK_TPL_DEP
(
    TSK_UID                        NUMBER(15) NOT NULL ,
    LEAD_TSK_UID                   NUMBER(15) NOT NULL ,
    EMP_CD                         VARCHAR2(30) NOT NULL ,
    DEP_SEG                        CHAR(1) NOT NULL ,
    WBS_ID                         NUMBER(15) NOT NULL ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTR_PJ_WBS_TSK_TPL_DEP ADD CONSTRAINT PK_PJTR_PJ_WBS_TSK_TPL_DEP PRIMARY KEY (TSK_UID, LEAD_TSK_UID, EMP_CD ) ;

COMMENT ON TABLE PJTR_PJ_WBS_TSK_TPL_DEP IS 'WBSタスクテンプレート依存関係トラン(ver.0.5)';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.TSK_UID IS 'タスクUID';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.LEAD_TSK_UID IS '先行タスクUID';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.EMP_CD IS '従業員コード';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.DEP_SEG IS '依存関係区分';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.WBS_ID IS 'WBS_ID';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTR_PJ_WBS_TSK_TPL_DEP.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL
CREATE INDEX IX1_PJTR_PJ_WBS_TSK_TPL_DEP ON PJTR_PJ_WBS_TSK_TPL_DEP (
    EMP_CD 
);

CREATE INDEX IX2_PJTR_PJ_WBS_TSK_TPL_DEP ON PJTR_PJ_WBS_TSK_TPL_DEP (
    WBS_ID 
);

