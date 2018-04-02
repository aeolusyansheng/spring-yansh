col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- WBS�^�X�N�g����
-- 1. [AC]���폜
-- 2. [AC�z]���폜
ALTER TABLE PJTR_PJ_WBS_TSK DROP (AC , AC_PRICE);
COMMENT ON TABLE PJTR_PJ_WBS_TSK IS 'WBS�^�X�N�g����(ver.1.01)';


-- WBS�^�X�N���\�[�X�g����
--1. [AC]���폜
--2. [AC�z]���폜
ALTER TABLE PJTR_PJ_WBS_TSK_RESRC DROP (AC , AC_PRICE);
COMMENT ON TABLE PJTR_PJ_WBS_TSK_RESRC IS 'WBS�^�X�N���\�[�X�g����(ver.1.01)';


-- �������p�Ј��E�h���P���}�X�^
--1. �e�[�u���p�~�̂��߃R�����g��ύX
COMMENT ON TABLE PJTM_PJ_STF_DP_UNTPRICE IS '�y�p�~�z�������p�Ј��E�h���P���}�X�^(ver.0.3)';

-- ������ʍH���E���z�g����
--1. �e�[�u���p�~�̂��߃R�����g��ύX
COMMENT ON TABLE PJTR_PJ_SUPP_MNHOUR_PRICE IS '�y�p�~�z������ʍH���E���z�g����(ver.0.3)';

-- �^�X�N��PV�g����
--1. �e�[�u���p�~�̂��߃R�����g��ύX
COMMENT ON TABLE PJTR_PJ_TSK_DIST_PV IS '�y�p�~�z�^�X�N��PV�g����(ver.0.8)';

-- �^�X�N���\�[�X��PV�g����
--1. �e�[�u���p�~�̂��߃R�����g��ύX
COMMENT ON TABLE PJTR_PJ_TSK_RESRC_DIST_PV IS '�y�p�~�z�^�X�N���\�[�X��PV�g����(ver.0.6)';

-- �v���W�F�N�g�R�X�g�g����
--1. �e�[�u���p�~�̂��߃R�����g��ύX
COMMENT ON TABLE PJTR_PJ_PRJ_CST IS '�y�p�~�z�v���W�F�N�g�R�X�g�g����(ver.0.3)';

-- �O��񍐒�WBS�^�X�NSNAP�g����
--1. �e�[�u���p�~�̂��߃R�����g��ύX
COMMENT ON TABLE PJTR_PJ_PREV_WBS_TSK_SNAP IS '�y�p�~�z�O��񍐒�WBS�^�X�NSNAP�g����(ver.0.8)';

-- ����񍐒�WBS�^�X�NSNAP�g����
--1. �e�[�u���p�~�̂��߃R�����g��ύX
COMMENT ON TABLE PJTR_PJ_THIS_WBS_TSK_SNAP IS '�y�p�~�z����񍐒�WBS�^�X�NSNAP�g����(ver.0.8)';

-- WBS��EVM�W�vSNAP�g����
--1. �e�[�u���p�~�̂��߃R�����g��ύX
COMMENT ON TABLE PJTR_PJ_WBS_EVM_SUM_SNAP IS '�y�p�~�zWBS��EVM�W�vSNAP�g����(ver.0.91)';

-- �v���W�F�N�g��EVM�W�vSNAP�g����
--1. �e�[�u���p�~�̂��߃R�����g��ύX
COMMENT ON TABLE PJTR_PJ_PRJ_EVM_SUM_SNAP IS '�y�p�~�z�v���W�F�N�g��EVM�W�vSNAP�g����(ver.1.01)';

spool off