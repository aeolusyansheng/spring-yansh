-- 2013/9/19 �J�����ǉ�
-- �g�D�}�X�^ [ ���㌴���U�֐�ݒ�t���O ]��ǉ�
ALTER TABLE PJTM_PJ_ORGAN	ADD(SLS_COST_TRNS_ENBL_FLG CHAR(1) NULL);
COMMENT ON TABLE PJTM_PJ_ORGAN IS '�g�D�}�X�^(ver.0.9)';
COMMENT ON COLUMN PJTM_PJ_ORGAN.SLS_COST_TRNS_ENBL_FLG IS '���㌴���U�֐�ݒ�t���O';

UPDATE PJTM_PJ_ORGAN
SET    SLS_COST_TRNS_ENBL_FLG = '0';
COMMIT;

ALTER TABLE PJTM_PJ_ORGAN	MODIFY(SLS_COST_TRNS_ENBL_FLG NOT NULL);

                                                                                                                                                                                                                                                                               