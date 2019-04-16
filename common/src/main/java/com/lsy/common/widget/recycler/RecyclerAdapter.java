package com.lsy.common.widget.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsy.common.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
implements View.OnClickListener, View.OnLongClickListener {
    private final List<Data> mDataList = new ArrayList();

    /**
     * 创建一个ViewHolder
     * @param parent RecyclerView
     * @param i 界面到类型，这里约定为xml布局到id
     * @return
     */
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(i, parent, false);
        ViewHolder<Data> holder = onCreateViewholder(root, i);

        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        //设置View到tag为ViewHolder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);

        //进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        return null;
    }

    protected abstract ViewHolder<Data> onCreateViewholder(View root, int viewType);

    /**
     * 绑定一个数据到Holder上
     * @param dataViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> dataViewHolder, int i) {
        Data data = mDataList.get(i);
        dataViewHolder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        private AdapterCallback<Data> callback;
        protected Data mData;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         * @param data
         */
        void bind(Data data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 绑定数据时的回调，必须实现的方法，所以定义为抽象
         */
        protected abstract void onBind(Data data);

        public void updateData(Data data) {
            if (this.callback != null) {
                this.callback.update(data, this);
            }
        }
    }
}
