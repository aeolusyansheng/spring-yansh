col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- プロジェクトトラン
-- 1. [ 最新進捗入力日 ]を追加
-- 2. [ 最新進捗入力従業員コード ]を追加

ALTER TABLE PJTR_PJ_PRJ ADD(
    LAT_PROG_ENT_DT         DATE,
    LAT_PROG_ENT_EMP_CD              VARCHAR2(30)
);
COMMENT ON TABLE PJTR_PJ_PRJ IS 'プロジェクトトラン(ver.1.03)';
COMMENT ON COLUMN PJTR_PJ_PRJ.LAT_PROG_ENT_DT IS '最新進捗入力日';
COMMENT ON COLUMN PJTR_PJ_PRJ.LAT_PROG_ENT_EMP_CD IS '最新進捗入力従業員コード';

spool off
