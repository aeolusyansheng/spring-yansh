col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- プロジェクトトラン
-- 1. [ プロジェクト顧客窓口組織名 ]を追加
-- 2. [ プロジェクト顧客窓口担当者 ]を追加
-- 3. [ プロジェクト顧客窓口TEL ]を追加
-- 4. [ プロジェクト顧客窓口FAX ]を追加
-- 5. [ プロジェクト顧客窓口E-MAIL ]を追加

ALTER TABLE PJTR_PJ_PRJ ADD(
    PRJ_CUST_CONT_ORGAN_NM         VARCHAR2(450) ,
    PRJ_CUST_CONT_CHG              VARCHAR2(450) ,
    PRJ_CUST_CONT_TEL              VARCHAR2(45) ,
    PRJ_CUST_CONT_FAX              VARCHAR2(45) ,
    PRJ_CUST_CONT_EMAIL            VARCHAR2(240) 
);
COMMENT ON TABLE PJTR_PJ_PRJ IS 'プロジェクトトラン(ver.1.02)';
COMMENT ON COLUMN PJTR_PJ_PRJ.PRJ_CUST_CONT_ORGAN_NM IS 'プロジェクト顧客窓口組織名';
COMMENT ON COLUMN PJTR_PJ_PRJ.PRJ_CUST_CONT_CHG IS 'プロジェクト顧客窓口担当者';
COMMENT ON COLUMN PJTR_PJ_PRJ.PRJ_CUST_CONT_TEL IS 'プロジェクト顧客窓口TEL';
COMMENT ON COLUMN PJTR_PJ_PRJ.PRJ_CUST_CONT_FAX IS 'プロジェクト顧客窓口FAX';
COMMENT ON COLUMN PJTR_PJ_PRJ.PRJ_CUST_CONT_EMAIL IS 'プロジェクト顧客窓口E-MAIL';



-- 成約明細トラン
-- 1. [ 請求先顧客コード ]を追加

ALTER TABLE PJTR_PJ_CONT_DT ADD(
    BILLDEST_CUST_CD               VARCHAR2(30) 
);
COMMENT ON TABLE PJTR_PJ_CONT_DT IS '成約明細トラン(ver.1.01)';
COMMENT ON COLUMN PJTR_PJ_CONT_DT.BILLDEST_CUST_CD IS '請求先顧客コード';

spool off
