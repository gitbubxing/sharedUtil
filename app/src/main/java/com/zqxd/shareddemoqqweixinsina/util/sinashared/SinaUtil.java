package com.zqxd.shareddemoqqweixinsina.util.sinashared;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.zqxd.shareddemoqqweixinsina.R;

/**
 * Created by wangtao on 2016/6/13.
 */
public class SinaUtil {

    public static final String TAG      = "MainActivity";
    public static final String APP_KEY      = "1677290317";		   // 应用的APP_KEY
    public static final String REDIRECT_URL = "http://www.sdzqxd.com";// 应用的回调页
    public static IWeiboShareAPI mWeiboShareAPI ;
    public static SsoHandler mSsoHandler;
    static   SinaUtil sinaUtil;
      private SinaUtil (){

}
public static SinaUtil getInstances(){
    if (sinaUtil==null){
        sinaUtil   =new SinaUtil();
    }
    return sinaUtil;
}

    /**
     * 初始化
     * @param context
     * @return
     */
    public  void  initSinaShared(Activity context){
        mWeiboShareAPI =  WeiboShareSDK.createWeiboAPI(context, APP_KEY);
        mWeiboShareAPI.registerApp();	// 将应用注册到微博客户端
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
        if (accessToken== null ){
            AuthInfo mAuthInfo = new AuthInfo(context, APP_KEY,REDIRECT_URL, null);
               mSsoHandler = new SsoHandler(context, mAuthInfo);
            mSsoHandler. authorize(new AuthListener(context));
        }

    }

    /**
     * 授权回调监听
     */
    class AuthListener implements WeiboAuthListener {
        private Activity context;
        public AuthListener(Activity context){
            this.context = context;
        }
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken     mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
            if (mAccessToken.isSessionValid()) {

                AccessTokenKeeper.writeAccessToken(context,mAccessToken);
            } else {
                // 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
                String code = values.getString("code", "");
                Log.e(TAG,"onComplete---code==>"+code);
            }
        }

        @Override
        public void onCancel() {
            Log.e(TAG,"onCancel");
            Toast.makeText(context,"取消新浪授权",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.e(TAG,"onWeiboException=="+e.toString());
        }
    }

    /**
     * 开始分享
     * @param sharedContent  分享内容
     * @param sharedUrl  分享的活动网页链接地址
     * @param context 上下文对象
     */
    public void sendMultiMessage(String sharedContent, String sharedUrl,final Activity context) {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (sharedContent!= null) {
            TextObject textObject = new TextObject();
            textObject.text =sharedContent;
            weiboMessage.textObject = textObject;
        }
        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (sharedUrl!= null) {
            WebpageObject mediaObject = new WebpageObject();
            mediaObject.identify = Utility.generateGUID();
            mediaObject.title =  sharedContent;
            mediaObject.description = sharedUrl;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
            mediaObject.setThumbImage(bitmap);
            mediaObject.actionUrl =  sharedUrl;
            mediaObject.defaultText = sharedContent;
            weiboMessage.mediaObject= mediaObject;
        }


        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;


        // 3. 发送请求消息到微博，唤起微博分享界面

        AuthInfo authInfo = new AuthInfo(context, APP_KEY, REDIRECT_URL, null);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        Log.e(TAG,"onComplete新浪分享开始");
       /* mWeiboShareAPI.sendRequest(context, request);*/
        mWeiboShareAPI.sendRequest(context, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException( WeiboException e ) {
                Log.e(TAG,"onWeiboException=="+e.toString());
            }

            @Override
            public void onComplete( Bundle bundle ) {
                Log.e(TAG,"onComplete新浪分享成功");
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                Toast.makeText(context,"新浪分享成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.e(TAG,"onCancel新浪分享取消");
                Toast.makeText(context,"新浪分享取消",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
