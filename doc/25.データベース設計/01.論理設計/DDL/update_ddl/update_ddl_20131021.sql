-- 2013/10/21 �J�����ǉ�
-- eTime���������g����
-- 1. [ �o�b�`�����σt���O ]��ǉ�
ALTER TABLE PJTR_MI_ETIME_WRKDT_INFO	ADD(BAT_PROC_DONE_FLG CHAR(1) DEFAULT '0' NOT NULL);
COMMENT ON COLUMN PJTR_MI_ETIME_WRKDT_INFO.BAT_PROC_DONE_FLG IS '�o�b�`�����σt���O';
-- 2. [ �o�b�`�����σt���O ]�A[ �Ζ���(�~��) ]�ɕ����C���f�b�N�X��t�^
CREATE INDEX IX1_PJTR_MI_ETIME_WRKDT_INFO ON PJTR_MI_ETIME_WRKDT_INFO (
    BAT_PROC_DONE_FLG, WK_DT DESC
);
COMMENT ON TABLE PJTR_MI_ETIME_WRKDT_INFO IS 'eTime���������g����(ver.0.5)';

-- eTime���������W�v�g���� �e�[�u���̍폜
DROP TABLE PJTR_MI_ETIME_WRKDT_SUM CASCADE CONSTRAINTS;

                                                                                                                                                                                                                                                                            