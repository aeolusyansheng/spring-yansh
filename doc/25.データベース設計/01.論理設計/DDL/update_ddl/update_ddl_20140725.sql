-- 2014/7/10 ��L�[���ڂ̒ǉ�
-- �O���d�|�i���уg�����̎�L�[���ڂƂ��� [ ������R�[�h ]��ǉ�
ALTER TABLE PJTR_PJ_OUTS_WKINPR_RSLT DROP CONSTRAINT PK_PJTR_PJ_OUTS_WKINPR_RSLT CASCADE DROP INDEX;

ALTER TABLE PJTR_PJ_OUTS_WKINPR_RSLT ADD CONSTRAINT PK_PJTR_PJ_OUTS_WKINPR_RSLT PRIMARY KEY(PRJ_CD, SHIP_MNG_NO, INCI_DT, INCI_TMS, ENT_YYMM, SUPP_CD);
COMMENT ON TABLE PJTR_PJ_OUTS_WKINPR_RSLT IS '�O���d�|�i���уg����(ver.1.01)';



-- 2014/7/25 �J���������ύX
-- �v���W�F�N�g�g����
-- 1. [ �v���W�F�N�g�� ]��vachar2(45)����vachar2(90)�ɕύX
-- 2. [ �v���W�F�N�g�ڍז� ]��vachar2(360)����vachar2(720)�ɕύX
ALTER TABLE PJTR_PJ_PRJ	MODIFY(PRJ_NM VARCHAR2(90));
ALTER TABLE PJTR_PJ_PRJ	MODIFY(PRJ_DT_NM VARCHAR2(720));
COMMENT ON TABLE PJTR_PJ_PRJ IS '�v���W�F�N�g�g����(ver.1.01)';

-- �v���W�F�N�g�A���[�g�ʒm��񃏁[�N
-- 1. [ �v���W�F�N�g�� ]��vachar2(45)����vachar2(90)�ɕύX
ALTER TABLE PJTW_PJ_PRJ_ALNOT	MODIFY(PRJ_NM VARCHAR2(90));
COMMENT ON TABLE PJTW_PJ_PRJ_ALNOT IS '�v���W�F�N�g�A���[�g�ʒm��񃏁[�N(ver.1.01)';
                                                                                                                                                                                    