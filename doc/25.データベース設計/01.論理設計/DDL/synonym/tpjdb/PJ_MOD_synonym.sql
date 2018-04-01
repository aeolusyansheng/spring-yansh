SET ECHO OFF
SPOOL PJ_MOD_synonym.log
/************************************************************/
/*   SqlID       : PJ_MOD_synonym.sql                       */
/*   SqlName     : シノニム作成SQL                          */
/*   DBID        : tpjdb                                    */
/*   User        : PJ_MOD                                   */
/*   UpdateDate  : 2014/04/22 12:52:42                      */
/************************************************************/
PROMPT ジョブ間パラメータ保持トラン < PJ_MOD.PJTR_FW_INTERJOB_PARAM > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_FW_INTERJOB_PARAM
                      FOR PJNAVI.PJTR_FW_INTERJOB_PARAM
;
PROMPT 業務日付マスタ < PJ_MOD.PJTM_FW_BUSI_DT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_FW_BUSI_DT
                      FOR PJNAVI.PJTM_FW_BUSI_DT
;
PROMPT eTime実働日情報トラン < PJ_MOD.PJTR_MI_ETIME_WRKDT_INFO > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_ETIME_WRKDT_INFO
                      FOR PJNAVI.PJTR_MI_ETIME_WRKDT_INFO
;
PROMPT NextMIプロジェクトタスクトラン < PJ_MOD.PJTR_MI_NEXTMI_PRJ_TSK > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_NEXTMI_PRJ_TSK
                      FOR PJNAVI.PJTR_MI_NEXTMI_PRJ_TSK
;
PROMPT NextMIプロジェクトトラン < PJ_MOD.PJTR_MI_NEXTMI_PRJ > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_NEXTMI_PRJ
                      FOR PJNAVI.PJTR_MI_NEXTMI_PRJ
;
PROMPT NextMIプロジェクトメンバトラン < PJ_MOD.PJTR_MI_NEXTMI_PRJ_MBR > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_NEXTMI_PRJ_MBR
                      FOR PJNAVI.PJTR_MI_NEXTMI_PRJ_MBR
;
PROMPT プロジェクト付帯情報トラン < PJ_MOD.PJTR_MI_PRJ_ICDT_INFO > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_PRJ_ICDT_INFO
                      FOR PJNAVI.PJTR_MI_PRJ_ICDT_INFO
;
PROMPT 稼働日情報トラン < PJ_MOD.PJTR_MI_OPE_DT_INFO > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_OPE_DT_INFO
                      FOR PJNAVI.PJTR_MI_OPE_DT_INFO
;
PROMPT 稼働入力カレンダーマスタ < PJ_MOD.PJTM_MI_OPENT_CAL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_MI_OPENT_CAL
                      FOR PJNAVI.PJTM_MI_OPENT_CAL
;
PROMPT 稼働入力コメントトラン < PJ_MOD.PJTR_MI_OPENT_CMT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_OPENT_CMT
                      FOR PJNAVI.PJTR_MI_OPENT_CMT
;
PROMPT 稼働入力コントロールトラン < PJ_MOD.PJTR_MI_OPENT_CTRL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_OPENT_CTRL
                      FOR PJNAVI.PJTR_MI_OPENT_CTRL
;
PROMPT 稼働入力テンプレートトラン < PJ_MOD.PJTR_MI_OPENT_TPL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_OPENT_TPL
                      FOR PJNAVI.PJTR_MI_OPENT_TPL
;
PROMPT 稼働入力テンプレート明細トラン < PJ_MOD.PJTR_MI_OPENT_TPL_DT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_OPENT_TPL_DT
                      FOR PJNAVI.PJTR_MI_OPENT_TPL_DT
;
PROMPT 稼働入力承認権限委譲設定マスタ < PJ_MOD.PJTM_MI_OPENT_AUTH_TRSET > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_MI_OPENT_AUTH_TRSET
                      FOR PJNAVI.PJTM_MI_OPENT_AUTH_TRSET
;
PROMPT 稼働入力承認者設定マスタ < PJ_MOD.PJTM_MI_OPENT_APP_PSN_SET > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_MI_OPENT_APP_PSN_SET
                      FOR PJNAVI.PJTM_MI_OPENT_APP_PSN_SET
;
PROMPT 稼働入力制限マスタ < PJ_MOD.PJTM_MI_OPENT_LMT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_MI_OPENT_LMT
                      FOR PJNAVI.PJTM_MI_OPENT_LMT
;
PROMPT 稼働入力制限解除マスタ < PJ_MOD.PJTM_MI_OPENT_LMT_RLS > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_MI_OPENT_LMT_RLS
                      FOR PJNAVI.PJTM_MI_OPENT_LMT_RLS
;
PROMPT 稼働入力履歴トラン < PJ_MOD.PJTR_MI_OPENT_HIST > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_OPENT_HIST
                      FOR PJNAVI.PJTR_MI_OPENT_HIST
;
PROMPT 稼働明細トラン < PJ_MOD.PJTR_MI_OPE_DT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_MI_OPE_DT
                      FOR PJNAVI.PJTR_MI_OPE_DT
;
PROMPT GL伝票トラン < PJ_MOD.PJTR_PJ_GL_VOUC > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_GL_VOUC
                      FOR PJNAVI.PJTR_PJ_GL_VOUC
;
PROMPT SI案件MY案件トラン < PJ_MOD.PJTR_PJ_SI_DEAL_MY_DEAL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_SI_DEAL_MY_DEAL
                      FOR PJNAVI.PJTR_PJ_SI_DEAL_MY_DEAL
;
PROMPT SI案件トラン < PJ_MOD.PJTR_PJ_SI_DEAL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_SI_DEAL
                      FOR PJNAVI.PJTR_PJ_SI_DEAL
;
PROMPT SI案件リソーストラン < PJ_MOD.PJTR_PJ_SI_DEAL_RESRC > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_SI_DEAL_RESRC
                      FOR PJNAVI.PJTR_PJ_SI_DEAL_RESRC
;
PROMPT SI案件成約紐付トラン < PJ_MOD.PJTR_PJ_SI_DEAL_CONT_LNK > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_SI_DEAL_CONT_LNK
                      FOR PJNAVI.PJTR_PJ_SI_DEAL_CONT_LNK
;
PROMPT プロジェクトNext.MIタスクトラン < PJ_MOD.PJTR_PJ_PRJ_NEXTMI_TSK > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PRJ_NEXTMI_TSK
                      FOR PJNAVI.PJTR_PJ_PRJ_NEXTMI_TSK
;
PROMPT プロジェクトトラン < PJ_MOD.PJTR_PJ_PRJ > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PRJ
                      FOR PJNAVI.PJTR_PJ_PRJ
;
PROMPT プロジェクトリソーストラン < PJ_MOD.PJTR_PJ_PRJ_RESRC > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PRJ_RESRC
                      FOR PJNAVI.PJTR_PJ_PRJ_RESRC
;
PROMPT 経費実績トラン < PJ_MOD.PJTR_PJ_EXP_RSLT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_EXP_RSLT
                      FOR PJNAVI.PJTR_PJ_EXP_RSLT
;
PROMPT 検収売上ヘッダトラン < PJ_MOD.PJTR_PJ_ACCEPT_SLS_HEAD > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_ACCEPT_SLS_HEAD
                      FOR PJNAVI.PJTR_PJ_ACCEPT_SLS_HEAD
;
PROMPT 検収売上明細トラン < PJ_MOD.PJTR_PJ_ACCEPT_SLS_DT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_ACCEPT_SLS_DT
                      FOR PJNAVI.PJTR_PJ_ACCEPT_SLS_DT
;
PROMPT 採算原価見積トラン < PJ_MOD.PJTR_PJ_PROSC_EST > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PROSC_EST
                      FOR PJNAVI.PJTR_PJ_PROSC_EST
;
PROMPT 採算原価見積経費明細トラン < PJ_MOD.PJTR_PJ_PROSC_EST_EXP_DT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PROSC_EST_EXP_DT
                      FOR PJNAVI.PJTR_PJ_PROSC_EST_EXP_DT
;
PROMPT 採算原価見積社員明細トラン < PJ_MOD.PJTR_PJ_PROSC_EST_STF_DT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PROSC_EST_STF_DT
                      FOR PJNAVI.PJTR_PJ_PROSC_EST_STF_DT
;
PROMPT 採算原価見積書内部開発明細トラン < PJ_MOD.PJTR_PJ_PROSC_EST_INDEV_DT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PROSC_EST_INDEV_DT
                      FOR PJNAVI.PJTR_PJ_PROSC_EST_INDEV_DT
;
PROMPT 採算原価見積派遣明細トラン < PJ_MOD.PJTR_PJ_PROSC_EST_DP_DT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PROSC_EST_DP_DT
                      FOR PJNAVI.PJTR_PJ_PROSC_EST_DP_DT
;
PROMPT 採算原価見積発注明細トラン < PJ_MOD.PJTR_PJ_PROSC_EST_SHIP_DT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PROSC_EST_SHIP_DT
                      FOR PJNAVI.PJTR_PJ_PROSC_EST_SHIP_DT
;
PROMPT 新旧組織マッピングワーク < PJ_MOD.PJTW_PJ_OAN_ORGAN_MAP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTW_PJ_OAN_ORGAN_MAP
                      FOR PJNAVI.PJTW_PJ_OAN_ORGAN_MAP
;
PROMPT 進行原価見積送信指示トラン < PJ_MOD.PJTR_PJ_PROGC_EST_SND > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PROGC_EST_SND
                      FOR PJNAVI.PJTR_PJ_PROGC_EST_SND
;
PROMPT 進行売上トラン < PJ_MOD.PJTR_PJ_PROG_SLS > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PROG_SLS
                      FOR PJNAVI.PJTR_PJ_PROG_SLS
;
PROMPT 人件費実績トラン < PJ_MOD.PJTR_PJ_MPC_RSLT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_MPC_RSLT
                      FOR PJNAVI.PJTR_PJ_MPC_RSLT
;
PROMPT 成約ヘッダトラン < PJ_MOD.PJTR_PJ_CONT_HEAD > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_CONT_HEAD
                      FOR PJNAVI.PJTR_PJ_CONT_HEAD
;
PROMPT 成約明細トラン < PJ_MOD.PJTR_PJ_CONT_DT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_CONT_DT
                      FOR PJNAVI.PJTR_PJ_CONT_DT
;
PROMPT 提案コストアラート送信履歴トラン < PJ_MOD.PJTR_PJ_PROP_CA_SND_HIST > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PROP_CA_SND_HIST
                      FOR PJNAVI.PJTR_PJ_PROP_CA_SND_HIST
;
PROMPT 発注実績トラン < PJ_MOD.PJTR_PJ_SHIP_RSLT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_SHIP_RSLT
                      FOR PJNAVI.PJTR_PJ_SHIP_RSLT
;
PROMPT SI案件アラート通知情報ワーク < PJ_MOD.PJTW_PJ_SI_DEAL_ALNOT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTW_PJ_SI_DEAL_ALNOT
                      FOR PJNAVI.PJTW_PJ_SI_DEAL_ALNOT
;
PROMPT データクリーニング対象PJキー情報ワーク < PJ_MOD.PJTW_PJ_DTCL_PJ_KEY > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTW_PJ_DTCL_PJ_KEY
                      FOR PJNAVI.PJTW_PJ_DTCL_PJ_KEY
;
PROMPT データクリーニング対象SI案件キー情報ワーク < PJ_MOD.PJTW_PJ_DTCL_SI_DEAL_KEY > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTW_PJ_DTCL_SI_DEAL_KEY
                      FOR PJNAVI.PJTW_PJ_DTCL_SI_DEAL_KEY
;
PROMPT データクリーニング対象WBSキー情報ワーク < PJ_MOD.PJTW_PJ_DTCL_WBS_KEY > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTW_PJ_DTCL_WBS_KEY
                      FOR PJNAVI.PJTW_PJ_DTCL_WBS_KEY
;
PROMPT データクリーニング対象受注キー情報ワーク < PJ_MOD.PJTW_PJ_DTCL_ODR_KEY > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTW_PJ_DTCL_ODR_KEY
                      FOR PJNAVI.PJTW_PJ_DTCL_ODR_KEY
;
PROMPT データクリーニング対象進行キー情報ワーク < PJ_MOD.PJTW_PJ_DTCL_PROG_KEY > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTW_PJ_DTCL_PROG_KEY
                      FOR PJNAVI.PJTW_PJ_DTCL_PROG_KEY
;
PROMPT プロジェクトアラート通知情報ワーク < PJ_MOD.PJTW_PJ_PRJ_ALNOT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTW_PJ_PRJ_ALNOT
                      FOR PJNAVI.PJTW_PJ_PRJ_ALNOT
;
PROMPT Next.MIテンプレートタスクマスタ < PJ_MOD.PJTM_PJ_NEXTMI_TPL_TSK > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_NEXTMI_TPL_TSK
                      FOR PJNAVI.PJTM_PJ_NEXTMI_TPL_TSK
;
PROMPT PA形態別区分マスタ < PJ_MOD.PJTM_PJ_PA_FORM_SEG > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_PA_FORM_SEG
                      FOR PJNAVI.PJTM_PJ_PA_FORM_SEG
;
PROMPT SI案件アラート基準マスタ < PJ_MOD.PJTM_PJ_SI_DEAL_ALERT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_SI_DEAL_ALERT
                      FOR PJNAVI.PJTM_PJ_SI_DEAL_ALERT
;
PROMPT SI案件全社PMOアラート基準マスタ < PJ_MOD.PJTM_PJ_SI_DEAL_PMO_ALERT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_SI_DEAL_PMO_ALERT
                      FOR PJNAVI.PJTM_PJ_SI_DEAL_PMO_ALERT
;
PROMPT SI案件役職別アラート基準マスタ < PJ_MOD.PJTM_PJ_SI_DEAL_PST_ALERT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_SI_DEAL_PST_ALERT
                      FOR PJNAVI.PJTM_PJ_SI_DEAL_PST_ALERT
;
PROMPT アサインメントマスタ < PJ_MOD.PJTM_PJ_ASSIGN > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_ASSIGN
                      FOR PJNAVI.PJTM_PJ_ASSIGN
;
PROMPT データクリーニング情報マスタ < PJ_MOD.PJTM_PJ_DTCL_INFO > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_DTCL_INFO
                      FOR PJNAVI.PJTM_PJ_DTCL_INFO
;
PROMPT プロジェクトアラート基準マスタ < PJ_MOD.PJTM_PJ_PRJ_ALERT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_PRJ_ALERT
                      FOR PJNAVI.PJTM_PJ_PRJ_ALERT
;
PROMPT プロジェクトテンプレートマスタ < PJ_MOD.PJTM_PJ_PRJ_TPL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_PRJ_TPL
                      FOR PJNAVI.PJTM_PJ_PRJ_TPL
;
PROMPT プロジェクト全社PMOアラート基準マスタ < PJ_MOD.PJTM_PJ_PRJ_PMO_ALERT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_PRJ_PMO_ALERT
                      FOR PJNAVI.PJTM_PJ_PRJ_PMO_ALERT
;
PROMPT プロジェクト役職別アラート基準マスタ < PJ_MOD.PJTM_PJ_PRJ_PST_ALERT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_PRJ_PST_ALERT
                      FOR PJNAVI.PJTM_PJ_PRJ_PST_ALERT
;
PROMPT 営業日カレンダーマスタ < PJ_MOD.PJTM_PJ_BIZDAY_CAL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_BIZDAY_CAL
                      FOR PJNAVI.PJTM_PJ_BIZDAY_CAL
;
PROMPT 顧客マスタ < PJ_MOD.PJTM_PJ_CUST > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_CUST
                      FOR PJNAVI.PJTM_PJ_CUST
;
PROMPT 仕入先マスタ < PJ_MOD.PJTM_PJ_SUPP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_SUPP
                      FOR PJNAVI.PJTM_PJ_SUPP
;
PROMPT 支出タイプマスタ < PJ_MOD.PJTM_PJ_SPD_TYP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_SPD_TYP
                      FOR PJNAVI.PJTM_PJ_SPD_TYP
;
PROMPT 支出タイプマッピングマスタ < PJ_MOD.PJTM_PJ_SPD_TYP_MAP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_SPD_TYP_MAP
                      FOR PJNAVI.PJTM_PJ_SPD_TYP_MAP
;
PROMPT 社内レートマスタ < PJ_MOD.PJTM_PJ_INOFFICE_RATE > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_INOFFICE_RATE
                      FOR PJNAVI.PJTM_PJ_INOFFICE_RATE
;
PROMPT 従業員マスタ < PJ_MOD.PJTM_PJ_EMP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_EMP
                      FOR PJNAVI.PJTM_PJ_EMP
;
PROMPT 組織マスタ < PJ_MOD.PJTM_PJ_ORGAN > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_ORGAN
                      FOR PJNAVI.PJTM_PJ_ORGAN
;
PROMPT 派遣単価マスタ < PJ_MOD.PJTM_PJ_DP_UNTPRICE > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_DP_UNTPRICE
                      FOR PJNAVI.PJTM_PJ_DP_UNTPRICE
;
PROMPT 汎用区分マスタ < PJ_MOD.PJTM_PJ_HNYO_SEG > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_HNYO_SEG
                      FOR PJNAVI.PJTM_PJ_HNYO_SEG
;
PROMPT 部門別グレード別単価マスタ < PJ_MOD.PJTM_PJ_SECT_GD_UNTPRICE > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_SECT_GD_UNTPRICE
                      FOR PJNAVI.PJTM_PJ_SECT_GD_UNTPRICE
;
PROMPT WBS別EVM集計SNAPトラン < PJ_MOD.PJTR_PJ_WBS_EVM_SUM_SNAP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_WBS_EVM_SUM_SNAP
                      FOR PJNAVI.PJTR_PJ_WBS_EVM_SUM_SNAP
;
PROMPT タスクリソース別PVトラン < PJ_MOD.PJTR_PJ_TSK_RESRC_DIST_PV > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_TSK_RESRC_DIST_PV
                      FOR PJNAVI.PJTR_PJ_TSK_RESRC_DIST_PV
;
PROMPT タスク別PVトラン < PJ_MOD.PJTR_PJ_TSK_DIST_PV > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_TSK_DIST_PV
                      FOR PJNAVI.PJTR_PJ_TSK_DIST_PV
;
PROMPT プロジェクトコストトラン < PJ_MOD.PJTR_PJ_PRJ_CST > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PRJ_CST
                      FOR PJNAVI.PJTR_PJ_PRJ_CST
;
PROMPT プロジェクト別EVM集計SNAPトラン < PJ_MOD.PJTR_PJ_PRJ_EVM_SUM_SNAP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PRJ_EVM_SUM_SNAP
                      FOR PJNAVI.PJTR_PJ_PRJ_EVM_SUM_SNAP
;
PROMPT 稼働仮コストトラン < PJ_MOD.PJTR_PJ_OPE_TMP_CST > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_OPE_TMP_CST
                      FOR PJNAVI.PJTR_PJ_OPE_TMP_CST
;
PROMPT 外注仕掛品実績トラン < PJ_MOD.PJTR_PJ_OUTS_WKINPR_RSLT > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_OUTS_WKINPR_RSLT
                      FOR PJNAVI.PJTR_PJ_OUTS_WKINPR_RSLT
;
PROMPT 今回報告締WBSタスクSNAPトラン < PJ_MOD.PJTR_PJ_THIS_WBS_TSK_SNAP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_THIS_WBS_TSK_SNAP
                      FOR PJNAVI.PJTR_PJ_THIS_WBS_TSK_SNAP
;
PROMPT 前回報告締WBSタスクSNAPトラン < PJ_MOD.PJTR_PJ_PREV_WBS_TSK_SNAP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_PREV_WBS_TSK_SNAP
                      FOR PJNAVI.PJTR_PJ_PREV_WBS_TSK_SNAP
;
PROMPT 締処理用社員・派遣単価マスタ < PJ_MOD.PJTM_PJ_STF_DP_UNTPRICE > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_PJ_STF_DP_UNTPRICE
                      FOR PJNAVI.PJTM_PJ_STF_DP_UNTPRICE
;
PROMPT 発注先別工数・金額トラン < PJ_MOD.PJTR_PJ_SUPP_MNHOUR_PRICE > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_SUPP_MNHOUR_PRICE
                      FOR PJNAVI.PJTR_PJ_SUPP_MNHOUR_PRICE
;
PROMPT 報告基準日管理トラン < PJ_MOD.PJTR_PJ_RPT_BSDT_MNG > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_RPT_BSDT_MNG
                      FOR PJNAVI.PJTR_PJ_RPT_BSDT_MNG
;
PROMPT WBSタスクテンプレートトラン < PJ_MOD.PJTR_PJ_WBS_TSK_TPL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_WBS_TSK_TPL
                      FOR PJNAVI.PJTR_PJ_WBS_TSK_TPL
;
PROMPT WBSタスクテンプレート依存関係トラン < PJ_MOD.PJTR_PJ_WBS_TSK_TPL_DEP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_WBS_TSK_TPL_DEP
                      FOR PJNAVI.PJTR_PJ_WBS_TSK_TPL_DEP
;
PROMPT WBSタスクトラン < PJ_MOD.PJTR_PJ_WBS_TSK > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_WBS_TSK
                      FOR PJNAVI.PJTR_PJ_WBS_TSK
;
PROMPT WBSタスクリソーストラン < PJ_MOD.PJTR_PJ_WBS_TSK_RESRC > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_WBS_TSK_RESRC
                      FOR PJNAVI.PJTR_PJ_WBS_TSK_RESRC
;
PROMPT WBSタスク依存関係トラン < PJ_MOD.PJTR_PJ_WBS_TSK_DEP > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_WBS_TSK_DEP
                      FOR PJNAVI.PJTR_PJ_WBS_TSK_DEP
;
PROMPT WBSテンプレートトラン < PJ_MOD.PJTR_PJ_WBS_TPL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_WBS_TPL
                      FOR PJNAVI.PJTR_PJ_WBS_TPL
;
PROMPT WBSトラン < PJ_MOD.PJTR_PJ_WBS > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_WBS
                      FOR PJNAVI.PJTR_PJ_WBS
;
PROMPT 汎用ロックワーク < PJ_MOD.PJTW_SY_HNYO_LOCK > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTW_SY_HNYO_LOCK
                      FOR PJNAVI.PJTW_SY_HNYO_LOCK
;
PROMPT 汎用採番管理マスタ < PJ_MOD.PJTM_SY_HNYO_NUM_MNG > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_SY_HNYO_NUM_MNG
                      FOR PJNAVI.PJTM_SY_HNYO_NUM_MNG
;
PROMPT ボタンマスタ < PJ_MOD.PJTM_SY_BTN > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_SY_BTN
                      FOR PJNAVI.PJTM_SY_BTN
;
PROMPT 参照権限マスタ < PJ_MOD.PJTM_SY_REF_AUTH > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_SY_REF_AUTH
                      FOR PJNAVI.PJTM_SY_REF_AUTH
;
PROMPT 従業員割当職責マスタ < PJ_MOD.PJTM_SY_EMP_ASSIGN_RES > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_SY_EMP_ASSIGN_RES
                      FOR PJNAVI.PJTM_SY_EMP_ASSIGN_RES
;
PROMPT 職責マスタ < PJ_MOD.PJTM_SY_RES > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTM_SY_RES
                      FOR PJNAVI.PJTM_SY_RES
;
PROMPT 発注依頼トラン < PJ_MOD.PJTR_PJ_SHIP_REQ > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJTR_PJ_SHIP_REQ
                      FOR PJNAVI.PJTR_PJ_SHIP_REQ
;
PROMPT タスクトラン連結ビュー < PJ_MOD.PJVR_MI_TSK_UNI > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJVR_MI_TSK_UNI
                      FOR PJNAVI.PJVR_MI_TSK_UNI
;
PROMPT プロジェクトトラン連結ビュー < PJ_MOD.PJVR_MI_PRJ_UNI > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJVR_MI_PRJ_UNI
                      FOR PJNAVI.PJVR_MI_PRJ_UNI
;
PROMPT プロジェクトメンバトラン連結ビュー < PJ_MOD.PJVR_MI_PRJ_MBR_UNI > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJVR_MI_PRJ_MBR_UNI
                      FOR PJNAVI.PJVR_MI_PRJ_MBR_UNI
;
PROMPT セッションワークリンクビュー < PJ_MOD.PJVW_SY_SESSION_LNK > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJVW_SY_SESSION_LNK
                      FOR PJNAVI.PJVW_SY_SESSION_LNK
;
PROMPT セッション管理テーブルビュー < PJ_MOD.PJVW_SY_SESSION_MNG_TBL > シノニム再作成
CREATE OR REPLACE SYNONYM PJ_MOD.PJVW_SY_SESSION_MNG_TBL
                      FOR PJNAVI.PJVW_SY_SESSION_MNG_TBL
;
SPOOL OFF
EXIT
