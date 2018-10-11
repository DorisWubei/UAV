package com.projects.wu.wateruav.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.wu.wateruav.R;
import com.projects.wu.wateruav.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {


    //日期数组
    private int[] date = {
            R.string.date1, R.string.date2, R.string.date3, R.string.date4, R.string.date5,
            R.string.date6, R.string.date7, R.string.date8, R.string.date9, R.string.date10,
            R.string.date11
    };

    //时间数组
    private int[] time = {
            R.string.time1, R.string.time2, R.string.time3, R.string.time4, R.string.time5,
            R.string.time6, R.string.time7, R.string.time8, R.string.time9, R.string.time10,
            R.string.time11
    };

    //溶氧数组
    private int[] o = {
            R.string.o1, R.string.o2, R.string.o3, R.string.o4, R.string.o5,
            R.string.o6, R.string.o7, R.string.o8, R.string.o9, R.string.o10,
            R.string.o11
    };


    //氮氨数组
    private int[] n = {
            R.string.n1, R.string.n2, R.string.n3, R.string.n4, R.string.n5,
            R.string.n6, R.string.n7, R.string.n8, R.string.n9, R.string.n10,
            R.string.n11
    };


    //PH数组
    private int[] ph = {
            R.string.ph1, R.string.ph2, R.string.ph3, R.string.ph4, R.string.ph5,
            R.string.ph6, R.string.ph7, R.string.ph8, R.string.ph9, R.string.ph10,
            R.string.ph11
    };
    private Context lContent;//定义上下文

    //集合
    private List<Integer> listDate = new ArrayList<>();
    private List<Integer> listTime = new ArrayList<>();
    private List<Integer> listo = new ArrayList<>();
    private List<Integer> listn = new ArrayList<>();
    private List<Integer> listPH = new ArrayList<>();

    public Adapter(Context lContent) {
        this.lContent = lContent;
        //设置菜单行数与行内日期、时间、溶氧、氮氨、PH
        for (int i = 0; i < 11; i++) {
            listDate.add(date[i]);
            listTime.add(time[i]);
            listo.add(o[i]);
            listn.add(n[i]);
            listPH.add(ph[i]);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //获取列表中每行的布局文件
        View view = LayoutInflater.from(lContent).inflate(R.layout.layout_item, parent, false);
        return new MyViewHolder(view);
    }

    //设置列表中行所显示的内容
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //设置日期
        holder.date.setText(listDate.get(position));
        //设置时间
        holder.time.setText(listTime.get(position));
        //设置溶氧
        holder.o.setText(listo.get(position));
        //设置氮氨
        holder.n.setText(listn.get(position));
        //设置PH
        holder.ph.setText(listPH.get(position));
        //设置内容宽度为屏幕的宽度
        holder.layout_content.getLayoutParams().width = Utils.getScreenWidth(lContent);

        //删除按钮的方法
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();//获取要删除行的位置
                removeData(n);//删除列表中指定的行
            }
        });
    }

    //返回行的总数
    @Override
    public int getItemCount() {
        return listDate.size();
    }

    //删除列表行中信息的方法
    public void removeData(int position){
        listDate.remove(position);//删除行中日期
        listTime.remove(position);//删除时间
        listo.remove(position);//删除溶氧
        listn.remove(position);//删除氮氨
        listPH.remove(position);//删除PH
        notifyItemRemoved(position);//删除行
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_delete;
        public TextView date, time, o, n, ph;//日期，时间，溶氧，氮氨与PH
        public ViewGroup layout_content;//图标与信息布局

        //获取控件
        public MyViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            o = itemView.findViewById(R.id.o2);
            n = itemView.findViewById(R.id.nh3);
            ph = itemView.findViewById(R.id.ph);
            layout_content = itemView.findViewById(R.id.layout_content);
            btn_delete = itemView.findViewById(R.id.tv_delete);
        }
    }
}
