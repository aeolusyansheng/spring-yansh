-- 2013/9/17 ƒJƒ‰ƒ€’Ç‰Á
-- ÌŽZŒ´‰¿Œ©Ïƒgƒ‰ƒ“ [ ‹Æ–±ˆ—“úŽž ]‚ð’Ç‰Á
ALTER TABLE PJTR_PJ_PROSC_EST	ADD(BUSI_PROC_HMS TIMESTAMP(3) NULL);
COMMENT ON TABLE PJTR_PJ_PROSC_EST IS 'ÌŽZŒ´‰¿Œ©Ïƒgƒ‰ƒ“(ver.0.92)';
COMMENT ON COLUMN PJTR_PJ_PROSC_EST.BUSI_PROC_HMS IS '‹Æ–±ˆ—“úŽž';

UPDATE PJTR_PJ_PROSC_EST
SET    BUSI_PROC_HMS = to_timestamp(to_char(BUSI_PROC_DT,'YYYYMMDD')||'000000.000');
COMMIT;

ALTER TABLE PJTR_PJ_PROSC_EST	MODIFY(BUSI_PROC_HMS NOT NULL);

                                                                                                                                                                                                                                                                               