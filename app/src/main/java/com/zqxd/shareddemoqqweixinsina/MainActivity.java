package com.zqxd.shareddemoqqweixinsina;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.tencent.tauth.Tencent;
import com.zqxd.shareddemoqqweixinsina.util.qqshared.QQsharedUtil;
import com.zqxd.shareddemoqqweixinsina.util.sinashared.SinaUtil;
import com.zqxd.shareddemoqqweixinsina.util.weixinshared.WeixinUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IWeiboHandler.Response {
    private final  String TAG="MainActivity";
    private Button btn_qq;
    private Button btn_qzone,btn_weixin,btn_weixinCircle,btn_sina,btn_all;

    private SinaUtil sinaUtil;
    private WeixinUtil weixinUtil;
    private QQsharedUtil qqUtil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sinaUtil=  SinaUtil.getInstances();
        sinaUtil.initSinaShared(this);
        weixinUtil=  WeixinUtil.getInsance();
        weixinUtil.initWeinxinShared(this);
        Log.e(TAG," onCreate");
        qqUtil= QQsharedUtil.getInstances();
        qqUtil.initQQShared(this);
        if (savedInstanceState != null) {
            SinaUtil. mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
            btn_qq= (Button) findViewById(R.id.btn_qq);
        btn_qq.setOnClickListener(this);

        btn_qzone= (Button)findViewById(R.id.btn_qqzone);
        btn_qzone.setOnClickListener(this);

        btn_weixin= (Button)findViewById(R.id.btn_weixin);
        btn_weixin.setOnClickListener(this);

        btn_weixinCircle= (Button)findViewById(R.id.btn_circle);
        btn_weixinCircle.setOnClickListener(this);

        btn_sina= (Button)findViewById(R.id.btn_sina);
        btn_sina.setOnClickListener(this);

        btn_all= (Button)findViewById(R.id.btn_all);
        btn_all.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_all:
                break;
           case R.id.btn_qq:
               Log.e(TAG," QQ分享===");
               qqUtil.startShared(this,"分享标题","分享到好友","www.baidu.com","http://d.hiphotos.baidu.com/image/pic/item/a5c27d1ed21b0ef4400edb2fdec451da80cb3ed8.jpg");
                break;
           case R.id.btn_qqzone:
               Log.e(TAG," QQ空间分享===");
               qqUtil.  startSharedToQZone(this,"分享标题","分享到空间","www.baidu.com","http://d.hiphotos.baidu.com/image/pic/item/a5c27d1ed21b0ef4400edb2fdec451da80cb3ed8.jpg");

               break;
           case R.id.btn_weixin:
               weixinUtil.startSharedToWeixin("分享到微信好友","www.baidu.com");
                break;
           case R.id.btn_circle:
               weixinUtil.startSharedToWeixinCircle("分享到微信朋友圈","www.baidu.com");
                break;
           case R.id.btn_sina:
               startSinaShared();
                break;

        }

    }

    /**
     * 开始分享
     */
    void startSinaShared(){
        sinaUtil.sendMultiMessage("这里是分享内容","http://www.baidu.com",this);

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        SinaUtil.mWeiboShareAPI.handleWeiboResponse(intent, this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SinaUtil. mSsoHandler != null) {
            SinaUtil.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        Tencent.onActivityResultData(requestCode,resultCode,data,qqUtil.mMListener);

    }


    @Override
    public void onResponse(BaseResponse baseResponse) {

    }





}
