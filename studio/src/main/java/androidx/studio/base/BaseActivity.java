package androidx.studio.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.studio.utils.AuthUtil;
import androidx.viewbinding.ViewBinding;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.disposables.Disposable;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T mBinding;

    protected Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AuthUtil.openAuth(this)
        mBinding = getViewBinding();
        setContentView(mBinding.getRoot());
        if (useEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData(savedInstanceState);
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }

        if (useEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected void initData() {

    }

    protected void initData(Bundle savedInstanceState) {
        initData();
    }

    protected void initEvent() {

    }

    protected abstract T getViewBinding();

    protected boolean useEventBus() {
        return false;
    }
}
