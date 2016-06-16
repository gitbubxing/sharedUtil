package com.zqxd.shareddemoqqweixinsina.util.qqshared;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * Created by wangtao on 2016/6/13.
 */
public class QQsharedUtil {
    private static  final String TAG="QQsharedUtil";
    private static  final String APP_ID="1104641914";
   public static   Tencent  mTencent;
    public  static  QQsharedUtil util;
public MListener mMListener;
    public   BaseApiListener mBaseApiListener;
    private  QQsharedUtil(){}


    public static QQsharedUtil getInstances(){
        if (util==null){
            util= new QQsharedUtil();
        }
        return util;
    }
   public void initQQShared(Activity context){
       mTencent = Tencent.createInstance(APP_ID, context);
       mBaseApiListener= new BaseApiListener();
       mMListener= new MListener();
       if (!mTencent.isSessionValid())
       {
           mTencent.login(context, "", mMListener);
       }
   }
 class MListener  implements IUiListener{

     @Override
     public void onComplete(Object o) {
          Log.e(TAG,"onComplete分享成功");
     }

     @Override
 public void onError(UiError uiError) {

     }

     @Override
     public void onCancel() {

     }
 }

    private   class BaseApiListener implements IRequestListener {


        @Override
        public void onComplete(JSONObject jsonObject) {

        }

        @Override
        public void onIOException(IOException e) {

        }

        @Override
        public void onMalformedURLException(MalformedURLException e) {

        }

        @Override
        public void onJSONException(JSONException e) {

        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException e) {

        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException e) {

        }

        @Override
        public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e) {

        }

        @Override
        public void onHttpStatusException(HttpUtils.HttpStatusException e) {

        }

        @Override
        public void onUnknowException(Exception e) {

        }
    }


    /**
     * 分享到QQ 好友
     * @param context
     * @param title 标题
     * @param sharedContent  分享内容
     * @param sharedURL  分享的网页地址
     * @param sharedImgUrl 分享的图片地址
     */
    public void startShared(Activity context,String title ,String sharedContent,String  sharedURL,String sharedImgUrl)
    {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE,title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  sharedContent);
        if(sharedURL!= null){
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  sharedURL);

        }
        if(sharedImgUrl!= null){
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,sharedImgUrl);
        }
        Log.e(TAG," context.getApplicationInfo().name==="+ getAppName(context));

        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getAppName(context));
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);
        mTencent.shareToQQ(context, params, mMListener);
    }

    /**
     * 获取App的名称
     * @param context 上下文
     * @return 名称
     */
    public static String getAppName(Context context){
        PackageManager pm = context.getPackageManager();
//获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),0);
//获取应用 信息
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//获取albelRes
            int labelRes = applicationInfo.labelRes;
//返回App的名称
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

 /**
     * 分享到QQ空间
     * @param context
     * @param title 标题
     * @param sharedContent  分享内容
     * @param sharedURL  分享的网页地址
     * @param sharedImgUrl 分享的图片地址
     */
    public void startSharedToQZone(Activity context,String title ,String sharedContent,String  sharedURL,String sharedImgUrl)
    {
        final Bundle params = new Bundle();
//       params.putString(QzoneShare.SHARE_TO_QQ_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT );
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE,title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,  sharedContent);
        if(sharedURL!= null){
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,  sharedURL);

        }
        if(sharedImgUrl!= null){
            params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL,sharedImgUrl);
        }
        params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME,  getAppName(context));
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, 0);
        mTencent.shareToQQ(context, params, mMListener);
    }
}
