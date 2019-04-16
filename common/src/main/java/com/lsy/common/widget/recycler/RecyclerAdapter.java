package com.lsy.common.widget.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsy.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {
    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    /**
     * 复写默认当布局类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewTtype(position, mDataList.get(position));
    }

    protected abstract int getItemViewTtype(int position, Data data);

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

        //设置View到tag为ViewHolder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        //进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        holder.callback = this;
        return null;
    }

    /**
     * 当得到一个新的ViewHolder
     * @param root
     * @param viewType
     * @return
     */
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

    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入数组
     * @param dataList
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入集合
     * @param dataList
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 清空数据
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (mListener != null) {
            int pos = viewHolder.getAdapterPosition();
            mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (mListener != null) {
            int pos = viewHolder.getAdapterPosition();
            mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    public void setListener(AdapterListener<Data> adapterListener) {
        this.mListener = adapterListener;
    }

    /**
     * 点击监听器
     * @param <Data>
     */
    public interface AdapterListener<Data> {
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
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
