package com.example.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class CustomEmailView : AppCompatEditText {

    private var isValidEmail = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_24), null,
            null, null
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                string: CharSequence, start: Int, count: Int, after:
                Int
            ) {

            }

            override fun onTextChanged(string: CharSequence, start: Int, before: Int, count: Int) {
                isValidEmail =
                    string.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(string).matches()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        hint = context.getString(R.string.email)

        if (!isValidEmail) {
            error = context.getString(R.string.email_not_valid)
        }
    }
}
