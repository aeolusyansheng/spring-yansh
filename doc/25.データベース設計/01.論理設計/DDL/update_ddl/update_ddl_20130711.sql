-- 2013/7/11 �J���������ύX
-- ����w�b�_�g���� [ ���ϔԍ� ]�̑�����[ VARCHAR2(10) ]�ɕύX
ALTER TABLE PJTR_PJ_CONT_HEAD	MODIFY(EST_NO VARCHAR2(10));
COMMENT ON TABLE PJTR_PJ_CONT_HEAD IS '����w�b�_�g����(ver.0.6)';

UPDATE	PJTR_PJ_CONT_HEAD
set		EST_NO = RTRIM(EST_NO)
;
COMMIT;

-- ��������w�b�_�g���� [ ���ϔԍ� ]�̑�����[ VARCHAR2(10) ]�ɕύX
ALTER TABLE PJTR_PJ_ACCEPT_SLS_HEAD	MODIFY(EST_NO VARCHAR2(10));
COMMENT ON TABLE PJTR_PJ_ACCEPT_SLS_HEAD IS '��������w�b�_�g����(ver.0.6)';

UPDATE	PJTR_PJ_ACCEPT_SLS_HEAD
set		EST_NO = RTRIM(EST_NO)
;
COMMIT;
