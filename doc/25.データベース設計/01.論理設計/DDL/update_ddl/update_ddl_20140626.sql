/*内部開発EACの項目を追加*/
ALTER TABLE PJTR_PJ_PRJ_EVM_SUM_SNAP ADD(
    INDEV_EAC           NUMBER(10,2),
    INDEV_EAC_PRICE     NUMBER(12),
    INDEV_EAC2_PRICE    NUMBER(12)
);
