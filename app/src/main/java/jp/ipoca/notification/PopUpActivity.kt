package jp.ipoca.notification

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class PopUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ステータスバー非表示
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        PopUpDialogFragment().apply {
            arguments = Bundle().apply {
                putString(PopUpDialogFragment.ARGUMENT_TEXT, text)
            }
        }.show(supportFragmentManager, PopUpDialogFragment.TAG)
    }
}
