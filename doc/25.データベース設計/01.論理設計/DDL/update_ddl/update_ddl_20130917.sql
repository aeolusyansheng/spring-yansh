-- 2013/9/17 カラム追加
-- 採算原価見積トラン [ 業務処理日時 ]を追加
ALTER TABLE PJTR_PJ_PROSC_EST	ADD(BUSI_PROC_HMS TIMESTAMP(3) NULL);
COMMENT ON TABLE PJTR_PJ_PROSC_EST IS '採算原価見積トラン(ver.0.92)';
COMMENT ON COLUMN PJTR_PJ_PROSC_EST.BUSI_PROC_HMS IS '業務処理日時';

UPDATE PJTR_PJ_PROSC_EST
SET    BUSI_PROC_HMS = to_timestamp(to_char(BUSI_PROC_DT,'YYYYMMDD')||'000000.000');
COMMIT;

ALTER TABLE PJTR_PJ_PROSC_EST	MODIFY(BUSI_PROC_HMS NOT NULL);

                                                                                                                                                                                                                                                                               