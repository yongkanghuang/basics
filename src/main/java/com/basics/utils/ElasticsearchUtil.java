package com.basics.utils;

import java.util.List;

/**
 * @Description elasticsearch 工具
 * @Author hyk
 * @Date 2019/1/25 11:42
 **/
public class ElasticsearchUtil {

    /**
     * 根据时间获取索引
     * @param indexPrefix
     * @param startTime
     * @param endTime
     * @return
     */
    public static String[] getIndexNames(String indexPrefix,String startTime,String endTime){
        List<String> dateList = DateUtil.getPeriodDates(startTime,endTime);
        int dateSize = dateList.size();
        String[] indexArr = new String[dateSize];
        for (int i=0;i<dateSize;i++){
            indexArr[i]=indexPrefix+"_"+dateList.get(i);
        }
        return indexArr;
    }
}
