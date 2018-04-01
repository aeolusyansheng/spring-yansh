-- 2013/10/21 カラム追加
-- eTime実働日情報トラン
-- 1. [ バッチ処理済フラグ ]を追加
ALTER TABLE PJTR_MI_ETIME_WRKDT_INFO	ADD(BAT_PROC_DONE_FLG CHAR(1) DEFAULT '0' NOT NULL);
COMMENT ON COLUMN PJTR_MI_ETIME_WRKDT_INFO.BAT_PROC_DONE_FLG IS 'バッチ処理済フラグ';
-- 2. [ バッチ処理済フラグ ]、[ 勤務日(降順) ]に複合インデックスを付与
CREATE INDEX IX1_PJTR_MI_ETIME_WRKDT_INFO ON PJTR_MI_ETIME_WRKDT_INFO (
    BAT_PROC_DONE_FLG, WK_DT DESC
);
COMMENT ON TABLE PJTR_MI_ETIME_WRKDT_INFO IS 'eTime実働日情報トラン(ver.0.5)';

-- eTime実働日情報集計トラン テーブルの削除
DROP TABLE PJTR_MI_ETIME_WRKDT_SUM CASCADE CONSTRAINTS;

                                                                                                                                                                                                                                                                            