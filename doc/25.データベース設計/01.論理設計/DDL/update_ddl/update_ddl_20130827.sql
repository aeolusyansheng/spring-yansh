-- 2013/8/27 �C���f�b�N�X�ǉ�
-- NextMI�v���W�F�N�g�����o�g���� [ �v���W�F�N�g�����o�]�ƈ��R�[�h ]�A[ �L���J�n�� ]�A[ �L���I���� ]�ɕ����C���f�b�N�X��t�^
CREATE INDEX IX1_PJTR_MI_NEXTMI_PRJ_MBR ON PJTR_MI_NEXTMI_PRJ_MBR (
	PRJ_MBR_EMP_CD, VLD_STR_DT, VLD_END_DT
);
COMMENT ON TABLE PJTR_MI_NEXTMI_PRJ_MBR IS 'NextMI�v���W�F�N�g�����o�g����(ver.0.6)';
