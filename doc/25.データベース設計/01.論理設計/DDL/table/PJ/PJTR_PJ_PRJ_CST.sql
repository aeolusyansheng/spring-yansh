-- �e�[�u���pDDL
DROP TABLE PJTR_PJ_PRJ_CST CASCADE CONSTRAINTS;

CREATE TABLE PJTR_PJ_PRJ_CST
(
    PRJ_CD                         CHAR(10) NOT NULL ,
    OPE_YYMM                       CHAR(6) NOT NULL ,
    EMP_ID                         NUMBER(10) NOT NULL ,
    EMP_SEG                        CHAR(1) NOT NULL ,
    AC                             NUMBER(10,2) ,
    AC_PRICE                       NUMBER(12) ,
    ENT_CLOSE_DT                   DATE ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTR_PJ_PRJ_CST ADD CONSTRAINT PK_PJTR_PJ_PRJ_CST PRIMARY KEY (PRJ_CD, OPE_YYMM, EMP_ID, EMP_SEG ) ;

COMMENT ON TABLE PJTR_PJ_PRJ_CST IS '�v���W�F�N�g�R�X�g�g����(ver.0.3)';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.PRJ_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.OPE_YYMM IS '�ғ��N��';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.EMP_ID IS '�]�ƈ�ID';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.EMP_SEG IS '�]�ƈ��敪';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.AC IS 'AC';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.AC_PRICE IS 'AC�z';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.ENT_CLOSE_DT IS '���͒���';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.DEL_FLG IS '�폜�t���O';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.REG_USR_ID IS '�o�^���[�UID';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.REG_PGM_ID IS '�o�^�v���O����ID';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.REG_TS IS '�o�^�^�C���X�^���v';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.UPD_USR_ID IS '�X�V���[�UID';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.UPD_PGM_ID IS '�X�V�v���O����ID';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.UPD_TS IS '�X�V�^�C���X�^���v';
COMMENT ON COLUMN PJTR_PJ_PRJ_CST.VER_NO IS '�o�[�W�����ԍ�';

-- ���j�[�N�C���f�b�N�X�pDDL

-- �C���f�b�N�X�pDDL
