package android.appcompat.test

import android.appcompat.test.databinding.ActivityMainBinding
import androidx.studio.base.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

}