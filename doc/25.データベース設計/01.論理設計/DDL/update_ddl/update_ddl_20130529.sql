-- 2013/5/29 �J���������ύX
-- NextMI�v���W�F�N�g�����o�g���� [ ���[�� ]�̑�����[ VARCHAR2(4) ]�ɕύX
ALTER TABLE PJTR_MI_NEXTMI_PRJ_MBR	MODIFY(ROLE VARCHAR2(4));
COMMENT ON TABLE PJTR_MI_NEXTMI_PRJ_MBR IS 'NextMI�v���W�F�N�g�����o�g����(ver.0.5)';

UPDATE	PJTR_MI_NEXTMI_PRJ_MBR
set		ROLE = RTRIM(ROLE)
;
COMMIT;
