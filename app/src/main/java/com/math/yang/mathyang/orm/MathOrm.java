package com.math.yang.mathyang.orm;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

/**
 * Created by ZTX on 2016/6/17.
 * 数据库操作
 * @author  ztx
 */
public class MathOrm {
    /**
     * 数据库
     */
    private static LiteOrm liteOrm;

    /**
     * 初始化
     *
     * @param config 配置
     */
    public static void init(DataBaseConfig config) {
        liteOrm = LiteOrm.newSingleInstance(config);
    }

    /**
     * 单例获得
     *
     * @return 返回单例
     */
    public static LiteOrm getLiteOrm() {
        return liteOrm;
    }
}
