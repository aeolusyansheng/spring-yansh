col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- �v���W�F�N�g�g����
-- 1. [ �v���W�F�N�g�ڋq�����g�D�� ]��ǉ�
-- 2. [ �v���W�F�N�g�ڋq�����S���� ]��ǉ�
-- 3. [ �v���W�F�N�g�ڋq����TEL ]��ǉ�
-- 4. [ �v���W�F�N�g�ڋq����FAX ]��ǉ�
-- 5. [ �v���W�F�N�g�ڋq����E-MAIL ]��ǉ�

ALTER TABLE PJTR_PJ_PRJ ADD(
    PRJ_CUST_CONT_ORGAN_NM         VARCHAR2(450) ,
    PRJ_CUST_CONT_CHG              VARCHAR2(450) ,
    PRJ_CUST_CONT_TEL              VARCHAR2(45) ,
    PRJ_CUST_CONT_FAX              VARCHAR2(45) ,
    PRJ_CUST_CONT_EMAIL            VARCHAR2(240) 
);
COMMENT ON TABLE PJTR_PJ_PRJ IS '�v���W�F�N�g�g����(ver.1.02)';
COMMENT ON COLUMN PJTR_PJ_PRJ.PRJ_CUST_CONT_ORGAN_NM IS '�v���W�F�N�g�ڋq�����g�D��';
COMMENT ON COLUMN PJTR_PJ_PRJ.PRJ_CUST_CONT_CHG IS '�v���W�F�N�g�ڋq�����S����';
COMMENT ON COLUMN PJTR_PJ_PRJ.PRJ_CUST_CONT_TEL IS '�v���W�F�N�g�ڋq����TEL';
COMMENT ON COLUMN PJTR_PJ_PRJ.PRJ_CUST_CONT_FAX IS '�v���W�F�N�g�ڋq����FAX';
COMMENT ON COLUMN PJTR_PJ_PRJ.PRJ_CUST_CONT_EMAIL IS '�v���W�F�N�g�ڋq����E-MAIL';



-- ���񖾍׃g����
-- 1. [ ������ڋq�R�[�h ]��ǉ�

ALTER TABLE PJTR_PJ_CONT_DT ADD(
    BILLDEST_CUST_CD               VARCHAR2(30) 
);
COMMENT ON TABLE PJTR_PJ_CONT_DT IS '���񖾍׃g����(ver.1.01)';
COMMENT ON COLUMN PJTR_PJ_CONT_DT.BILLDEST_CUST_CD IS '������ڋq�R�[�h';

spool off
