-- 2013/10/18 インデックス追加
-- 営業日カレンダーマスタ [ 営業日通番 ]にインデックスを付与
COMMENT ON TABLE PJTM_PJ_BIZDAY_CAL IS '営業日カレンダーマスタ(ver.0.4)';
CREATE INDEX IX1_PJTM_PJ_BIZDAY_CAL ON PJTM_PJ_BIZDAY_CAL (
    BIZDAY_SN 
);
                                                                                                                                                                                                                                                                               