package com.lsy.common.app;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.BindFont;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author liusiyu
 */
public abstract class Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面初始化之前调用的初始化窗口
        initWindows();

        if (initArgs(getIntent().getExtras())) {
            int layId = getContentLayoutId();
            setContentView(layId);
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 初始化窗口
     */
    protected void initWindows() {

    }

    /**
     * 初始化相关参数
     * @param bundle 参数Bundle
     * @return 如果参数正确返回了True，错误返回False
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 得到当前界面的资源文件Id
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();


    /**
     * 初始化控件
     */
    protected void initWidget() {
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击toolbar返回时，Finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //得到当前Activity下的所有Fragment判断是否为空
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof com.lsy.common.app.Fragment) {
                    //判断是否拦截了返回按钮
                    if (((com.lsy.common.app.Fragment) fragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
