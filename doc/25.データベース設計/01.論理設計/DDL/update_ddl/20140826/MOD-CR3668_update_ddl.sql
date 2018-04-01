col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- 採算原価見積トラン
-- 1. [ 採算見積従業員コード ]をNOT NULL　から　NULLへ変更

ALTER TABLE PJTR_PJ_PROSC_EST MODIFY (PROS_EST_EMP_CD NULL);
COMMENT ON TABLE PJTR_PJ_PROSC_EST IS '採算原価見積トラン(ver.1.01)';

spool off
