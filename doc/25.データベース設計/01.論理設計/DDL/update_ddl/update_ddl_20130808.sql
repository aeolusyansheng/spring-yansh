-- 2013/8/8 �C���f�b�N�X�ǉ�
-- �ғ����׃g���� [ �v���W�F�N�g�R�[�h ]�ɃC���f�b�N�X��t�^
CREATE INDEX IX1_PJTR_MI_OPE_DT ON PJTR_MI_OPE_DT (
    PRJ_CD 
);
COMMENT ON TABLE PJTR_MI_OPE_DT IS '�ғ����׃g����(ver.0.7)';

-- WBS�^�X�N�ˑ��֌W�g���� [ ��s�^�X�NUID ]�ɃC���f�b�N�X��t�^
CREATE INDEX IX2_PJTR_PJ_WBS_TSK_DEP ON PJTR_PJ_WBS_TSK_DEP (
    LEAD_TSK_UID
);
COMMENT ON TABLE PJTR_PJ_WBS_TSK_DEP IS 'WBS�^�X�N�ˑ��֌W�g����(ver.0.6)';
