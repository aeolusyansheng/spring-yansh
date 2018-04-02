col now_time new_value v_now_time
SELECT TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') NOW_TIME FROM DUAL ;
spool c:\spool_&v_now_time..log
set timing on
set echo on

-- �ėp���t�Ǘ��}�X�^
-- 1.�V�K�e�[�u���쐬
CREATE TABLE PJTM_PJ_HNYO_DT_MNG_MST
(
    HNYO_SEG_CD                    CHAR(3) NOT NULL ,
    HNYO_SEG_DT_CD                 VARCHAR2(30) NOT NULL ,
    MNG_DT                         DATE ,
    DEL_FLG                        CHAR(1) DEFAULT '0' NOT NULL ,
    REG_USR_ID                     NUMBER(10) NOT NULL ,
    REG_PGM_ID                     CHAR(11) NOT NULL ,
    REG_TS                         TIMESTAMP(3) NOT NULL ,
    UPD_USR_ID                     NUMBER(10) NOT NULL ,
    UPD_PGM_ID                     CHAR(11) NOT NULL ,
    UPD_TS                         TIMESTAMP(3) NOT NULL ,
    VER_NO                         NUMBER(10) DEFAULT 0 NOT NULL
);
ALTER TABLE PJTM_PJ_HNYO_DT_MNG_MST ADD CONSTRAINT PK_PJTM_PJ_HNYO_DT_MNG_MST PRIMARY KEY (HNYO_SEG_CD, HNYO_SEG_DT_CD ) ;

COMMENT ON TABLE PJTM_PJ_HNYO_DT_MNG_MST IS '�ėp���t�Ǘ��}�X�^(ver.1.0)';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.HNYO_SEG_CD IS '�ėp�敪�R�[�h';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.HNYO_SEG_DT_CD IS '�ėp�敪���׃R�[�h';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.MNG_DT IS '�Ǘ����t';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.DEL_FLG IS '�폜�t���O';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.REG_USR_ID IS '�o�^���[�UID';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.REG_PGM_ID IS '�o�^�v���O����ID';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.REG_TS IS '�o�^�^�C���X�^���v';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.UPD_USR_ID IS '�X�V���[�UID';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.UPD_PGM_ID IS '�X�V�v���O����ID';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.UPD_TS IS '�X�V�^�C���X�^���v';
COMMENT ON COLUMN PJTM_PJ_HNYO_DT_MNG_MST.VER_NO IS '�o�[�W�����ԍ�';

-- ���j�[�N�C���f�b�N�X�pDDL

-- �C���f�b�N�X�pDDL

PROMPT �ėp���t�Ǘ��}�X�^ < PJ_MOD.PJTM_PJ_HNYO_DT_MNG_MST > �V�m�j���č쐬
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_HNYO_DT_MNG_MST
                      FOR PJNAVI.PJTM_PJ_HNYO_DT_MNG_MST
;

PROMPT �ėp���t�Ǘ��}�X�^ < PJ_REF.PJTM_PJ_HNYO_DT_MNG_MST > �V�m�j���č쐬
CREATE OR REPLACE SYNONYM PJ_REF.PJTM_PJ_HNYO_DT_MNG_MST
                      FOR PJNAVI.PJTM_PJ_HNYO_DT_MNG_MST
;


spool off