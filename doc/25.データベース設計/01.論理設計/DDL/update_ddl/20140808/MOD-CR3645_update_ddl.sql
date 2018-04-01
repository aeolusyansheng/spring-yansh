col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- SI案件アラート通知情報ワーク
-- 1. [ SI案件開始日 ]を追加
-- 2. [ SI案件終了日 ]を追加
-- 3. [ 採算原価見積額 ]を追加
-- 4. [ 最終見込原価 ]を追加
-- 5. [ 営業担当者名 ]を追加

ALTER TABLE PJTW_PJ_SI_DEAL_ALNOT ADD(
    SI_DEAL_STR_DT                 DATE ,
    SI_DEAL_END_DT                 DATE ,
    PROSC_EST_PRICE                NUMBER(12) DEFAULT 0 ,
    LST_EST_COST                   NUMBER(12) DEFAULT 0 ,
    EGY_CHG_NM                     VARCHAR2(225) 
);
COMMENT ON TABLE PJTW_PJ_SI_DEAL_ALNOT IS 'SI案件アラート通知情報ワーク(ver.1.01)';
COMMENT ON COLUMN PJTW_PJ_SI_DEAL_ALNOT.SI_DEAL_STR_DT IS 'SI案件開始日';
COMMENT ON COLUMN PJTW_PJ_SI_DEAL_ALNOT.SI_DEAL_END_DT IS 'SI案件終了日';
COMMENT ON COLUMN PJTW_PJ_SI_DEAL_ALNOT.PROSC_EST_PRICE IS '採算原価見積額';
COMMENT ON COLUMN PJTW_PJ_SI_DEAL_ALNOT.LST_EST_COST IS '最終見込原価';
COMMENT ON COLUMN PJTW_PJ_SI_DEAL_ALNOT.EGY_CHG_NM IS '営業担当者名';



-- プロジェクトアラート通知情報ワーク
-- 1. [ SI案件コード ]を追加
-- 2. [ SI案件名 ]を追加
-- 3. [ プロジェクト開始日 ]を追加
-- 4. [ プロジェクト終了日 ]を追加
-- 5. [ 採算原価見積額 ]を追加
-- 6. [ 最終見込原価 ]を追加
-- 7. [ 営業担当者名 ]を追加
-- 8. [ プロジェクトマネージャ名 ]を追加

ALTER TABLE PJTW_PJ_PRJ_ALNOT ADD(
    SI_DEAL_CD                     CHAR(10) ,
    SI_DEAL_NM                     VARCHAR2(150) ,
    PRJ_STR_DT                     DATE ,
    PRJ_END_DT                     DATE ,
    PROSC_EST_PRICE                NUMBER(12) DEFAULT 0 ,
    LST_EST_COST                   NUMBER(12) DEFAULT 0 ,
    EGY_CHG_NM                     VARCHAR2(225) ,
    PRJ_MNG_NM                     VARCHAR2(225) 
);
COMMENT ON TABLE PJTW_PJ_PRJ_ALNOT IS 'プロジェクトアラート通知情報ワーク(ver.1.02)';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.SI_DEAL_CD IS 'SI案件コード';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.SI_DEAL_NM IS 'SI案件名';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.PRJ_STR_DT IS 'プロジェクト開始日';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.PRJ_END_DT IS 'プロジェクト終了日';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.PROSC_EST_PRICE IS '採算原価見積額';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.LST_EST_COST IS '最終見込原価';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.EGY_CHG_NM IS '営業担当者名';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.PRJ_MNG_NM IS 'プロジェクトマネージャ名';

spool off