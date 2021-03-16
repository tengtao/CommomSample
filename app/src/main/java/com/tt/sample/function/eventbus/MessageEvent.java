package com.tt.sample.function.eventbus;


/**
 * 最好一类事件用一个实体类
 */
public class MessageEvent {

    String code;

    MessageEvent(String code) {
        this.code = code;
    }

//在生命周期注册
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }
//发送消息
//     EventBus.getDefault().post(new MessageEvent());

    //接收消息
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {/* Do something */};
}
