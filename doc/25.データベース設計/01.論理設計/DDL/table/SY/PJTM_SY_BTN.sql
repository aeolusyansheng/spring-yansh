-- テーブル用DDL
DROP TABLE PJTM_SY_BTN CASCADE CONSTRAINTS;

CREATE TABLE PJTM_SY_BTN
(
    DISP_ID                        CHAR(11) NOT NULL ,
    EVENT_URI                      VARCHAR2(100) NOT NULL ,
    RES_SEG                        CHAR(2) NOT NULL ,
    RES_ROLE                       VARCHAR2(18) NOT NULL ,
    VLD_STR_DT                     DATE NOT NULL ,
    VLD_END_DT                     DATE NOT NULL ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTM_SY_BTN ADD CONSTRAINT PK_PJTM_SY_BTN PRIMARY KEY (DISP_ID, EVENT_URI, RES_ROLE, VLD_STR_DT, VLD_END_DT ) ;

COMMENT ON TABLE PJTM_SY_BTN IS 'ボタンマスタ(ver.0.9)';
COMMENT ON COLUMN PJTM_SY_BTN.DISP_ID IS '画面ID';
COMMENT ON COLUMN PJTM_SY_BTN.EVENT_URI IS 'イベントURI';
COMMENT ON COLUMN PJTM_SY_BTN.RES_SEG IS '職責区分';
COMMENT ON COLUMN PJTM_SY_BTN.RES_ROLE IS '職責ロール';
COMMENT ON COLUMN PJTM_SY_BTN.VLD_STR_DT IS '有効開始日';
COMMENT ON COLUMN PJTM_SY_BTN.VLD_END_DT IS '有効終了日';
COMMENT ON COLUMN PJTM_SY_BTN.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTM_SY_BTN.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTM_SY_BTN.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTM_SY_BTN.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTM_SY_BTN.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTM_SY_BTN.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTM_SY_BTN.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTM_SY_BTN.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL
