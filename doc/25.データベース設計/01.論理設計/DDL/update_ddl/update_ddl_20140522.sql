-- 2014/5/22 �J���������ύX
-- SI�Č��A���[�g�ʒm��񃏁[�N [ �x������ ]�̑�����[ NUMBER(5) ]�ɕύX
--                              [ SPI ]�̑�����[ NUMBER(14,4) ]�ɕύX
--                              [ CPI ]�̑�����[ NUMBER(14,4) ]�ɕύX
ALTER TABLE PJTW_PJ_SI_DEAL_ALNOT	MODIFY(DELAY_DAYS NUMBER(5),
                                           SPI NUMBER(14,4),
                                           CPI NUMBER(14,4));
COMMENT ON TABLE PJTW_PJ_SI_DEAL_ALNOT IS 'SI�Č��A���[�g�ʒm��񃏁[�N(ver.0.3)';

