-- 2013/9/19 カラム追加
-- 組織マスタ [ 売上原価振替先設定可フラグ ]を追加
ALTER TABLE PJTM_PJ_ORGAN	ADD(SLS_COST_TRNS_ENBL_FLG CHAR(1) NULL);
COMMENT ON TABLE PJTM_PJ_ORGAN IS '組織マスタ(ver.0.9)';
COMMENT ON COLUMN PJTM_PJ_ORGAN.SLS_COST_TRNS_ENBL_FLG IS '売上原価振替先設定可フラグ';

UPDATE PJTM_PJ_ORGAN
SET    SLS_COST_TRNS_ENBL_FLG = '0';
COMMIT;

ALTER TABLE PJTM_PJ_ORGAN	MODIFY(SLS_COST_TRNS_ENBL_FLG NOT NULL);

                                                                                                                                                                                                                                                                               