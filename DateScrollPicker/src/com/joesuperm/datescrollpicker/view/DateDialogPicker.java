/**
 * @项目名称：DateScrollPicker
 * @文件名：DateDialogPicker.java
 * @版本信息：
 * @日期：2015年7月30日
 * @Copyright 2015 www.517na.com Inc. All rights reserved.
 */
package com.joesuperm.datescrollpicker.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.joesuperm.datescrollpicker.R;
import com.joesuperm.datescrollpicker.view.WheelView.OnWheelViewListener;

/**
 * @项目名称：DateScrollPicker
 * @类名称：DateDialogPicker
 * @类描述：
 * @创建人：huaiying
 * @创建时间：2015年7月30日 下午7:25:07
 * @修改人：huaiying
 * @修改时间：2015年7月30日 下午7:25:07
 * @修改备注：
 * @version
 */
public class DateDialogPicker extends Dialog implements android.view.View.OnClickListener {
    
    /** 三个用于显示时间的滚轮视图 */
    private WheelView mWvDate, mWvHour, mWvMinute;
    
    /** 底部确定按钮 */
    private Button mBtnSure;
    
    /** 当前用户选中选中的日期 */
    private Calendar mSourceCalendar = null;
    
    /** 最小日期，即选择时间不能比这个日期小 */
    private Calendar mMinCalendar = null;
    
    /** 记录用户最终选择的日期 */
    private Calendar mDstCalendar = null;
    
    /** 当前日期，不得更改 */
    private Calendar mCurCalendar = null;
    
    /** 日期数据源 */
    private List<String> mDateList = null;
    
    /** 小时数据源 */
    private List<String> mHourList = null;
    
    /** 分钟数据源 */
    private List<String> mMinuteList = null;
    
    /** 监听器对象 */
    private OnDateSelectListener mDateSelectListener = null;
    
    /** 标识日期是否在最小日期 */
    private boolean mIsNeedInitAll = false;
    
    /** 标识是否当前处于最小日期 */
    private boolean mIsCurMinDate = false;
    
    /** 最大天数 */
    private static final int MAX_DATE = 30;
    
    /**
     * 创建一个新的实例 DateDialogPicker.
     * 
     * @param context
     */
    public DateDialogPicker(Context context) {
        super(context);
    }
    
    /**
     * 创建一个新的实例 DateDialogPicker.
     */
    public DateDialogPicker(Context context, int theme) {
        super(context, theme);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_picker);
        mCurCalendar = Calendar.getInstance();
        mWvDate = (WheelView) findViewById(R.id.wv_dialog_date);
        mWvHour = (WheelView) findViewById(R.id.wv_dialog_hour);
        mWvMinute = (WheelView) findViewById(R.id.wv_dialog_minutes);
        mBtnSure = (Button) findViewById(R.id.btn_dialog_sure);
        mBtnSure.setOnClickListener(this);
        initData();
        setTitle("请选择日期");
    }
    
    /**
     * @description 初始化基本日期对象和监听器
     * @date 2015年7月31日
     * @param curCalendar
     *            当前需要选中的日期对象
     * @param minCalendar
     *            最小日期对象，即可选择日期不能小于改对象
     * @param listener
     *            日期选中监听器
     */
    public void setCalendar(Calendar curCalendar, Calendar minCalendar, OnDateSelectListener listener) {
        this.mSourceCalendar = curCalendar;
        if (curCalendar.getTimeInMillis() < minCalendar.getTimeInMillis()) {
            this.mMinCalendar = curCalendar;
        }
        else {
            this.mMinCalendar = minCalendar;
        }
        this.mDstCalendar = curCalendar;
        this.mDateSelectListener = listener;
    }
    
    /** 初始化滚轮数据 */
    private void initData() {
        // 设置偏移量
        mWvDate.setOffset(1);
        mWvHour.setOffset(1);
        mWvMinute.setOffset(1);
        // 初始化日期、小时、分钟的数据源对象
        mHourList = new ArrayList<String>();
        mMinuteList = new ArrayList<String>();
        mDateList = new ArrayList<String>();
        // 初始化小时
        initHourDate(0);
        // 初始化分钟
        initMinuteDate(0);
        // 初始化日期数据
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mMinCalendar.getTime());
        for (int i = 0; i < MAX_DATE; i++) {
            mDateList.add(getDate(calendar));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        mWvDate.setItems(mDateList);
        setListener();
        restoreSelectDate(mSourceCalendar);
    }
    
    /**
     * @description 根据日历对象恢复显示数据
     * @date 2015年7月30日
     */
    private void restoreSelectDate(Calendar calendar) {
        // 恢复开始数据
        int dateIndex = mDateList.indexOf(getDate(calendar));
        mWvDate.setSeletion(dateIndex);
        // 在数据List中查找对应的小时是否存在，存在则选中，不存在则显示第一个并记录日期
        int hourIndex = mHourList.indexOf(getNumber(calendar.get(Calendar.HOUR_OF_DAY)));
        if (hourIndex == -1) {
            hourIndex = 0;
            mDstCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(mHourList.get(0)));
        }
        mWvHour.setSeletion(hourIndex);
        // 在数据List中查找对应的分钟是否存在，存在则选中，不存在则显示第一个并记录日期
        int minuteIndex = mMinuteList.indexOf(getNumber(calendar.get(Calendar.MINUTE)));
        if (minuteIndex == -1) {
            mDstCalendar.set(Calendar.MINUTE, Integer.valueOf(mMinuteList.get(0)));
            minuteIndex = 0;
        }
        mWvMinute.setSeletion(minuteIndex);
    }
    
    /**
     * @description 从index位置开始初始化小时数据
     * @date 2015年7月30日
     */
    private void initHourDate(int index) {
        // 初始化时间
        mHourList.clear();
        for (int i = index; i < 24; i++) {
            mHourList.add(getNumber(i));
        }
        // 设置数据
        mWvHour.setItems(mHourList);
    }
    
    /**
     * @description 从index位置开始初始化分钟数据
     * @date 2015年7月30日
     */
    private void initMinuteDate(int index) {
        // 初始化分钟
        mMinuteList.clear();
        for (int i = index; i < 60; i++) {
            mMinuteList.add(getNumber(i));
        }
        // 设置数据
        mWvMinute.setItems(mMinuteList);
    }
    
    /** 获取数字的两位表示 供小时和分钟使用 */
    private String getNumber(int num) {
        if (num < 10) {
            return "0" + num;
        }
        else {
            return String.valueOf(num);
        }
    }
    
    /**
     * @description 设置滚轮的监听事件
     * @date 2015年7月30日
     */
    private void setListener() {
        // 设置监听事件
        mWvDate.setOnWheelViewListener(new OnWheelViewListener() {
            
            @Override
            public void onSelected(int selectedIndex, String item) {
                // 8月20日 星期几
                int mounthIndex = item.indexOf("月");
                int dateIndex = item.indexOf("日");
                int realMouth = Integer.valueOf(item.substring(0, mounthIndex));
                int realDate = Integer.valueOf(item.substring(mounthIndex + 1, dateIndex));
                if (mCurCalendar.get(Calendar.MONTH) > realMouth) {
                    mDstCalendar.add(Calendar.YEAR, 1);
                }
                mDstCalendar.set(Calendar.MONTH, realMouth - 1);
                mDstCalendar.set(Calendar.DAY_OF_MONTH, realDate);
                if (realMouth == mMinCalendar.get(Calendar.MONTH) + 1 && realDate == mMinCalendar.get(Calendar.DAY_OF_MONTH)) {
                    initHourDate(mMinCalendar.get(Calendar.HOUR_OF_DAY));
                    if (mDstCalendar.get(Calendar.HOUR_OF_DAY) > mMinCalendar.get(Calendar.HOUR_OF_DAY)) {
                        initMinuteDate(0);
                    }
                    else {
                        initMinuteDate(mMinCalendar.get(Calendar.MINUTE));
                    }
                    restoreSelectDate(mDstCalendar);
                    mIsNeedInitAll = true;
                    mIsCurMinDate = true;
                }
                else {
                    mIsCurMinDate = false;
                    if (mIsNeedInitAll) {
                        // 初始化时间
                        initHourDate(0);
                        // 初始化分钟
                        initMinuteDate(0);
                        restoreSelectDate(mDstCalendar);
                        mIsNeedInitAll = false;
                    }
                }
            }
            
        });
        mWvHour.setOnWheelViewListener(new OnWheelViewListener() {
            
            @Override
            public void onSelected(int selectedIndex, String item) {
                mDstCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(item));
                if (mIsCurMinDate) {
                    if (Integer.valueOf(item) > mMinCalendar.get(Calendar.HOUR_OF_DAY)) {
                        initMinuteDate(0);
                    }
                    else {
                        initMinuteDate(mMinCalendar.get(Calendar.MINUTE));
                    }
                    restoreSelectDate(mDstCalendar);
                }
            }
        });
        mWvMinute.setOnWheelViewListener(new OnWheelViewListener() {
            
            @Override
            public void onSelected(int selectedIndex, String item) {
                mDstCalendar.set(Calendar.MINUTE, Integer.valueOf(item));
            }
        });
    }
    
    /** 获取日期的显示方式 [xx月xx日 星期一/明天] */
    private String getDate(Calendar calendar) {
        String format = "%s月%s日 %s";
        String week = null;
        if (calendar.get(Calendar.DAY_OF_MONTH) == mCurCalendar.get(Calendar.DAY_OF_MONTH)) {
            week = "今天";
        }
        else if (calendar.get(Calendar.DAY_OF_MONTH) == mCurCalendar.get(Calendar.DAY_OF_MONTH) + 1) {
            week = "明天";
        }
        else if (calendar.get(Calendar.DAY_OF_MONTH) == mCurCalendar.get(Calendar.DAY_OF_MONTH) + 2) {
            week = "后天";
        }
        else {
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            switch (weekday) {
                case Calendar.SUNDAY:
                    week = "星期日";
                    break;
                case Calendar.MONDAY:
                    week = "星期一";
                    break;
                case Calendar.TUESDAY:
                    week = "星期二";
                    break;
                case Calendar.WEDNESDAY:
                    week = "星期三";
                    break;
                case Calendar.THURSDAY:
                    week = "星期四";
                    break;
                case Calendar.FRIDAY:
                    week = "星期五";
                    break;
                case Calendar.SATURDAY:
                    week = "星期六";
                    break;
            }
        }
        return String.format(format,
                             String.valueOf(calendar.get(Calendar.MONTH) + 1),
                             String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
                             week);
    }
    
    @Override
    public void onClick(View v) {
        if (mDateSelectListener != null) {
            mDateSelectListener.onSelect(mDstCalendar);
        }
        dismiss();
    }
    
    /**
     * @项目名称：DateScrollPicker
     * @类名称：OnDateSelectListener
     * @类描述： 日期选择接口
     * @创建人：huaiying
     * @创建时间：2015年7月30日 下午9:47:00
     * @修改人：huaiying
     * @修改时间：2015年7月30日 下午9:47:00
     * @修改备注：
     * @version
     */
    public interface OnDateSelectListener {
        /** 选中函数 */
        void onSelect(Calendar calendar);
    }
}
