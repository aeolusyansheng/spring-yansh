col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- SI�Č��A���[�g�ʒm��񃏁[�N
-- 1. [ SI�Č��J�n�� ]��ǉ�
-- 2. [ SI�Č��I���� ]��ǉ�
-- 3. [ �̎Z�������ϊz ]��ǉ�
-- 4. [ �ŏI�������� ]��ǉ�
-- 5. [ �c�ƒS���Җ� ]��ǉ�

ALTER TABLE PJTW_PJ_SI_DEAL_ALNOT ADD(
    SI_DEAL_STR_DT                 DATE ,
    SI_DEAL_END_DT                 DATE ,
    PROSC_EST_PRICE                NUMBER(12) DEFAULT 0 ,
    LST_EST_COST                   NUMBER(12) DEFAULT 0 ,
    EGY_CHG_NM                     VARCHAR2(225) 
);
COMMENT ON TABLE PJTW_PJ_SI_DEAL_ALNOT IS 'SI�Č��A���[�g�ʒm��񃏁[�N(ver.1.01)';
COMMENT ON COLUMN PJTW_PJ_SI_DEAL_ALNOT.SI_DEAL_STR_DT IS 'SI�Č��J�n��';
COMMENT ON COLUMN PJTW_PJ_SI_DEAL_ALNOT.SI_DEAL_END_DT IS 'SI�Č��I����';
COMMENT ON COLUMN PJTW_PJ_SI_DEAL_ALNOT.PROSC_EST_PRICE IS '�̎Z�������ϊz';
COMMENT ON COLUMN PJTW_PJ_SI_DEAL_ALNOT.LST_EST_COST IS '�ŏI��������';
COMMENT ON COLUMN PJTW_PJ_SI_DEAL_ALNOT.EGY_CHG_NM IS '�c�ƒS���Җ�';



-- �v���W�F�N�g�A���[�g�ʒm��񃏁[�N
-- 1. [ SI�Č��R�[�h ]��ǉ�
-- 2. [ SI�Č��� ]��ǉ�
-- 3. [ �v���W�F�N�g�J�n�� ]��ǉ�
-- 4. [ �v���W�F�N�g�I���� ]��ǉ�
-- 5. [ �̎Z�������ϊz ]��ǉ�
-- 6. [ �ŏI�������� ]��ǉ�
-- 7. [ �c�ƒS���Җ� ]��ǉ�
-- 8. [ �v���W�F�N�g�}�l�[�W���� ]��ǉ�

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
COMMENT ON TABLE PJTW_PJ_PRJ_ALNOT IS '�v���W�F�N�g�A���[�g�ʒm��񃏁[�N(ver.1.02)';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.SI_DEAL_CD IS 'SI�Č��R�[�h';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.SI_DEAL_NM IS 'SI�Č���';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.PRJ_STR_DT IS '�v���W�F�N�g�J�n��';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.PRJ_END_DT IS '�v���W�F�N�g�I����';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.PROSC_EST_PRICE IS '�̎Z�������ϊz';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.LST_EST_COST IS '�ŏI��������';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.EGY_CHG_NM IS '�c�ƒS���Җ�';
COMMENT ON COLUMN PJTW_PJ_PRJ_ALNOT.PRJ_MNG_NM IS '�v���W�F�N�g�}�l�[�W����';

spool off