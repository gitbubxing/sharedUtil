package com.zqxd.shareddemoqqweixinsina.util.weixinshared;

import android.content.Context;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by wangtao on 2016/6/13.
 */
public class WeixinUtil {

    private final  static  String APP_ID="wx901f7d7203e2df09";
    private static IWXAPI mIWXAPI;
 public  static WeixinUtil mWeixinUtil;
    private WeixinUtil(){};
    public  static WeixinUtil getInsance(){

        if (mWeixinUtil== null){
            mWeixinUtil= new WeixinUtil();
        }

        return mWeixinUtil;
    }

   public void  initWeinxinShared(Context context){
        mIWXAPI= WXAPIFactory.createWXAPI(context,APP_ID,true);
       mIWXAPI.registerApp(APP_ID);
    }

    public void startSharedToWeixin(String sharedContent,String sharedURL){

        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = sharedContent;

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage mediaMessage= new WXMediaMessage();
        mediaMessage.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        // msg.title = "Will be ignored";
         mediaMessage.description = sharedContent;
        WXWebpageObject mWXWebpageObject= new WXWebpageObject();
        mWXWebpageObject.webpageUrl=sharedURL;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction =String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标识一个请求
        mediaMessage.mediaObject = mWXWebpageObject;
        req.message = mediaMessage;
        req.scene =  SendMessageToWX.Req.WXSceneSession ;

        // 调用api接口发送数据到微信
        mIWXAPI.sendReq(req);

    }
    public  void startSharedToWeixinCircle(String sharedContent,String sharedURL){

        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = sharedContent;
        WXWebpageObject mWXWebpageObject= new WXWebpageObject();
        mWXWebpageObject.webpageUrl=sharedURL;
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage mediaMessage= new WXMediaMessage();
//        mediaMessage.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        mediaMessage.title = sharedContent;
        mediaMessage.description = sharedContent;

        mediaMessage.mediaObject = mWXWebpageObject;
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction =String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标识一个请求

        req.message = mediaMessage;
        req.scene =  SendMessageToWX.Req.WXSceneTimeline ;

        // 调用api接口发送数据到微信
        mIWXAPI.sendReq(req);

    }
}
