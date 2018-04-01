-- 2013/8/14 カラム属性変更
-- ジョブ間パラメータ保持トラン [ パラメータキー ]の属性を[ VARCHAR2(200) ]に変更
ALTER TABLE PJTR_FW_INTERJOB_PARAM	MODIFY(PARAM_KEY VARCHAR2(200));
COMMENT ON TABLE PJTR_FW_INTERJOB_PARAM IS 'ジョブ間パラメータ保持トラン(ver.0.2)';

