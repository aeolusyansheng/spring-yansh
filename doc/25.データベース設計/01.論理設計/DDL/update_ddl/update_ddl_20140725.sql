-- 2014/7/10 主キー項目の追加
-- 外注仕掛品実績トランの主キー項目として [ 発注先コード ]を追加
ALTER TABLE PJTR_PJ_OUTS_WKINPR_RSLT DROP CONSTRAINT PK_PJTR_PJ_OUTS_WKINPR_RSLT CASCADE DROP INDEX;

ALTER TABLE PJTR_PJ_OUTS_WKINPR_RSLT ADD CONSTRAINT PK_PJTR_PJ_OUTS_WKINPR_RSLT PRIMARY KEY(PRJ_CD, SHIP_MNG_NO, INCI_DT, INCI_TMS, ENT_YYMM, SUPP_CD);
COMMENT ON TABLE PJTR_PJ_OUTS_WKINPR_RSLT IS '外注仕掛品実績トラン(ver.1.01)';



-- 2014/7/25 カラム桁数変更
-- プロジェクトトラン
-- 1. [ プロジェクト名 ]をvachar2(45)からvachar2(90)に変更
-- 2. [ プロジェクト詳細名 ]をvachar2(360)からvachar2(720)に変更
ALTER TABLE PJTR_PJ_PRJ	MODIFY(PRJ_NM VARCHAR2(90));
ALTER TABLE PJTR_PJ_PRJ	MODIFY(PRJ_DT_NM VARCHAR2(720));
COMMENT ON TABLE PJTR_PJ_PRJ IS 'プロジェクトトラン(ver.1.01)';

-- プロジェクトアラート通知情報ワーク
-- 1. [ プロジェクト名 ]をvachar2(45)からvachar2(90)に変更
ALTER TABLE PJTW_PJ_PRJ_ALNOT	MODIFY(PRJ_NM VARCHAR2(90));
COMMENT ON TABLE PJTW_PJ_PRJ_ALNOT IS 'プロジェクトアラート通知情報ワーク(ver.1.01)';
                                                                                                                                                                                    