package com.tasklist.tasklist;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private IItemTouchHelperAdapter mAdapter;

    public MyItemTouchHelperCallback(IItemTouchHelperAdapter mAdapter) {

        this.mAdapter = mAdapter;

    }

    @Override

    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        //上下拖拽，若有其他需求同理

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        //向右侧滑，若有其他需求同理

        int swipeFlags = ItemTouchHelper.RIGHT;

        return makeMovementFlags(dragFlags, swipeFlags);

    }

    @Override

    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        //通知Adapter更新数据和视图

        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());

        //若返回false则表示不支持上下拖拽

        return true;

    }

    @Override

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        //通知Adapter更新数据和视图

        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());

    }



    @Override

    public boolean isItemViewSwipeEnabled() {

        //是否可以左右侧滑，默认返回true

        return true;

    }

    @Override

    public boolean isLongPressDragEnabled() {

        //是否可以长按上下拖拽，默认返回false

        return true;

    }


}