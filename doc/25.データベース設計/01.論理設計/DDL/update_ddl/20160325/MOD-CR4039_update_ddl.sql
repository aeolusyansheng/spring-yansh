col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- �W���u�ԃp�����[�^�ێ��g����
-- 1. [ �s�ԍ� ]�������ύX�@number(3)��number(6)

ALTER TABLE PJTR_FW_INTERJOB_PARAM MODIFY( LINE_NO NUMBER(6));
COMMENT ON TABLE PJTR_FW_INTERJOB_PARAM IS '�W���u�ԃp�����[�^�ێ��g����(ver.1.01)';

spool off

