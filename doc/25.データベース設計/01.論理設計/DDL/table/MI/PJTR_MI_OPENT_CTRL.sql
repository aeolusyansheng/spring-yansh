-- テーブル用DDL
DROP TABLE PJTR_MI_OPENT_CTRL CASCADE CONSTRAINTS;

CREATE TABLE PJTR_MI_OPENT_CTRL
(
    ENT_PSN_ID                     NUMBER(10) NOT NULL ,
    WEEK_STR_DT                    DATE NOT NULL ,
    OPE_ENT_CTRL_SEQ               NUMBER(4) NOT NULL ,
    TSK_ID                         NUMBER(15) NOT NULL ,
    TSK_MK_SYS_SEG                 CHAR(1) DEFAULT '0' NOT NULL ,
    OPE_DT_SEQ                     NUMBER(4) NOT NULL ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTR_MI_OPENT_CTRL ADD CONSTRAINT PK_PJTR_MI_OPENT_CTRL PRIMARY KEY (ENT_PSN_ID, WEEK_STR_DT, OPE_ENT_CTRL_SEQ ) ;

COMMENT ON TABLE PJTR_MI_OPENT_CTRL IS '稼働入力コントロールトラン(ver.0.5)';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.ENT_PSN_ID IS '入力者ID';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.WEEK_STR_DT IS '週開始日';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.OPE_ENT_CTRL_SEQ IS '稼働入力コントロール連番';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.TSK_ID IS 'タスクID';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.TSK_MK_SYS_SEG IS 'タスク作成システム区分';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.OPE_DT_SEQ IS '稼働明細連番';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTR_MI_OPENT_CTRL.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL
