-- 2013/8/23 �C���f�b�N�X�ǉ�
-- �ғ����͗����g���� [ ���͎�ID ]�A[ �ғ��� ]�A[ �ғ��X�e�[�^�X ]�ɕ����C���f�b�N�X��t�^
CREATE INDEX IX1_PJTR_MI_OPENT_HIST ON PJTR_MI_OPENT_HIST (
    ENT_PSN_ID, OPE_DT, OPE_STATS
);
COMMENT ON TABLE PJTR_MI_OPENT_HIST IS '�ғ����͗����g����(ver.0.8)';
