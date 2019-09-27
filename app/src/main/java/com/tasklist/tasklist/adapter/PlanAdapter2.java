package com.tasklist.tasklist.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.tasklist.tasklist.EditActivity;
import com.tasklist.tasklist.IItemTouchHelperAdapter;
import com.tasklist.tasklist.MainActivity;
import com.tasklist.tasklist.R;
import com.tasklist.tasklist.entity.Plan2;

import org.litepal.LitePal;

import java.util.Collections;
import java.util.List;

public class PlanAdapter2 extends RecyclerView.Adapter<PlanAdapter2.VH> implements IItemTouchHelperAdapter {
        private Context context;
        private List<Plan2> plan;
        private int flag;
    private OnItemClickListener onItemClickListener;
    public PlanAdapter2(List<Plan2> plan){
        this.plan = plan;
        }//获取数据
        public static class VH extends RecyclerView.ViewHolder{//实例化控件
            public final CheckBox checkBox;
            public final TextView title;
            public final TextView date;
            public final TextView time;
            private VH(View v){
                   super(v);
                   checkBox = v.findViewById(R.id.checkbox);
                   title = v.findViewById(R.id.text);
                   date = v.findViewById(R.id.tv_date);
                   time = v.findViewById(R.id.tv_time);
            }
        }


        @Override
        public void onBindViewHolder(@NonNull final VH vh, final int i) {//加载数据
            LitePal.getDatabase();
            vh.title.setText(plan.get(i).getTitle());//设置标题
            vh.checkBox.setChecked(plan.get(i).getIsCompleted());//设置checkbox
            vh.itemView.setOnClickListener(new View.OnClickListener() {//设置itemView点击事件
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EditActivity.class);//v.getContext获取上下文
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag",0x00000006);
                    bundle.putString("title",plan.get(i).getTitle());
                    bundle.putString("text",plan.get(i).getText());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                    Activity activity = (Activity) v.getContext();
                    activity.finish();
                }
            });
            vh.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (plan.get(i).getIsCompleted() ==false){
                        plan.get(i).setIsCompleted(true);
                        plan.get(i).save();
                    }
                    else {
                        plan.get(i).setIsCompleted(false);
                        plan.get(i).save();
                    }
                }
            });
            String date1 = plan.get(i).getDate1();
            String date2 = plan.get(i).getDate2();
            vh.date.setText(date1);//年月日
            vh.time.setText(date2);//时间
        }

        @Override
        public int getItemCount() {
                return plan.size();
        }//计算Item数目

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {//获取View
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
                return new VH(v);
        }
    @Override

    public void onItemMove(int fromPosition, int toPosition) {//上下移动Item

        Collections.swap(plan, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);

    }

    @Override

    public void onItemDismiss(int position) {//侧滑删除Item
        LitePal.deleteAll(Plan2.class,"title = ?",plan.get(position).getTitle());//从数据库删除
        plan.remove(position);//从plan数组删除
        notifyItemRemoved(position);//更新

    }
    //定义点击接口
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    //点击方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
