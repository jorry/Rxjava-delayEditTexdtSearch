package com.iqianjin.client.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.iqianjin.client.view.Rxjava.TextViewTextOnSubscribe;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by jorryLiu on 2016/3/15.
 * liujiawei@puhuifinance.com
 */
public class DelayEditText extends EditText {

    public static final int DELAY_TIME = 600;

    public DelayEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initDelayEdit() {
        Observable.create(new TextViewTextOnSubscribe(this)).debounce(DELAY_TIME, TimeUnit.MILLISECONDS).switchMap(new Func1<CharSequence, Observable<String>>() {

            @Override
            public Observable<String> call(CharSequence charSequence) {
                return Observable.just(charSequence.toString());
            }
        }).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Object o) {
                if (daleyTimeOnNextListner != null) {
                    String temp = (String) o;
                    if (TextUtils.isEmpty(temp)) {
                        daleyTimeOnNextListner.onEmpty();
                    } else {
                        daleyTimeOnNextListner.onCompleted((String) o);
                    }
                }
            }
        });
    }


    public void setDaleyTimeOnNextListner(DaleyTimeOnNextListner daleyTimeOnNextListner) {
        this.daleyTimeOnNextListner = daleyTimeOnNextListner;
    }

    public DaleyTimeOnNextListner daleyTimeOnNextListner;

    public interface DaleyTimeOnNextListner {
        public void onCompleted(String str);

        public void onEmpty();
    }
}
