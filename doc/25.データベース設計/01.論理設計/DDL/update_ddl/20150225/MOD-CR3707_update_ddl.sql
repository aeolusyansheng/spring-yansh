col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- 従業員プロファイルトラン
-- 1.新規テーブル作成
CREATE TABLE PJTR_PJ_EMP_PRFL
(
    EMP_ID                         NUMBER(10) NOT NULL ,
    SEG_DT_CD                      VARCHAR2(30) NOT NULL ,
    VAL                            VARCHAR2(150) ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTR_PJ_EMP_PRFL ADD CONSTRAINT PK_PJTR_PJ_EMP_PRFL PRIMARY KEY (EMP_ID, SEG_DT_CD ) ;

COMMENT ON TABLE PJTR_PJ_EMP_PRFL IS '従業員プロファイルトラン(ver.1.0)';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.EMP_ID IS '従業員ID';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.SEG_DT_CD IS '区分明細コード';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.VAL IS '値';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.VER_NO IS 'バージョン番号';

-- ユニークインデックス用DDL

-- インデックス用DDL

--PROMPT 従業員プロファイルトラン < PJ_MOD.PJTR_PJ_EMP_PRFL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_EMP_PRFL
                      FOR PJNAVI.PJTR_PJ_EMP_PRFL
;

--PROMPT 従業員プロファイルトラン < PJ_REF.PJTR_PJ_EMP_PRFL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_REF.PJTR_PJ_EMP_PRFL
                      FOR PJNAVI.PJTR_PJ_EMP_PRFL
;


spool off