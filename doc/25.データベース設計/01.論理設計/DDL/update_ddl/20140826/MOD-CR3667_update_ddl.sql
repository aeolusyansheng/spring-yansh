col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- �v���W�F�N�g�g����
-- 1. [ �ŐV�i�����͓� ]��ǉ�
-- 2. [ �ŐV�i�����͏]�ƈ��R�[�h ]��ǉ�

ALTER TABLE PJTR_PJ_PRJ ADD(
    LAT_PROG_ENT_DT         DATE,
    LAT_PROG_ENT_EMP_CD              VARCHAR2(30)
);
COMMENT ON TABLE PJTR_PJ_PRJ IS '�v���W�F�N�g�g����(ver.1.03)';
COMMENT ON COLUMN PJTR_PJ_PRJ.LAT_PROG_ENT_DT IS '�ŐV�i�����͓�';
COMMENT ON COLUMN PJTR_PJ_PRJ.LAT_PROG_ENT_EMP_CD IS '�ŐV�i�����͏]�ƈ��R�[�h';

spool off
