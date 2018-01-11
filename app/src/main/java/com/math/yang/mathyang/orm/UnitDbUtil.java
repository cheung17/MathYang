package com.math.yang.mathyang.orm;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.math.yang.mathyang.download.DownloadService;
import com.math.yang.mathyang.model.DownloadBean;
import com.math.yang.mathyang.model.UnitVideo;

import java.util.List;

/**
 * Created by zhangtx on 2018/1/10.
 */

public class UnitDbUtil {

    /**
     * 根据BookId查找图书
     *
     * @param unitId 书本ID
     * @return 返回图书实体
     */
    public static UnitVideo getUnitById(String unitId) {
        try {
            if (MathOrm.getLiteOrm().queryById(unitId, UnitVideo.class) == null) {
                return null;
            }
            return MathOrm.getLiteOrm().queryById(unitId, UnitVideo.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据BookId查找图书
     *
     * @param unitVideo 书本ID
     * @return 返回图书实体
     */
    public static void saveUnit(UnitVideo unitVideo) {
        if (unitVideo == null) {
            return;
        }
        if (MathOrm.getLiteOrm().queryById(unitVideo.getId(), UnitVideo.class) != null) {
            MathOrm.getLiteOrm().update(unitVideo);
        } else {
            MathOrm.getLiteOrm().save(unitVideo);
        }
    }

    public static void saveList(List<UnitVideo> list) {
        for (UnitVideo unitVideo : list) {
            saveUnit(unitVideo);
        }
    }
    /**
     * 根据BookId查找图书
     *
     * @return 返回图书实体
     */
    public static List<UnitVideo> getDownloadingList() {
        List<UnitVideo> list = MathOrm.getLiteOrm()
                .query(new QueryBuilder<>(UnitVideo.class)
                        .where(UnitVideo.DOWNLOAD_STATUS + "= ? ",
                                new Integer[]{DownloadService.DOWNLOAD_ING})
                );
        return list;
    }
}
