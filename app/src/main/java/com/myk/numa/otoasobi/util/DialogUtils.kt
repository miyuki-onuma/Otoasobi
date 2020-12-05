package com.myk.numa.otoasobi.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.myk.numa.otoasobi.R

class DialogUtils {

    companion object {
        private var dialog: AlertDialog? = null

        fun dismiss() {
            dialog?.dismiss()
        }

        fun isShowing(): Boolean {
            return dialog?.isShowing ?: false
        }

        /* style dialogShowMessage
        * |-----------------------------|
        * |            title            |
        * |           message           |
        * |-----------------------------|
        * | textNegative | textPositive |
        * |-----------------------------|
        * */
        fun dialogShowMessage(
            context: Context,
            title: String? = null,
            message: String,
            textPositive: String,
            textNegative: String? = null,
            onClickPositive: (() -> Unit)? = null,
            onClickNegative: (() -> Unit)? = null,
            isCancelOnTouchOutside: Boolean = true,
            isCancelable: Boolean? = true,
            onCancelListener: () -> Unit = {}
        ) {
            val alertBuilder = AlertDialog.Builder(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_default, null)
            alertBuilder.setView(dialogView)
            val alertDialog = alertBuilder.create().apply {
                setCanceledOnTouchOutside(isCancelOnTouchOutside)
                setCancelable(isCancelable == true)
                setOnCancelListener { onCancelListener.invoke() }
            }

            dialogView.findViewById<TextView>(R.id.title_dialog)?.apply {
                if (title.isNullOrEmpty()) {
                    this.visibility = View.GONE
                } else {
                    this.text = title
                }
            }

            dialogView.findViewById<TextView>(R.id.message_dialog)?.apply {
                this.text = message
            }

            dialogView.findViewById<TextView>(R.id.btn_ok_dialog)?.apply {
                this.text = textPositive
                setOnClickListener {
                    onClickPositive?.invoke()
                    alertDialog.dismiss()
                }
            }

            dialogView.findViewById<TextView>(R.id.btn_cancel_dialog)?.apply {
                if (textNegative.isNullOrEmpty()) {
                    this.visibility = View.GONE
                } else {
                    this.text = textNegative
                }
                setOnClickListener {
                    onClickNegative?.invoke()
                    alertDialog.dismiss()
                }
            }
            alertDialog.show()
        }

        /* style dialogShowListMessage
         * |-----------------------------|
         * |           title             |
         * |           items[0]          |
         * |           items[1]          |
         * |           items[2]          |
         * |           .....             |
         * |-----------------------------|
         * | textNegative | textPositive |
         * |-----------------------------|
         * */
        fun dialogShowListMessage(
            context: Context,
            title: String? = null,
            items: Array<String>,
            textPositive: String,
            textNegative: String? = null,
            onClickPositive: (() -> Unit)? = null,
            onClickNegative: (() -> Unit)? = null,
            onClickItem: ((Int) -> Unit)? = null,
            isCancelOnTouchOutside: Boolean = true,
            isGravityCenter: Boolean? = true,
            isShowButton: Boolean? = true,
            titleTextSizeDimen: Int = 0,
            titleColor: Int = 0,
            itemChecked: Int = -1
        ) {
            val builder = AlertDialog.Builder(context)
            with(builder)
            {
                if (!title.isNullOrEmpty()) {
                    setTitle(title)
                }
                if (isGravityCenter == true) {
                    val adapter = object : ArrayAdapter<String>(
                        this.context,
                        android.R.layout.simple_list_item_1, items
                    ) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view = super.getView(position, convertView, parent) as TextView
                            view.gravity = Gravity.CENTER
                            view.textAlignment = View.TEXT_ALIGNMENT_CENTER
                            if (itemChecked != -1) {
                                view.setBackgroundColor(
                                    ContextCompat.getColor(
                                        context,
                                        if (itemChecked == position) R.color.colorDarkGray else android.R.color.white
                                    )
                                )
                            }

                            return view
                        }
                    }
                    setSingleChoiceItems(adapter, -1) { _, which ->
                        dismiss()
                        onClickItem?.invoke(which)
                    }
                } else
                    setItems(items) { _, which ->
                        dismiss()
                        onClickItem?.invoke(which)
                    }

                setPositiveButton(textPositive) { _: DialogInterface, _: Int ->
                    onClickPositive?.invoke()
                }

                if (!textNegative.isNullOrBlank()) {
                    setNegativeButton(textNegative) { _: DialogInterface, _: Int ->
                        onClickNegative?.invoke()
                    }
                }
            }

            val alertDialog = builder.create()
            alertDialog.setCanceledOnTouchOutside(isCancelOnTouchOutside)
            alertDialog.setCancelable(true)
            alertDialog.show()
            dialog = alertDialog

            val titleView =
                alertDialog.findViewById(
                    context.resources.getIdentifier("alertTitle", "id", "android")
                ) as TextView

            if (isGravityCenter == true) {
                val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                layoutParams.gravity = Gravity.CENTER
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
                if (isShowButton == false) {
                    layoutParams.height = 1
                }
                btnPositive.layoutParams = layoutParams
                val titleView = alertDialog.findViewById(
                    context.resources.getIdentifier("alertTitle", "id", "android")
                ) as TextView
                titleView.gravity = Gravity.CENTER
                titleView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }

            if (titleTextSizeDimen != 0) {
                titleView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.resources.getDimension(titleTextSizeDimen)
                )
            }

            if (titleColor != 0) {
                titleView.setTextColor(ContextCompat.getColor(context, titleColor))
            }
        }

    }
}