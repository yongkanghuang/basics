package com.basics.utils;

import com.basics.base.es.MappingBuilder;
import com.basics.es.entity.EsUser;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description 时间工具
 * @Author hyk
 * @Date 2019/1/25 10:07
 **/
public class DateUtil {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyyMMdd";


    /**
     * 获取某一段时间的日期
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getPeriodDates(String startTime, String endTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.YYYY_MM_DD_HH_MM_SS);
        SimpleDateFormat reDataFormat = new SimpleDateFormat(DateUtil.YYYY_MM_DD);
        List<String> dateList = new ArrayList<>();
        try {

            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            String reStartTime = reDataFormat.format(startDate);
            String reEndTime = reDataFormat.format(endDate);
            dateList.add(reStartTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reDataFormat.parse(reStartTime));

            while (reDataFormat.parse(reEndTime).after(calendar.getTime())) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                dateList.add(reDataFormat.format(calendar.getTime()));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateList;
    }

    public static void main(String[] args){
       String[] dl = ElasticsearchUtil.getIndexNames("user","2019-01-23 00:20:02","2019-01-24 23:20:02");
        System.out.println("ss");
        for (String d:dl){
            System.out.println(d);
        }
        Field[] fs = MappingBuilder.retrieveFields(EsUser.class);
        EsUser esUser = new EsUser();
        esUser.setId("1002");
        for (int i = 0; i < fs.length; i++) {
            try {
                // 将目标属性设置为可以访问
                fs[i].setAccessible(true);
                System.out.println(fs[i].getName()+":"+fs[i].get(esUser));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
