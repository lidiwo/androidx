package androidx.studio.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;

import androidx.studio.ContactWayActivity;
import androidx.studio.R;

import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AuthUtil {
    private static final String TIME_URL = "https://worldtimeapi.org/api/timezone/Asia/Shanghai";
    private static final String FIRST_TIME = "first_time";

    public static void openAuth(Activity activity) {
        if (PreferencesUtil.contains(activity, FIRST_TIME)) {
            if (isExpires(activity)) {
                showExpiresDialog(activity);
            }
            return;
        }
        Observable<Long> networkRequest = Observable.create(emitter -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(TIME_URL)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String content = body.string();
                        JSONObject rootJson = new JSONObject(content);
                        long timeMillis = rootJson.optLong("unixtime");
                        emitter.onNext(timeMillis * 1000);
                    }
                    emitter.onComplete();
                } else {
                    emitter.onError(new IOException("Unexpected code " + response));
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        networkRequest.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Long>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        if (!activity.isFinishing()) {
                            PreferencesUtil.putLong(activity, FIRST_TIME, aLong);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private static boolean isExpires(Context context) {
        long currentTime = System.currentTimeMillis();
        long firstTime = PreferencesUtil.getLong(context, FIRST_TIME, 0);

        if (currentTime < firstTime) {
            return true;
        }
        long maxTime = DateUtil.getMaxTime(firstTime);
        return currentTime > maxTime;
    }

    private static void showExpiresDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.expires_title);
        builder.setMessage(R.string.trial_period_expired);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.contact_way, null);
        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(d -> activity.finish());
        dialog.show();
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.RED);
        positiveButton.setOnClickListener(view -> {
            activity.startActivity(new Intent(activity, ContactWayActivity.class));
        });
    }
}
