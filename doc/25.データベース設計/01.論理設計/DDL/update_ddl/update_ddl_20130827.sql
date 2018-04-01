-- 2013/8/27 インデックス追加
-- NextMIプロジェクトメンバトラン [ プロジェクトメンバ従業員コード ]、[ 有効開始日 ]、[ 有効終了日 ]に複合インデックスを付与
CREATE INDEX IX1_PJTR_MI_NEXTMI_PRJ_MBR ON PJTR_MI_NEXTMI_PRJ_MBR (
	PRJ_MBR_EMP_CD, VLD_STR_DT, VLD_END_DT
);
COMMENT ON TABLE PJTR_MI_NEXTMI_PRJ_MBR IS 'NextMIプロジェクトメンバトラン(ver.0.6)';
