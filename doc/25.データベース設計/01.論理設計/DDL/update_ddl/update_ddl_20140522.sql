-- 2014/5/22 カラム属性変更
-- SI案件アラート通知情報ワーク [ 遅延日数 ]の属性を[ NUMBER(5) ]に変更
--                              [ SPI ]の属性を[ NUMBER(14,4) ]に変更
--                              [ CPI ]の属性を[ NUMBER(14,4) ]に変更
ALTER TABLE PJTW_PJ_SI_DEAL_ALNOT	MODIFY(DELAY_DAYS NUMBER(5),
                                           SPI NUMBER(14,4),
                                           CPI NUMBER(14,4));
COMMENT ON TABLE PJTW_PJ_SI_DEAL_ALNOT IS 'SI案件アラート通知情報ワーク(ver.0.3)';

