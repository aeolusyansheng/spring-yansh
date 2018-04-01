-- 2013/12/6 カラム属性修正、カラム追加
-- SI案件アラート通知情報ワーク
-- 1. [ 遅延日数 ]を必須から任意に変更
ALTER TABLE PJTW_PJ_SI_DEAL_ALNOT	MODIFY(DELAY_DAYS DEFAULT NULL NULL);
-- 2. [ SPI ]、[ CPI ]を追加
ALTER TABLE PJTW_PJ_SI_DEAL_ALNOT	ADD(SPI NUMBER(6,4) DEFAULT 0.0 NOT NULL);
ALTER TABLE PJTW_PJ_SI_DEAL_ALNOT	ADD(CPI NUMBER(6,4) DEFAULT 0.0 NOT NULL);
COMMENT ON TABLE PJTW_PJ_SI_DEAL_ALNOT IS 'SI案件アラート通知情報ワーク(ver.0.2)';
-- プロジェクトアラート通知情報ワーク
-- 1. [ 遅延日数 ]を必須から任意に変更
ALTER TABLE PJTW_PJ_PRJ_ALNOT	MODIFY(DELAY_DAYS DEFAULT NULL NULL);
-- 2. [ SPI ]、[ CPI ]を追加
ALTER TABLE PJTW_PJ_PRJ_ALNOT	ADD(SPI NUMBER(6,4) DEFAULT 0.0 NOT NULL);
ALTER TABLE PJTW_PJ_PRJ_ALNOT	ADD(CPI NUMBER(6,4) DEFAULT 0.0 NOT NULL);
COMMENT ON TABLE PJTW_PJ_PRJ_ALNOT IS 'プロジェクトアラート通知情報ワーク(ver.0.2)';
                                                                                                                                                                                                                                                                            