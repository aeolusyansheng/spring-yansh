-- 2014/5/29 主キータスクUIDを追加
-- 締処理用社員・派遣単価マスタ
-- 1. 締処理用社員・派遣単価マスタテーブルをDROPする
DROP TABLE PJTM_PJ_STF_DP_UNTPRICE;

-- 2．締処理用社員・派遣単価マスタテーブルを作成し、それに対する制約とコメント付与を行う。
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

COMMENT ON TABLE PJTM_PJ_STF_DP_UNTPRICE IS '締処理用社員・派遣単価マスタ(ver.0.3)';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.PRJ_CD IS 'プロジェクトコード';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.EMP_CD IS '従業員コード';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.TSK_UID IS 'タスクUID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.EMP_ID IS '従業員ID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.EXP_SELL_TMPRICE IS '経費込時間単価';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.DEL_FLG IS '削除フラグ';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.REG_USR_ID IS '登録ユーザID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.REG_PGM_ID IS '登録プログラムID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.REG_TS IS '登録タイムスタンプ';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.UPD_USR_ID IS '更新ユーザID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.UPD_PGM_ID IS '更新プログラムID';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.UPD_TS IS '更新タイムスタンプ';
COMMENT ON COLUMN PJTM_PJ_STF_DP_UNTPRICE.VER_NO IS 'バージョン番号';
                                                                                                                                                                                                                                                                            