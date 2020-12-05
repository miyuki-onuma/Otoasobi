package com.myk.numa.otoasobi.util

import android.content.Context
import com.myk.numa.otoasobi.R

fun Context.showDialogConfirm(msg: String, textPositive : String ? = null, onCallBackOk: (() -> Unit)? = null) {
    DialogUtils.dialogShowMessage(
        this,
        message = msg,
        textPositive = textPositive ?: getString(R.string.ok),
        isCancelOnTouchOutside = false,
        isCancelable = false,
        onClickPositive = onCallBackOk
    )
}