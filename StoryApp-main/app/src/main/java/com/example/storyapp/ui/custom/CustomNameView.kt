package com.example.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class CustomNameView: AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var isValidName = true

    init {
        inputType = InputType.TYPE_CLASS_TEXT
        setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_account_circle_24), null,
            null, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence, start: Int, count: Int, after:
            Int) {

            }

            override fun onTextChanged(string: CharSequence, start: Int, before: Int, count: Int) {
                isValidName = string.isNotEmpty()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        hint = context.getString(R.string.name)

        if (!isValidName) {
            error = context.getString(R.string.name_cannot_empty)
        }
    }
}