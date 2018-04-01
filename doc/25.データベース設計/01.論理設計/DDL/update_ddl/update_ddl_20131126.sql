-- 2013/11/26 インデックス追加
-- 組織マスタ
-- [ 部課コード ]、[ 有効開始日 ]、[ 有効終了日 ]に複合インデックスを付与
CREATE INDEX IX1_PJTM_PJ_ORGAN ON PJTM_PJ_ORGAN (
    DEPT_CD ,
    VLD_STR_DT ,
    VLD_END_DT 
);
COMMENT ON TABLE PJTM_PJ_ORGAN IS '組織マスタ(ver.0.91)';
                                                                                                                                                                                                                                                                            