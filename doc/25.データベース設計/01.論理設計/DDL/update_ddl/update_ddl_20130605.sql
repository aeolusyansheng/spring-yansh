-- 2013/6/5 属性変更
-- 稼働入力履歴トラン ユニークキーを削除
COMMENT ON TABLE PJTR_MI_OPENT_HIST IS '稼働入力履歴トラン(ver.0.7)';
DROP INDEX UX_PJTR_MI_OPENT_HIST;
