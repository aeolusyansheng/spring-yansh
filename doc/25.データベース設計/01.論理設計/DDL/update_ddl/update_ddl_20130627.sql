-- 2013/6/27 カラム属性変更
-- 稼働入力テンプレートトラン [ 稼働入力テンプレート名称 ]の属性を[ VARCHAR2(180) ]に変更
ALTER TABLE PJTR_MI_OPENT_TPL	MODIFY(OPE_ENT_TPL_NM VARCHAR2(180));
COMMENT ON TABLE PJTR_MI_OPENT_TPL IS '稼働入力テンプレートトラン(ver.0.6)';
