package com.joesuperm.datescrollpicker.utils;

import android.content.Context;
import android.view.View;

/**
 * @项目名称：DateScrollPicker
 * @类名称：DeviceUitls
 * @类描述：
 * @创建人：huaiying
 * @创建时间：2015年7月30日 下午7:17:15
 * @修改人：huaiying
 * @修改时间：2015年7月30日 下午7:17:15
 * @修改备注：
 * @version
 */
public class DeviceUitls {
    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }
    
    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }
    
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
