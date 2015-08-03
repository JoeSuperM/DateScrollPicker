# DateScrollPicker
实现滚动选择日期，自动计算日期的转换、支持给定最小日期和当前选中日期的调用。  

## 调用方式：  
  
    
    
        //构造对话框
        DateDialogPicker dateDialogPicker = new DateDialogPicker(this);
        
        //对话框默认显示的日期
        Calendar calendarSelect = Calendar.getInstance();
        calendarSelect.add(Calendar.DAY_OF_MONTH, 6);
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
          
          
            
# 效果演示图如下：

![](https://raw.githubusercontent.com/JoeSuperM/DateScrollPicker/master/DateScrollPicker/demo.png)
