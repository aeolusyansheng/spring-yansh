-- テーブル用DDL
DROP TABLE PJTM_PJ_HNYO_SEG CASCADE CONSTRAINTS;

CREATE TABLE PJTM_PJ_HNYO_SEG
(
    HNYO_SEG_CD                    CHAR(3) NOT NULL ,
    HNYO_SEG_NM                    VARCHAR2(150) ,
    HNYO_SEG_DT_CD                 VARCHAR2(30) NOT NULL ,
    HNYO_SEG_DT_NM                 VARCHAR2(150) ,
    DSP_SEQ                        NUMBER(4) ,
    NUM_VAL                        NUMBER(13,2) ,
    ALNUM                          VARCHAR2(20) ,
    CHR                            VARCHAR2(150) ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTM_PJ_HNYO_SEG ADD CONSTRAINT PK_PJTM_PJ_HNYO_SEG PRIMARY KEY (HNYO_SEG_CD, HNYO_SEG_DT_CD ) ;

COMMENT ON TABLE PJTM_PJ_HNYO_SEG IS '汎用区分マスタ(ver.0.6)';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.HNYO_SEG_CD IS '汎用区分コード';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.HNYO_SEG_NM IS '汎用区分名';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.HNYO_SEG_DT_CD IS '汎用区分明細コード';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.HNYO_SEG_DT_NM IS '汎用区分明細名';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.DSP_SEQ IS '表示順';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.NUM_VAL IS '数値';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.ALNUM IS '英数字';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.CHR IS '文字';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTM_PJ_HNYO_SEG.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL
