-- 2013/8/23 インデックス追加
-- 稼働入力履歴トラン [ 入力者ID ]、[ 稼働日 ]、[ 稼働ステータス ]に複合インデックスを付与
CREATE INDEX IX1_PJTR_MI_OPENT_HIST ON PJTR_MI_OPENT_HIST (
    ENT_PSN_ID, OPE_DT, OPE_STATS
);
COMMENT ON TABLE PJTR_MI_OPENT_HIST IS '稼働入力履歴トラン(ver.0.8)';
