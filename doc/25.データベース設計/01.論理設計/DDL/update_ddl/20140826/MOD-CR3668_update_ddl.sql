col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- �̎Z�������σg����
-- 1. [ �̎Z���Ϗ]�ƈ��R�[�h ]��NOT NULL�@����@NULL�֕ύX

ALTER TABLE PJTR_PJ_PROSC_EST MODIFY (PROS_EST_EMP_CD NULL);
COMMENT ON TABLE PJTR_PJ_PROSC_EST IS '�̎Z�������σg����(ver.1.01)';

spool off
