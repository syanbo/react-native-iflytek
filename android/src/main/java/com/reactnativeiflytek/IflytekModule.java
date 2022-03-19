package com.reactnativeiflytek;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

@ReactModule(name = IflytekModule.NAME)
public class IflytekModule extends ReactContextBaseJavaModule {
    public static final String NAME = "Iflytek";
    private Context context;
    // 语音合成对象
    private SpeechSynthesizer mTts;

    public IflytekModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void init(String appID) {
//      String param = "appid=" + appID + "," + SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC;
      SpeechUtility.createUtility(this.context, SpeechConstant.APPID + "=" + appID);
    }

    @ReactMethod
    public void createSynthesizer(Promise promise) {
      mTts = SpeechSynthesizer.createSynthesizer(this.context, new InitListener() {
          @Override
          public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
              promise.reject(code + "", "初始化失败");
            } else {
              promise.resolve(code + "");
            }
          }
        });
    }

    @ReactMethod
    public void startSpeaking(String text, Callback synthesizerCallback) {
      mTts.startSpeaking(text, new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
          // 开始播放
          synthesizerCallback.invoke("begin");
        }

        @Override
        public void onSpeakPaused() {
          // 暂停播放
          synthesizerCallback.invoke("paused");
        }

        @Override
        public void onSpeakResumed() {
          // 继续播放
          synthesizerCallback.invoke("resumed");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
          // 合成进度
          synthesizerCallback.invoke("bufferProgress", percent, beginPos, endPos);
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
          // 播放进度
          synthesizerCallback.invoke("speakProgress", percent, beginPos, endPos);
        }

        @Override
        public void onCompleted(SpeechError error) {
          if (error == null) {
            synthesizerCallback.invoke("completed");
          } else {
            synthesizerCallback.invoke("error");
          }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
          // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
          // 若使用本地能力，会话id为null
          if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            String sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL);
            synthesizerCallback.invoke("event", sid);
          }
        }
      });
    }

    @ReactMethod
    public void stopSpeaking() {
      mTts.stopSpeaking();
    }

    @ReactMethod
    public void pauseSpeaking() {
      mTts.pauseSpeaking();
    }

    @ReactMethod
    public void resumeSpeaking() {
      mTts.resumeSpeaking();
    }

    @ReactMethod
    public void isSpeaking(Promise promise) {
      promise.resolve(mTts.isSpeaking());
    }

    @ReactMethod
    public void setParameter(String key, String value) {
      mTts.setParameter(key, value);
    }

    @ReactMethod
    public void getParameter(String key, Promise promise) {
      promise.resolve(mTts.getParameter(key));
    }

    @ReactMethod
    public void destroy() {
      mTts.destroy();
    }
}
