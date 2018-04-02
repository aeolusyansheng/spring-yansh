-- �e�[�u���pDDL
DROP TABLE PJTM_PJ_PRJ_TPL CASCADE CONSTRAINTS;

CREATE TABLE PJTM_PJ_PRJ_TPL
(
    TPL_ID                         NUMBER(15) NOT NULL ,
    TPL_NO                         VARCHAR2(25) ,
    VLD_STR_DT                     DATE ,
    VLD_END_DT                     DATE ,
    TPL_SNM                        VARCHAR2(90) ,
    TPL_FM_NM                      VARCHAR2(720) ,
    PRJ_TYP_NM                     VARCHAR2(60) ,
    PROP_PRJ_FLG                   CHAR(1) DEFAULT '0' NOT NULL ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTM_PJ_PRJ_TPL ADD CONSTRAINT PK_PJTM_PJ_PRJ_TPL PRIMARY KEY (TPL_ID ) ;

COMMENT ON TABLE PJTM_PJ_PRJ_TPL IS '�v���W�F�N�g�e���v���[�g�}�X�^(ver.0.4)';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.TPL_ID IS '�e���v���[�gID';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.TPL_NO IS '�e���v���[�g�ԍ�';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.VLD_STR_DT IS '�L���J�n��';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.VLD_END_DT IS '�L���I����';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.TPL_SNM IS '�e���v���[�g����';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.TPL_FM_NM IS '�e���v���[�g��������';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.PRJ_TYP_NM IS '�v���W�F�N�g�^�C�v����';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.PROP_PRJ_FLG IS '��ăv���W�F�N�g�t���O';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.DEL_FLG IS '�폜�t���O';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.REG_USR_ID IS '�o�^���[�UID';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.REG_PGM_ID IS '�o�^�v���O����ID';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.REG_TS IS '�o�^�^�C���X�^���v';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.UPD_USR_ID IS '�X�V���[�UID';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.UPD_PGM_ID IS '�X�V�v���O����ID';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.UPD_TS IS '�X�V�^�C���X�^���v';
COMMENT ON COLUMN PJTM_PJ_PRJ_TPL.VER_NO IS '�o�[�W�����ԍ�';

-- ���j�[�N�C���f�b�N�X�pDDL

-- �C���f�b�N�X�pDDL
