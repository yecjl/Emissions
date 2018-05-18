package com.study.emissions;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.NumberFormat;

/**
 * 自定义图表的MarkerView(点击坐标点，弹出提示框)
 */
public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    /**
     *
     * @param context
     *            上下文
     * @param layoutResource
     *            资源文件
     */
    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // 显示布局中的文本框
        tvContent = findViewById(R.id.tvContent);
    }

    // 每次markerview回调重绘，可以用来更新内容
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        // 设置Y周数据源对象Entry的value值为显示的文本内容
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);

        Bundle data = (Bundle) e.getData();
        tvContent.setText("当前" + data.getString("title")  + ": "+ nf.format(e.getY()));
        tvContent.setTextColor(data.getInt("color"));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}