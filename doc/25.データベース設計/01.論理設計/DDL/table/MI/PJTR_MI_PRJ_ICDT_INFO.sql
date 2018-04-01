-- テーブル用DDL
DROP TABLE PJTR_MI_PRJ_ICDT_INFO CASCADE CONSTRAINTS;

CREATE TABLE PJTR_MI_PRJ_ICDT_INFO
(
    PRJ_CD                         CHAR(10) NOT NULL ,
    LEAD_STA_FLG                   CHAR(1) NOT NULL ,
    LEAD_STA_TRM_STR_DT            DATE ,
    LEAD_STA_TRM_END_DT            DATE ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTR_MI_PRJ_ICDT_INFO ADD CONSTRAINT PK_PJTR_MI_PRJ_ICDT_INFO PRIMARY KEY (PRJ_CD ) ;

COMMENT ON TABLE PJTR_MI_PRJ_ICDT_INFO IS 'プロジェクト付帯情報トラン(ver.0.3)';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.PRJ_CD IS 'プロジェクトコード';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.LEAD_STA_FLG IS '先行着手フラグ';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.LEAD_STA_TRM_STR_DT IS '先行着手期間開始日';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.LEAD_STA_TRM_END_DT IS '先行着手期間終了日';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTR_MI_PRJ_ICDT_INFO.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL
