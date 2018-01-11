package com.math.yang.mathyang.orm;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.math.yang.mathyang.download.DownloadService;
import com.math.yang.mathyang.model.DownloadBean;
import com.math.yang.mathyang.model.UnitVideo;
import com.math.yang.mathyang.util.LL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtx on 2018/1/10.
 */

public class DownloadDbUtil {
    /**
     * 根据BookId查找图书
     *
     * @param downloadId 书本ID
     * @return 返回图书实体
     */
    public static DownloadBean getDownloadDataById(String downloadId) {
        try {
            if (MathOrm.getLiteOrm().queryById(downloadId, DownloadBean.class) == null) {
                return null;
            }
            return MathOrm.getLiteOrm().queryById(downloadId, DownloadBean.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据BookId查找图书
     *
     * @return 返回图书实体
     */
    public static List<DownloadBean> getDownloadingList() {
        List<DownloadBean> list = MathOrm.getLiteOrm()
                .query(new QueryBuilder<>(DownloadBean.class)
                        .where(DownloadBean.DOWNLOAD_STATUS + "!= ?",
                                new Integer[]{DownloadService.DOWNLOAD_NOT_YET}));
                      /*  .whereOr(DownloadBean.DOWNLOAD_STATUS + "= ?",
                                new Integer[]{DownloadService.DOWNLOAD_NOT_YET}));*/
        return list;
    }

    /**
     * 根据BookId查找图书
     *
     * @return 返回图书实体
     */
    public static List<UnitVideo> getDownloadingUnitList() {
        List<UnitVideo> list = new ArrayList<>();
        List<DownloadBean> downloadList = getDownloadingList();
        for (DownloadBean download : downloadList) {
            list.add(UnitDbUtil.getUnitById(download.getDownloadId()));
        }
        return list;
    }

    /**
     * 有则更新
     *
     * @param data
     */
    public static void saveDownload(DownloadBean data) {
        if (data == null) {
            return;
        }
        if (MathOrm.getLiteOrm().queryById(data.getDownloadId(), DownloadBean.class) != null) {
            MathOrm.getLiteOrm().update(data);
        } else {
            MathOrm.getLiteOrm().save(data);
            if (MathOrm.getLiteOrm().queryById(data.getDownloadId(), DownloadBean.class) == null) {
                LL.ztx("存了还是空的");
            }
        }
    }
}
