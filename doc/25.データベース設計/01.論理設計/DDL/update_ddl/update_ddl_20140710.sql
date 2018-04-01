-- 2014/6/13 カラム追加
-- Next.MIテンプレートタスクマスタ [ 作業タイプID ]を追加
ALTER TABLE PJTM_PJ_NEXTMI_TPL_TSK	ADD(WORK_TYP_ID NUMBER(15,0));
COMMENT ON TABLE PJTM_PJ_NEXTMI_TPL_TSK IS 'Next.MIテンプレートタスクマスタ(ver.1.01)';
COMMENT ON COLUMN PJTM_PJ_NEXTMI_TPL_TSK.WORK_TYP_ID IS '作業タイプID';

-- 2014/6/13 カラム追加
-- プロジェクトNext.MIタスクトラン [ 作業タイプID ]を追加
ALTER TABLE PJTR_PJ_PRJ_NEXTMI_TSK	ADD(WORK_TYP_ID NUMBER(15,0));
COMMENT ON TABLE PJTR_PJ_PRJ_NEXTMI_TSK IS 'プロジェクトNext.MIタスクトラン(ver.1.01)';
COMMENT ON COLUMN PJTR_PJ_PRJ_NEXTMI_TSK.WORK_TYP_ID IS '作業タイプID';

-- 2014/6/13 カラム追加
-- NextMIプロジェクトトラン [ 顧客コード ]と[ テンプレートID ]を追加
ALTER TABLE PJTR_MI_NEXTMI_PRJ	ADD(CUST_CD VARCHAR2(30));
ALTER TABLE PJTR_MI_NEXTMI_PRJ	ADD(TPL_ID NUMBER(15,0));
COMMENT ON TABLE PJTR_MI_NEXTMI_PRJ IS 'NextMIプロジェクトトラン(ver.1.01)';
COMMENT ON COLUMN PJTR_MI_NEXTMI_PRJ.CUST_CD IS '顧客コード';
COMMENT ON COLUMN PJTR_MI_NEXTMI_PRJ.TPL_ID IS 'テンプレートID';

-- 2014/6/13 カラム追加
-- NextMIプロジェクトタスクトラン [ 作業タイプID ]を追加
ALTER TABLE PJTR_MI_NEXTMI_PRJ_TSK	ADD(WORK_TYP_ID NUMBER(15,0));
COMMENT ON TABLE PJTR_MI_NEXTMI_PRJ_TSK IS 'NextMIプロジェクトタスクトラン(ver.1.01)';
COMMENT ON COLUMN PJTR_MI_NEXTMI_PRJ_TSK.WORK_TYP_ID IS '作業タイプID';



--NextMIプロジェクト(PJTR_MI_NEXTMI_PRJ)と
--PJNaviプロジェクト(PJTR_PJ_PRJ)の連結VIEW
--PK:プロジェクトコード

CREATE OR REPLACE VIEW PJVR_MI_PRJ_UNI AS
SELECT NEXTMI_PRJ.PRJ_CD                    AS "PRJ_CD"
     , NEXTMI_PRJ.PRJ_NM                    AS "PRJ_NM"
     , NEXTMI_PRJ.PRJ_TYP                   AS "PRJ_TYP"
     , NEXTMI_PRJ.PRJ_STATS                 AS "PRJ_STATS"
     , NULL                                 AS "SI_DEAL_CD"
     , NEXTMI_PRJ.PRJ_OWN_ORGAN_CD          AS "PRJ_OWN_ORGAN_CD"
     , NEXTMI_PRJ.PRJ_EGY_EMP_CD            AS "PRJ_EGY_EMP_CD"
     , '0'                                  AS "PROP_PRJ_FLG"
     , '0'                                  AS "FLAW_PRJ_FLG"
     , PRJ_ICDT_INFO.LEAD_STA_FLG           AS "LEAD_STA_FLG"
     , PRJ_ICDT_INFO.LEAD_STA_TRM_STR_DT    AS "LEAD_STA_TRM_STR_DT"
     , PRJ_ICDT_INFO.LEAD_STA_TRM_END_DT    AS "LEAD_STA_TRM_END_DT"
     , NEXTMI_PRJ.CUST_CD                   AS "PRJ_CUST_CD"
     , TPL_SEG.HNYO_SEG_DT_NM               AS "TPL_SNM"
  FROM PJTR_MI_NEXTMI_PRJ       NEXTMI_PRJ
     , PJTR_MI_PRJ_ICDT_INFO    PRJ_ICDT_INFO
     , PJTM_PJ_HNYO_SEG         TPL_SEG
 WHERE NEXTMI_PRJ.PRJ_CD = PRJ_ICDT_INFO.PRJ_CD(+)
   AND NEXTMI_PRJ.TPL_ID = TPL_SEG.HNYO_SEG_DT_CD(+)
   AND TPL_SEG.HNYO_SEG_CD(+) = '074'
UNION ALL
SELECT PRJ.PRJ_CD                           AS "PRJ_CD"
     , PRJ.PRJ_NM                           AS "PRJ_NM"
     , PRJ_TPL.PRJ_TYP_NM                   AS "PRJ_TYP"
     , PRJ.PRJ_STATS_SEG                    AS "PRJ_STATS"
     , PRJ.SI_DEAL_CD                       AS "SI_DEAL_CD"
     , PRJ.PRJ_DEV_DEPT_CD                  AS "PRJ_OWN_ORGAN_CD"
     , PRJ.PRJ_EGY_EMP_CD                   AS "PRJ_EGY_EMP_CD"
     , PRJ.PROP_PRJ_FLG                     AS "PROP_PRJ_FLG"
     , PRJ.FLAW_PRJ_FLG                     AS "FLAW_PRJ_FLG"
     , PRJ_ICDT_INFO.LEAD_STA_FLG           AS "LEAD_STA_FLG"
     , PRJ_ICDT_INFO.LEAD_STA_TRM_STR_DT    AS "LEAD_STA_TRM_STR_DT"
     , PRJ_ICDT_INFO.LEAD_STA_TRM_END_DT    AS "LEAD_STA_TRM_END_DT"
     , PRJ.PRJ_CUST_CD                      AS "PRJ_CUST_CD"
     , PRJ_TPL.TPL_SNM                      AS "TPL_SNM"
  FROM PJTR_PJ_PRJ              PRJ
     , PJTR_MI_PRJ_ICDT_INFO    PRJ_ICDT_INFO
     , PJTM_PJ_PRJ_TPL          PRJ_TPL
 WHERE PRJ.PRJ_CD   = PRJ_ICDT_INFO.PRJ_CD(+)
   AND PRJ.PRJ_TPL  = PRJ_TPL.TPL_NO
;

COMMENT ON TABLE PJVR_MI_PRJ_UNI IS 'プロジェクトトラン連結ビュー(ver.1.01)';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_CD IS 'プロジェクトコード';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_NM IS 'プロジェクト名';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_TYP IS 'プロジェクトタイプ';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_STATS IS 'プロジェクトステータス';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.SI_DEAL_CD IS 'SI案件コード';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_OWN_ORGAN_CD IS 'プロジェクト所有組織コード';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_EGY_EMP_CD IS 'プロジェクト営業従業員コード';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PROP_PRJ_FLG IS '提案プロジェクトフラグ';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.FLAW_PRJ_FLG IS '瑕疵プロジェクトフラグ';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.LEAD_STA_FLG IS '先行着手フラグ';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.LEAD_STA_TRM_STR_DT IS '先行着手期間開始日';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.LEAD_STA_TRM_END_DT IS '先行着手期間終了日';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_CUST_CD IS 'プロジェクト顧客取引先コード';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.TPL_SNM IS 'テンプレート略称';





--NextMIタスク(PJTR_MI_NEXTMI_PRJ_TSK)と
--PJNaviタスク(PJTR_PJ_WBS_TSK)の連結VIEW
--PK:プロジェクト作成システム区分,タスクID

CREATE OR REPLACE VIEW PJVR_MI_TSK_UNI AS
SELECT '1'                                      AS TSK_MK_SYS_SEG  -- 0：PJNavi,1：NextMI
     , NEXTMI_PRJ_TSK.TSK_ID                    AS TSK_ID
     , NEXTMI_PRJ_TSK.PRJ_CD                    AS PRJ_CD
     , NEXTMI_PRJ_TSK.TSK_NO                    AS TSK_NO
     , NEXTMI_PRJ_TSK.NEXTMI_TSK_NM             AS TSK_NM
     , NEXTMI_PRJ_TSK.TSK_NO                    AS NEXTMI_TSK_NO
     , NEXTMI_PRJ_TSK.NEXTMI_TSK_NM             AS NEXTMI_TSK_NM
     , NULL                                     AS WBS_ID
     , NEXTMI_PRJ_TSK.TSK_VLD_STR_DT            AS TSK_VLD_STR_DT
     , NEXTMI_PRJ_TSK.TSK_VLD_END_DT            AS TSK_VLD_END_DT
     , NEXTMI_PRJ_TSK.LEVY_POS_FLG              AS LEVY_POS_FLG
     , NEXTMI_PRJ_TSK.SV_TYP_CD                 AS SV_TYP_CD
     , NEXTMI_PRJ_TSK.TSK_ENF_ORGAN_ID          AS TSK_ENF_ORGAN_ID
     , NEXTMI_PRJ_TSK.FORM_SEG_NM               AS FORM_SEG_NM
     , NEXTMI_PRJ_TSK.TRAN_MNG_INFO_VLD_STR_DT  AS TRAN_MNG_INFO_VLD_STR_DT
     , NEXTMI_PRJ_TSK.TRAN_MNG_INFO_VLD_END_DT  AS TRAN_MNG_INFO_VLD_END_DT
     , NULL                                     AS STR_PLN_DT
     , NULL                                     AS END_PLN_DT
     , NULL                                     AS ACC_MES_KND_CD
     , NULL                                     AS SHIP_MNG_NO
     , '1'                                      AS OPENT_POS_FLG
     , NEXTMI_PRJ_TSK.VAC_FLG                   AS VAC_FLG
     , NEXTMI_PRJ_TSK.WORK_TYP_ID               AS WORK_TYP_ID
  FROM PJTR_MI_NEXTMI_PRJ_TSK NEXTMI_PRJ_TSK
 WHERE NEXTMI_PRJ_TSK.DEL_FLG = '0'
UNION ALL
SELECT '0'                                      AS TSK_MK_SYS_SEG  -- 0：PJNavi,1：NextMI
     , WBS_TSK.TSK_UID                          AS TSK_ID
     , WBS_TSK.PRJ_CD                           AS PRJ_CD
     , WBS_TSK.TSK_CD                           AS TSK_NO
     , WBS_TSK.TSK_NM                           AS TSK_NM
     , PRJ_NEXTMI_TSK.NEXTMI_TSK_NO             AS NEXTMI_TSK_NO
     , PRJ_NEXTMI_TSK.NEXTMI_TSK_NM             AS NEXTMI_TSK_NM
     , WBS_TSK.WBS_ID                           AS WBS_ID
     , PRJ.PRJ_STR_DT                           AS TSK_VLD_STR_DT
     , PRJ.PRJ_END_DT                           AS TSK_VLD_END_DT
     , PRJ_NEXTMI_TSK.LEVY_POS_FLG              AS LEVY_POS_FLG
     , PRJ_NEXTMI_TSK.SV_TYP_CD                 AS SV_TYP_CD
     , PRJ_NEXTMI_TSK.TSK_ENF_ORGAN_ID          AS TSK_ENF_ORGAN_ID
     , NULL                                     AS FORM_SEG_NM
     , PRJ.PRJ_STR_DT                           AS TRAN_MNG_INFO_VLD_STR_DT
     , PRJ.PRJ_END_DT                           AS TRAN_MNG_INFO_VLD_END_DT
     , WBS_TSK.STR_PLN_DT                       AS STR_PLN_DT
     , WBS_TSK.END_PLN_DT                       AS END_PLN_DT
     , WBS_TSK.ACC_MES_KND_CD                   AS ACC_MES_KND_CD
     , WBS_TSK.SHIP_MNG_NO                      AS SHIP_MNG_NO
     , CASE
         WHEN WBS_TSK.SHIP_TSK_FLG = '1'
           OR PRJ_NEXTMI_TSK.PRJ_CD IS NULL
         THEN '0'
         ELSE '1'
       END                                      AS OPENT_POS_FLG
     , '0'                                      AS VAC_FLG
     , PRJ_NEXTMI_TSK.WORK_TYP_ID               AS WORK_TYP_ID
  FROM PJTR_PJ_WBS_TSK WBS_TSK
     , PJTR_PJ_PRJ PRJ
     , PJTR_PJ_PRJ_NEXTMI_TSK PRJ_NEXTMI_TSK
 WHERE WBS_TSK.PRJ_CD = PRJ.PRJ_CD
   AND WBS_TSK.PRJ_CD = PRJ_NEXTMI_TSK.PRJ_CD(+)
   AND WBS_TSK.NEXTMI_FLNK_TSK_ID = PRJ_NEXTMI_TSK.NEXTMI_FLNK_TSK_ID(+)
   AND WBS_TSK.TYP = '70'            --タスクのみ抽出
   AND WBS_TSK.PRJ_EXCEPT_SEG = '0'  --プロジェクト対象
   AND WBS_TSK.DEL_FLG = '0'
;

COMMENT ON TABLE PJVR_MI_TSK_UNI IS 'タスクトラン連結ビュー(ver.1.01)'; 
COMMENT ON COLUMN PJVR_MI_TSK_UNI.TSK_MK_SYS_SEG IS 'タスク作成システム区分';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.TSK_ID IS 'タスクID';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.PRJ_CD IS 'プロジェクトコード';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.TSK_NO IS 'タスク番号';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.TSK_NM IS 'タスク名';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.NEXTMI_TSK_NO IS 'NextMIタスク番号';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.NEXTMI_TSK_NM IS 'NextMIタスク名';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.WBS_ID IS 'WBS_ID';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.TSK_VLD_STR_DT IS 'タスク有効開始日';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.TSK_VLD_END_DT IS 'タスク有効終了日';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.LEVY_POS_FLG IS '賦課可能フラグ';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.SV_TYP_CD IS 'サービスタイプコード';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.TSK_ENF_ORGAN_ID IS 'タスク実施組織ID';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.FORM_SEG_NM IS '形態別区分名称';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.TRAN_MNG_INFO_VLD_STR_DT IS '取引管理情報有効開始日';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.TRAN_MNG_INFO_VLD_END_DT IS '取引管理情報有効終了日';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.STR_PLN_DT IS '開始予定日';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.END_PLN_DT IS '終了予定日';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.ACC_MES_KND_CD IS '達成計測種別コード';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.SHIP_MNG_NO IS '発注管理番号';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.OPENT_POS_FLG IS '稼働入力可能フラグ';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.VAC_FLG IS '休暇フラグ';
COMMENT ON COLUMN PJVR_MI_TSK_UNI.WORK_TYP_ID IS '作業タイプID';

