-- テーブル用DDL
DROP TABLE PJTW_PJ_PROSC_EST_ALNOT CASCADE CONSTRAINTS;

CREATE TABLE PJTW_PJ_PROSC_EST_ALNOT
(
    ALERT_SEG                      CHAR(1) NOT NULL ,
    PRJ_CD                         CHAR(10) NOT NULL ,
    EMP_CD                         VARCHAR2(30) NOT NULL ,
    PRJ_NM                         VARCHAR2(90) NOT NULL ,
    EMP_EMAIL                      VARCHAR2(240) NOT NULL ,
    PRJ_STR_DT                     DATE ,
    PRJ_END_DT                     DATE ,
    PROSC_EST_PRICE                NUMBER(12) DEFAULT 0 NOT NULL ,
    PLN_CST_BAC_PRICE              NUMBER(12) DEFAULT 0 NOT NULL ,
    PLN_CST_BLKODR_TTL_PRICE       NUMBER(12) DEFAULT 0 NOT NULL ,
    PLN_CST_EXP_TTL_PRICE          NUMBER(12) DEFAULT 0 NOT NULL ,
    PLN_CST_CON_PRICE              NUMBER(12) ,
    PRJ_MNG_EMP_CD                 VARCHAR2(30) ,
    PRJ_MNG_NM                     VARCHAR2(225) ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTW_PJ_PROSC_EST_ALNOT ADD CONSTRAINT PK_PJTW_PJ_PROSC_EST_ALNOT PRIMARY KEY (ALERT_SEG, PRJ_CD, EMP_CD ) ;

COMMENT ON TABLE PJTW_PJ_PROSC_EST_ALNOT IS '採算原価見積アラート通知情報ワーク(ver.1.0)';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.ALERT_SEG IS 'アラート区分';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PRJ_CD IS 'プロジェクトコード';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.EMP_CD IS '従業員コード';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PRJ_NM IS 'プロジェクト名';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.EMP_EMAIL IS '従業員メールアドレス';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PRJ_STR_DT IS 'プロジェクト開始日';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PRJ_END_DT IS 'プロジェクト終了日';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PROSC_EST_PRICE IS '採算原価見積額';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PLN_CST_BAC_PRICE IS '計画コストBAC額';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PLN_CST_BLKODR_TTL_PRICE IS '計画コスト一括発注総額';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PLN_CST_EXP_TTL_PRICE IS '計画コスト経費総額';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PLN_CST_CON_PRICE IS '計画コストコンティンジェンシー額';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PRJ_MNG_EMP_CD IS 'プロジェクトマネージャ従業員コード';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.PRJ_MNG_NM IS 'プロジェクトマネージャ名';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTW_PJ_PROSC_EST_ALNOT.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL
