package jp.ipoca.notification

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

class PopUpDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(arguments.getString(ARGUMENT_TEXT))
                .setMessage("アプリを起動する")
                .setPositiveButton("OK") { _, _ ->
                    val intent = Intent(activity, MainActivity::class.java).apply {
                        // 別タスクで起動しなければ、excludeFromRecentsを設定したactivityをルートにしてしまう
                        // taskAffinityを設定することで、正しく別タスクに配置される
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    dialog.cancel()
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    dialog.cancel()
                }
                .show()
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        activity.finish()
    }

    companion object {
        val ARGUMENT_TEXT = "text"
        val TAG = PopUpDialogFragment::class.java.simpleName
    }
}