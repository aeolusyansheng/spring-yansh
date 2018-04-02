-- 2014/5/29 ��L�[�^�X�NUID��ǉ�
-- �������p�Ј��E�h���P���}�X�^
-- 1. �������p�Ј��E�h���P���}�X�^�e�[�u����DROP����
DROP TABLE PJTM_PJ_STF_DP_UNTPRICE;

-- 2�D�������p�Ј��E�h���P���}�X�^�e�[�u�����쐬���A����ɑ΂��鐧��ƃR�����g�t�^���s���B
CREATE TABLE PJTM_PJ_STF_DP_UNTPRICE
(
    PRJ_CD                         CHAR(10) NOT NULL,
    EMP_CD                         VARCHAR2(30) NOT NULL,
    TSK_UID                        NUMBER(15,0) NOT NULL,
    EMP_ID                         NUMBER(10,0),
    EXP_SELL_TMPRICE               NUMBER(10,0) DEFAULT 0 NOT NULL,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL,
    REG_USR_ID                     NUMBER(10,0) NOT NULL,
    REG_PGM_ID                     CHAR(11) NOT NULL,
    REG_TS                         TIMESTAMP(3) NOT NULL,
    UPD_USR_ID                     NUMBER(10,0) NOT NULL,
    UPD_PGM_ID                     CHAR(11) NOT NULL,
    UPD_TS                         TIMESTAMP(3) NOT NULL,
    VER_NO                         NUMBER(10,0) DEFAULT 0 NOT NULL
);

ALTER TABLE PJTM_PJ_STF_DP_UNTPRICE ADD CONSTRAINT PK_PJTM_PJ_STF_DP_UNTPRICE PRIMARY KEY (PRJ_CD, EMP_CD, TSK_UID);

COMMENT ON TABLE PJTM_PJ_STF_DP_UNTPRICE IS '�������p�Ј��E�h���P���}�X�^(ver.0.3)';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.PRJ_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.EMP_CD IS '�]�ƈ��R�[�h';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.TSK_UID IS '�^�X�NUID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.EMP_ID IS '�]�ƈ�ID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.EXP_SELL_TMPRICE IS '�o����ԒP��';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.DEL_FLG IS '�폜�t���O';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.REG_USR_ID IS '�o�^���[�UID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.REG_PGM_ID IS '�o�^�v���O����ID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.REG_TS IS '�o�^�^�C���X�^���v';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.UPD_USR_ID IS '�X�V���[�UID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.UPD_PGM_ID IS '�X�V�v���O����ID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.UPD_TS IS '�X�V�^�C���X�^���v';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.VER_NO IS '�o�[�W�����ԍ�';
                                                                                                                                                                                                                                                                            