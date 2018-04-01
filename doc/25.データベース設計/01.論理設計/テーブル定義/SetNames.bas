Attribute VB_Name = "SetNames"
Sub SetNames()
Attribute SetNames.VB_ProcData.VB_Invoke_Func = " \n14"
'
' Name setting Macro
'

'
Const N_CON_NAME = "ContentsName"
Const N_CRE_DATE = "CreateDate"
Const N_DATA_TYPE = "dataType"
Const N_DEF = "default"
Const N_DELI_NAME = "DeliverableName"
Const N_DIGIT = "digit"
Const N_FOREIGN_ATTR = "foreignKeyAttributes"
Const N_FOREIGN_TBL = "foreignKeyTable"
Const N_LOGI_ATTR = "logicalAttributes"
Const N_LOGI_TBL = "logicalTable"
Const N_MOD_DATE = "ModifyDate"
Const N_NOT_NULL = "notNull"
Const N_PHYS_ATTR = "physicalAttributes"
Const N_PHYS_TBL = "physicalTable"
Const N_PRECISION = "precision"
Const N_PRI_KEY = "primaryKey"
Const N_SCHEMA_NAME = "schemaName"
Const N_SYS_NAME = "SystemName"
Const N_TBL_DEF_NO = "tableDefinitionNo"
Const N_TITLE = "Title"
Const N_UNIQUE = "unique"
Const N_DOM = "Domain"
Const N_DOMG = "DomainGroup"
Const N_IDX = "index"

On Error Resume Next

    ActiveWorkbook.Names(N_CON_NAME).Delete
    ActiveWorkbook.Names(N_CRE_DATE).Delete
    ActiveWorkbook.Names(N_DATA_TYPE).Delete
    ActiveWorkbook.Names(N_DEF).Delete
    ActiveWorkbook.Names(N_DELI_NAME).Delete
    ActiveWorkbook.Names(N_DIGIT).Delete
    ActiveWorkbook.Names(N_FOREIGN_ATTR).Delete
    ActiveWorkbook.Names(N_FOREIGN_TBL).Delete
    ActiveWorkbook.Names(N_LOGI_ATTR).Delete
    ActiveWorkbook.Names(N_LOGI_TBL).Delete
    ActiveWorkbook.Names(N_MOD_DATE).Delete
    ActiveWorkbook.Names(N_NOT_NULL).Delete
    ActiveWorkbook.Names(N_PHYS_ATTR).Delete
    ActiveWorkbook.Names(N_PHYS_TBL).Delete
    ActiveWorkbook.Names(N_PRECISION).Delete
    ActiveWorkbook.Names(N_PRI_KEY).Delete
    ActiveWorkbook.Names(N_SCHEMA_NAME).Delete
    ActiveWorkbook.Names(N_SYS_NAME).Delete
    ActiveWorkbook.Names(N_TBL_DEF_NO).Delete
    ActiveWorkbook.Names(N_TITLE).Delete
    ActiveWorkbook.Names(N_UNIQUE).Delete
    ActiveWorkbook.Names(N_IDX).Delete
    ActiveWorkbook.Names(N_DOMG).Delete
    ActiveWorkbook.Names(N_DOM).Delete

    
    With ActiveWorkbook.Worksheets(1)
        .Names(N_CON_NAME).Delete
        .Names(N_CRE_DATE).Delete
        .Names(N_DATA_TYPE).Delete
        .Names(N_DEF).Delete
        .Names(N_DELI_NAME).Delete
        .Names(N_DIGIT).Delete
        .Names(N_FOREIGN_ATTR).Delete
        .Names(N_FOREIGN_TBL).Delete
        .Names(N_LOGI_ATTR).Delete
        .Names(N_LOGI_TBL).Delete
        .Names(N_MOD_DATE).Delete
        .Names(N_NOT_NULL).Delete
        .Names(N_PHYS_ATTR).Delete
        .Names(N_PHYS_TBL).Delete
        .Names(N_PRECISION).Delete
        .Names(N_PRI_KEY).Delete
        .Names(N_SCHEMA_NAME).Delete
        .Names(N_SYS_NAME).Delete
        .Names(N_TBL_DEF_NO).Delete
        .Names(N_TITLE).Delete
        .Names(N_UNIQUE).Delete
        .Names(N_IDX).Delete
        .Names(N_DOMG).Delete
        .Names(N_DOM).Delete
    
        .Names.Add Name:=N_CON_NAME, RefersTo:="='" & .Name & "'!$M$1"
        .Names.Add Name:=N_CRE_DATE, RefersTo:="='" & .Name & "'!$BG$2"
        .Names.Add Name:=N_DATA_TYPE, RefersTo:="='" & .Name & "'!$X$6"
        .Names.Add Name:=N_DEF, RefersTo:="='" & .Name & "'!$AP$6"
        .Names.Add Name:=N_DELI_NAME, RefersTo:="='" & .Name & "'!$BG$1"
        .Names.Add Name:=N_DIGIT, RefersTo:="='" & .Name & "'!$AB$6"
        .Names.Add Name:=N_FOREIGN_ATTR, RefersTo:="='" & .Name & "'!$AY$7"
        .Names.Add Name:=N_FOREIGN_TBL, RefersTo:="='" & .Name & "'!$AU$7"
        .Names.Add Name:=N_LOGI_ATTR, RefersTo:="='" & .Name & "'!$B$6"
        .Names.Add Name:=N_LOGI_TBL, RefersTo:="='" & .Name & "'!$AB$4"
        .Names.Add Name:=N_MOD_DATE, RefersTo:="='" & .Name & "'!$BQ$2"
        .Names.Add Name:=N_NOT_NULL, RefersTo:="='" & .Name & "'!$AF$6"
        .Names.Add Name:=N_PHYS_ATTR, RefersTo:="='" & .Name & "'!$M$6"
        .Names.Add Name:=N_PHYS_TBL, RefersTo:="='" & .Name & "'!$AP$4"
        .Names.Add Name:=N_PRECISION, RefersTo:="='" & .Name & "'!$AD$6"
        .Names.Add Name:=N_PRI_KEY, RefersTo:="='" & .Name & "'!$AH$6"
        .Names.Add Name:=N_SCHEMA_NAME, RefersTo:="='" & .Name & "'!$P$4"
        .Names.Add Name:=N_SYS_NAME, RefersTo:="='" & .Name & "'!$A$1"
        .Names.Add Name:=N_TBL_DEF_NO, RefersTo:="='" & .Name & "'!$A$6"
        .Names.Add Name:=N_TITLE, RefersTo:="='" & .Name & "'!$A$3"
        .Names.Add Name:=N_UNIQUE, RefersTo:="='" & .Name & "'!$AJ$6"
        .Names.Add Name:=N_IDX, RefersTo:="='" & .Name & "'!$AK$6"
        .Names.Add Name:=N_DOMG, RefersTo:="='" & .Name & "'!$CF$6"
        .Names.Add Name:=N_DOM, RefersTo:="='" & .Name & "'!$CJ$6"
    
    End With

End Sub
