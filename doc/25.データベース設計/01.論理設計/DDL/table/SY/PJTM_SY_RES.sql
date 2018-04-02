-- �e�[�u���pDDL
DROP TABLE PJTM_SY_RES CASCADE CONSTRAINTS;

CREATE TABLE PJTM_SY_RES
(
    RES_ID                         VARCHAR2(18) NOT NULL ,
    REF_AUTH_SEG                   CHAR(1) NOT NULL ,
    RES_NM                         VARCHAR2(150) NOT NULL ,
    REF_SEG                        CHAR(1) NOT NULL ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTM_SY_RES ADD CONSTRAINT PK_PJTM_SY_RES PRIMARY KEY (RES_ID ) ;

COMMENT ON TABLE PJTM_SY_RES IS '�E�Ӄ}�X�^(ver.0.7)';
COMMENT ON COLUMN PJTM_SY_RES.RES_ID IS '�E��ID';
COMMENT ON COLUMN PJTM_SY_RES.REF_AUTH_SEG IS '�Q�ƌ����敪';
COMMENT ON COLUMN PJTM_SY_RES.RES_NM IS '�E�Ӗ�';
COMMENT ON COLUMN PJTM_SY_RES.REF_SEG IS '�Q�Ƌ敪';
COMMENT ON COLUMN PJTM_SY_RES.DEL_FLG IS '�폜�t���O';
COMMENT ON COLUMN PJTM_SY_RES.REG_USR_ID IS '�o�^���[�UID';
COMMENT ON COLUMN PJTM_SY_RES.REG_PGM_ID IS '�o�^�v���O����ID';
COMMENT ON COLUMN PJTM_SY_RES.REG_TS IS '�o�^�^�C���X�^���v';
COMMENT ON COLUMN PJTM_SY_RES.UPD_USR_ID IS '�X�V���[�UID';
COMMENT ON COLUMN PJTM_SY_RES.UPD_PGM_ID IS '�X�V�v���O����ID';
COMMENT ON COLUMN PJTM_SY_RES.UPD_TS IS '�X�V�^�C���X�^���v';
COMMENT ON COLUMN PJTM_SY_RES.VER_NO IS '�o�[�W�����ԍ�';

-- ���j�[�N�C���f�b�N�X�pDDL

-- �C���f�b�N�X�pDDL
