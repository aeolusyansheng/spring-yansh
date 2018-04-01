col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- WBSタスクトラン
-- 1. [AC]を削除
-- 2. [AC額]を削除
ALTER TABLE PJTR_PJ_WBS_TSK DROP (AC , AC_PRICE);
COMMENT ON TABLE PJTR_PJ_WBS_TSK IS 'WBSタスクトラン(ver.1.01)';


-- WBSタスクリソーストラン
--1. [AC]を削除
--2. [AC額]を削除
ALTER TABLE PJTR_PJ_WBS_TSK_RESRC DROP (AC , AC_PRICE);
COMMENT ON TABLE PJTR_PJ_WBS_TSK_RESRC IS 'WBSタスクリソーストラン(ver.1.01)';


-- 締処理用社員・派遣単価マスタ
--1. テーブル廃止のためコメントを変更
COMMENT ON TABLE PJTM_PJ_STF_DP_UNTPRICE IS '【廃止】締処理用社員・派遣単価マスタ(ver.0.3)';

-- 発注先別工数・金額トラン
--1. テーブル廃止のためコメントを変更
COMMENT ON TABLE PJTR_PJ_SUPP_MNHOUR_PRICE IS '【廃止】発注先別工数・金額トラン(ver.0.3)';

-- タスク別PVトラン
--1. テーブル廃止のためコメントを変更
COMMENT ON TABLE PJTR_PJ_TSK_DIST_PV IS '【廃止】タスク別PVトラン(ver.0.8)';

-- タスクリソース別PVトラン
--1. テーブル廃止のためコメントを変更
COMMENT ON TABLE PJTR_PJ_TSK_RESRC_DIST_PV IS '【廃止】タスクリソース別PVトラン(ver.0.6)';

-- プロジェクトコストトラン
--1. テーブル廃止のためコメントを変更
COMMENT ON TABLE PJTR_PJ_PRJ_CST IS '【廃止】プロジェクトコストトラン(ver.0.3)';

-- 前回報告締WBSタスクSNAPトラン
--1. テーブル廃止のためコメントを変更
COMMENT ON TABLE PJTR_PJ_PREV_WBS_TSK_SNAP IS '【廃止】前回報告締WBSタスクSNAPトラン(ver.0.8)';

-- 今回報告締WBSタスクSNAPトラン
--1. テーブル廃止のためコメントを変更
COMMENT ON TABLE PJTR_PJ_THIS_WBS_TSK_SNAP IS '【廃止】今回報告締WBSタスクSNAPトラン(ver.0.8)';

-- WBS別EVM集計SNAPトラン
--1. テーブル廃止のためコメントを変更
COMMENT ON TABLE PJTR_PJ_WBS_EVM_SUM_SNAP IS '【廃止】WBS別EVM集計SNAPトラン(ver.0.91)';

-- プロジェクト別EVM集計SNAPトラン
--1. テーブル廃止のためコメントを変更
COMMENT ON TABLE PJTR_PJ_PRJ_EVM_SUM_SNAP IS '【廃止】プロジェクト別EVM集計SNAPトラン(ver.1.01)';

spool off