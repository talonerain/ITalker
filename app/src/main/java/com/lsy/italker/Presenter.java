package com.lsy.italker;

import android.text.TextUtils;

public class Presenter implements IPrensenter {
    private IView mView;

    public Presenter(IView view) {
        mView = view;
    }

    @Override
    public void search() {
        //开启界面Loading
        //...

        String inputString = mView.getInputString();
        if (TextUtils.isEmpty(inputString)) {
            //为空直接返回
            return;
        }

        IUserService service = new UserService();

        String result = "Result:" + service.search(hashCode());

        //关闭界面Loading
        mView.setResultString(result);
    }
}
