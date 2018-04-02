--NextMI�v���W�F�N�g(PJTR_MI_NEXTMI_PRJ)��
--PJNavi�v���W�F�N�g(PJTR_PJ_PRJ)�̘A��VIEW
--PK:�v���W�F�N�g�R�[�h

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

COMMENT ON TABLE PJVR_MI_PRJ_UNI IS '�v���W�F�N�g�g�����A���r���[(ver.1.01)';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_NM IS '�v���W�F�N�g��';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_TYP IS '�v���W�F�N�g�^�C�v';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_STATS IS '�v���W�F�N�g�X�e�[�^�X';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.SI_DEAL_CD IS 'SI�Č��R�[�h';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_OWN_ORGAN_CD IS '�v���W�F�N�g���L�g�D�R�[�h';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_EGY_EMP_CD IS '�v���W�F�N�g�c�Ə]�ƈ��R�[�h';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PROP_PRJ_FLG IS '��ăv���W�F�N�g�t���O';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.FLAW_PRJ_FLG IS '���r�v���W�F�N�g�t���O';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.LEAD_STA_FLG IS '��s����t���O';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.LEAD_STA_TRM_STR_DT IS '��s������ԊJ�n��';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.LEAD_STA_TRM_END_DT IS '��s������ԏI����';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.PRJ_CUST_CD IS '�v���W�F�N�g�ڋq�����R�[�h';
COMMENT ON COLUMN PJVR_MI_PRJ_UNI.TPL_SNM IS '�e���v���[�g����';
