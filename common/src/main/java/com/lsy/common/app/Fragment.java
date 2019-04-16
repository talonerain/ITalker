package com.lsy.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author liusiyu
 */
public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnBinder;

    /**
     *
     * @param context 这个context就是Activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layId = getContentLayoutId();
            //初始化当前的根布局，但是不在创建时就添加到container里面，所以设置了false
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                //上一个Fragment被回收后有可能mRoot没有被回收，需要从它的父布局中移除掉
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当view创建完成后初始化数据
        initData();
    }

    /**
     * 初始化相关参数
     * @param bundle 参数Bundle
     */
    protected void initArgs(Bundle bundle) {
    }

    protected abstract int getContentLayoutId();

    protected void initWidget(View root) {
        mRootUnBinder = ButterKnife.bind(this, root);
    }

    protected void initData() {

    }


    /**
     * 返回键触发时调用
     * @return 返回True代表fragment已处理返回逻辑，Activity不用finish；
     * 返回false代表没有处理，Activity自己处理。
     */
    public boolean onBackPressed() {
        return false;
    }
}
