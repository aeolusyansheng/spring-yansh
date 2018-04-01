-- 2013/8/8 インデックス追加
-- 稼働明細トラン [ プロジェクトコード ]にインデックスを付与
CREATE INDEX IX1_PJTR_MI_OPE_DT ON PJTR_MI_OPE_DT (
    PRJ_CD 
);
COMMENT ON TABLE PJTR_MI_OPE_DT IS '稼働明細トラン(ver.0.7)';

-- WBSタスク依存関係トラン [ 先行タスクUID ]にインデックスを付与
CREATE INDEX IX2_PJTR_PJ_WBS_TSK_DEP ON PJTR_PJ_WBS_TSK_DEP (
    LEAD_TSK_UID
);
COMMENT ON TABLE PJTR_PJ_WBS_TSK_DEP IS 'WBSタスク依存関係トラン(ver.0.6)';
