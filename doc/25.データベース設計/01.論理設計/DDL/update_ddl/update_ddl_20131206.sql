-- 2013/12/6 �J���������C���A�J�����ǉ�
-- SI�Č��A���[�g�ʒm��񃏁[�N
-- 1. [ �x������ ]��K�{����C�ӂɕύX
ALTER TABLE PJTW_PJ_SI_DEAL_ALNOT	MODIFY(DELAY_DAYS DEFAULT NULL NULL);
-- 2. [ SPI ]�A[ CPI ]��ǉ�
ALTER TABLE PJTW_PJ_SI_DEAL_ALNOT	ADD(SPI NUMBER(6,4) DEFAULT 0.0 NOT NULL);
ALTER TABLE PJTW_PJ_SI_DEAL_ALNOT	ADD(CPI NUMBER(6,4) DEFAULT 0.0 NOT NULL);
COMMENT ON TABLE PJTW_PJ_SI_DEAL_ALNOT IS 'SI�Č��A���[�g�ʒm��񃏁[�N(ver.0.2)';
-- �v���W�F�N�g�A���[�g�ʒm��񃏁[�N
-- 1. [ �x������ ]��K�{����C�ӂɕύX
ALTER TABLE PJTW_PJ_PRJ_ALNOT	MODIFY(DELAY_DAYS DEFAULT NULL NULL);
-- 2. [ SPI ]�A[ CPI ]��ǉ�
ALTER TABLE PJTW_PJ_PRJ_ALNOT	ADD(SPI NUMBER(6,4) DEFAULT 0.0 NOT NULL);
ALTER TABLE PJTW_PJ_PRJ_ALNOT	ADD(CPI NUMBER(6,4) DEFAULT 0.0 NOT NULL);
COMMENT ON TABLE PJTW_PJ_PRJ_ALNOT IS '�v���W�F�N�g�A���[�g�ʒm��񃏁[�N(ver.0.2)';
                                                                                                                                                                                                                                                                            