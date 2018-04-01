-- 2013/8/14 カラム属性変更
-- 検収売上明細トラン [ 営業従業員コード ]を必須から任意に変更
ALTER TABLE PJTR_PJ_ACCEPT_SLS_DT	MODIFY(EGY_EMP_CD DEFAULT NULL NULL);
COMMENT ON TABLE PJTR_PJ_ACCEPT_SLS_DT IS '検収売上明細トラン(ver.0.6)';

