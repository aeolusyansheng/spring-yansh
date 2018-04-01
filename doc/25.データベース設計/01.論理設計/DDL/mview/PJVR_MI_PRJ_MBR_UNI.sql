--NextMIプロジェクトメンバ(NextMIプロジェクトメンバトラン)と
--PJNaviプロジェクトメンバ(プロジェクトリソーストラン)の連結VIEW
--PK:プロジェクトコード,従業員コード,有効開始日,ロール

--DROP MATERIALIZED VIEW LOG ON PJTR_MI_NEXTMI_PRJ_MBR;
--CREATE MATERIALIZED VIEW LOG ON PJTR_MI_NEXTMI_PRJ_MBR WITH ROWID;

--DROP MATERIALIZED VIEW LOG ON PJTR_PJ_PRJ_RESRC;
--CREATE MATERIALIZED VIEW LOG ON PJTR_PJ_PRJ_RESRC WITH ROWID;

DROP MATERIALIZED VIEW PJVR_MI_PRJ_MBR_UNI;
CREATE MATERIALIZED VIEW PJVR_MI_PRJ_MBR_UNI
REFRESH FAST ON COMMIT
AS
SELECT NEXTMI_PRJ_MBR.PRJ_CD               AS PRJ_CD
     , NEXTMI_PRJ_MBR.PRJ_MBR_EMP_CD       AS PRJ_MBR_EMP_CD
     , NEXTMI_PRJ_MBR.ROLE                 AS ROLE
     , NEXTMI_PRJ_MBR.VLD_STR_DT           AS VLD_STR_DT
     , NEXTMI_PRJ_MBR.VLD_END_DT           AS VLD_END_DT
     , NEXTMI_PRJ_MBR.ROWID                AS ROW_ID1
     ,'1'                                  AS PRJ_MK_SYS_SEG  -- 0：PJNavi,1：NextMI
  FROM PJTR_MI_NEXTMI_PRJ_MBR NEXTMI_PRJ_MBR
 WHERE DEL_FLG = '0'
UNION ALL
SELECT PRJ_RESRC.PRJ_CD                    AS PRJ_CD
     , PRJ_RESRC.PRJ_RESRC_EMP_CD          AS PRJ_MBR_EMP_CD
     , PRJ_RESRC.PRJ_RESRC_ROLE            AS ROLE
     , PRJ_RESRC.PRJ_RESRC_STR_DT          AS VLD_STR_DT
     , PRJ_RESRC.PRJ_RESRC_END_DT          AS VLD_END_DT
     , PRJ_RESRC.ROWID                     AS ROW_ID1
     ,'0'                                  AS PRJ_MK_SYS_SEG  -- 0：PJNavi,1：NextMI
  FROM PJTR_PJ_PRJ_RESRC PRJ_RESRC
 WHERE DEL_FLG = '0'
;
ALTER MATERIALIZED VIEW PJVR_MI_PRJ_MBR_UNI ADD CONSTRAINT PK_PJVR_MI_PRJ_MBR_UNI PRIMARY KEY
    ( PRJ_CD, PRJ_MBR_EMP_CD, VLD_STR_DT, ROLE );

CREATE INDEX IX1_PJVR_MI_PRJ_MBR_UNI ON PJVR_MI_PRJ_MBR_UNI
     ( PRJ_MBR_EMP_CD, VLD_STR_DT, VLD_END_DT );

COMMENT ON MATERIALIZED VIEW PJVR_MI_PRJ_MBR_UNI IS 'プロジェクトメンバトラン連結ビュー(ver.0.1)';
COMMENT ON COLUMN PJVR_MI_PRJ_MBR_UNI.PRJ_CD IS 'プロジェクトコード';
COMMENT ON COLUMN PJVR_MI_PRJ_MBR_UNI.PRJ_MBR_EMP_CD IS 'プロジェクトメンバ従業員コード';
COMMENT ON COLUMN PJVR_MI_PRJ_MBR_UNI.ROLE IS 'ロール';
COMMENT ON COLUMN PJVR_MI_PRJ_MBR_UNI.VLD_STR_DT IS '有効開始日';
COMMENT ON COLUMN PJVR_MI_PRJ_MBR_UNI.VLD_END_DT IS '有効終了日';
COMMENT ON COLUMN PJVR_MI_PRJ_MBR_UNI.ROW_ID1 IS 'ROW_ID1';
COMMENT ON COLUMN PJVR_MI_PRJ_MBR_UNI.PRJ_MK_SYS_SEG IS 'プロジェクト作成システム区分';

