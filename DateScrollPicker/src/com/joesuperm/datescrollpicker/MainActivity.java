package com.joesuperm.datescrollpicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.joesuperm.datescrollpicker.view.DateDialogPicker;
import com.joesuperm.datescrollpicker.view.DateDialogPicker.OnDateSelectListener;

public class MainActivity extends Activity {
    
    private TextView mTxtContent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtContent = (TextView) findViewById(R.id.tv_content);
    }
    /**显示对话框点击事件*/
    public void onShowDialog(View v) {
        //构造对话框
        DateDialogPicker dateDialogPicker = new DateDialogPicker(this);
        
        //对话框默认显示的日期
        Calendar calendarSelect = Calendar.getInstance();
        calendarSelect.add(Calendar.DAY_OF_MONTH, 10);
        //对话框可选择的最小日期
        Calendar calendarMin = Calendar.getInstance();
        calendarMin.add(Calendar.DAY_OF_MONTH, 5);
        //设置监听器并初始化
        dateDialogPicker.setCalendar(calendarSelect, calendarMin, new OnDateSelectListener() {
            
            @Override
            public void onSelect(Calendar calendar) {
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.getTime());
                mTxtContent.setText("\n当前选中日期为："+date);
                Toast.makeText(getApplicationContext(), "时间：" + date, Toast.LENGTH_LONG).show();
            }
        });
        //显示对话框
        dateDialogPicker.show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
