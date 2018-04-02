col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- �]�ƈ��v���t�@�C���g����
-- 1.�V�K�e�[�u���쐬
CREATE TABLE PJTR_PJ_EMP_PRFL
(
    EMP_ID                         NUMBER(10) NOT NULL ,
    SEG_DT_CD                      VARCHAR2(30) NOT NULL ,
    VAL                            VARCHAR2(150) ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTR_PJ_EMP_PRFL ADD CONSTRAINT PK_PJTR_PJ_EMP_PRFL PRIMARY KEY (EMP_ID, SEG_DT_CD ) ;

COMMENT ON TABLE PJTR_PJ_EMP_PRFL IS '�]�ƈ��v���t�@�C���g����(ver.1.0)';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.EMP_ID IS '�]�ƈ�ID';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.SEG_DT_CD IS '�敪���׃R�[�h';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.VAL IS '�l';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.DEL_FLG IS '�폜�t���O';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.REG_USR_ID IS '�o�^���[�UID';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.REG_PGM_ID IS '�o�^�v���O����ID';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.REG_TS IS '�o�^�^�C���X�^���v';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.UPD_USR_ID IS '�X�V���[�UID';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.UPD_PGM_ID IS '�X�V�v���O����ID';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.UPD_TS IS '�X�V�^�C���X�^���v';
COMMENT ON COLUMN PJTR_PJ_EMP_PRFL.VER_NO IS '�o�[�W�����ԍ�';

-- ���j�[�N�C���f�b�N�X�pDDL

-- �C���f�b�N�X�pDDL

--PROMPT �]�ƈ��v���t�@�C���g���� < PJ_MOD.PJTR_PJ_EMP_PRFL > �V�m�j���č쐬
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_EMP_PRFL
                      FOR PJNAVI.PJTR_PJ_EMP_PRFL
;

--PROMPT �]�ƈ��v���t�@�C���g���� < PJ_REF.PJTR_PJ_EMP_PRFL > �V�m�j���č쐬
CREATE OR REPLACE SYNONYM PJ_REF.PJTR_PJ_EMP_PRFL
                      FOR PJNAVI.PJTR_PJ_EMP_PRFL
;


spool off