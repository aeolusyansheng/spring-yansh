-- テーブル用DDL
DROP TABLE PJTR_PJ_WBS_EVM_SUM_SNAP CASCADE CONSTRAINTS;

CREATE TABLE PJTR_PJ_WBS_EVM_SUM_SNAP
(
    WBS_ID                         NUMBER(15) NOT NULL ,
    RPT_BSDT                       DATE NOT NULL ,
    SI_DEAL_CD                     CHAR(10) NOT NULL ,
    PRJ_CD                         CHAR(10) NOT NULL ,
    PLN_STR_DT                     DATE NOT NULL ,
    PLN_END_DT                     DATE NOT NULL ,
    RSLT_STR_DT                    DATE ,
    RSLT_END_DT                    DATE ,
    FCT_CMP_DT                     DATE ,
    FCT_CMP_MNHOUR                 NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    DELAY_DAYS                     NUMBER(4) ,
    SPI                            NUMBER(6,4) DEFAULT 0.0 NOT NULL ,
    CPI                            NUMBER(6,4) DEFAULT 0.0 NOT NULL ,
    ACC_RATE                       NUMBER(6,4) DEFAULT 0.0 NOT NULL ,
    PLN_ACC_RATE                   NUMBER(6,4) DEFAULT 0.0 NOT NULL ,
    BAC                            NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    PV                             NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    EV                             NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    AC                             NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    EAC                            NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    ETC                            NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    VAC                            NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    SV                             NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    CV                             NUMBER(10,2) DEFAULT 0.0 NOT NULL ,
    BAC_PRICE                      NUMBER(12) DEFAULT 0 NOT NULL ,
    PV_PRICE                       NUMBER(12) DEFAULT 0 NOT NULL ,
    EV_PRICE                       NUMBER(12) DEFAULT 0 NOT NULL ,
    AC_PRICE                       NUMBER(12) DEFAULT 0 NOT NULL ,
    EAC_PRICE                      NUMBER(12) DEFAULT 0 NOT NULL ,
    ETC_PRICE                      NUMBER(12) DEFAULT 0 NOT NULL ,
    VAC_PRICE                      NUMBER(12) DEFAULT 0 NOT NULL ,
    SV_PRICE                       NUMBER(12) DEFAULT 0 NOT NULL ,
    CV_PRICE                       NUMBER(12) DEFAULT 0 NOT NULL ,
    FCT_FLG                        CHAR(1) NOT NULL ,
    PLN_END_AF_FLG                 CHAR(1) NOT NULL ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTR_PJ_WBS_EVM_SUM_SNAP ADD CONSTRAINT PK_PJTR_PJ_WBS_EVM_SUM_SNAP PRIMARY KEY (WBS_ID, RPT_BSDT ) ;

COMMENT ON TABLE PJTR_PJ_WBS_EVM_SUM_SNAP IS 'WBS別EVM集計SNAPトラン(ver.0.91)';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.WBS_ID IS 'WBS_ID';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.RPT_BSDT IS '報告基準日';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.SI_DEAL_CD IS 'SI案件コード';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.PRJ_CD IS 'プロジェクトコード';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.PLN_STR_DT IS '計画開始日';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.PLN_END_DT IS '計画終了日';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.RSLT_STR_DT IS '実績開始日';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.RSLT_END_DT IS '実績終了日';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.FCT_CMP_DT IS '予測完了日';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.FCT_CMP_MNHOUR IS '予測完了工数';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.DELAY_DAYS IS '遅延日数';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.SPI IS 'SPI';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.CPI IS 'CPI';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.ACC_RATE IS '達成率';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.PLN_ACC_RATE IS '計画達成率';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.BAC IS 'BAC';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.PV IS 'PV';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.EV IS 'EV';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.AC IS 'AC';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.EAC IS 'EAC';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.ETC IS 'ETC';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.VAC IS 'VAC';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.SV IS 'SV';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.CV IS 'CV';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.BAC_PRICE IS 'BAC額';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.PV_PRICE IS 'PV額';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.EV_PRICE IS 'EV額';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.AC_PRICE IS 'AC額';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.EAC_PRICE IS 'EAC額';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.ETC_PRICE IS 'ETC額';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.VAC_PRICE IS 'VAC額';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.SV_PRICE IS 'SV額';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.CV_PRICE IS 'CV額';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.FCT_FLG IS '予測フラグ';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.PLN_END_AF_FLG IS '計画終了後フラグ';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTR_PJ_WBS_EVM_SUM_SNAP.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL
CREATE INDEX IX1_PJTR_PJ_WBS_EVM_SUM_SNAP ON PJTR_PJ_WBS_EVM_SUM_SNAP (
    PRJ_CD 
);

