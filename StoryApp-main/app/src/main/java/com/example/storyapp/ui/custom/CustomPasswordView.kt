package com.example.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class CustomPasswordView : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object{
        private const val PASSWORD_LEN = 6
    }

    private var isValidPassword = true

    init {
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable
            .ic_baseline_lock_24), null,
            null, null)
        transformationMethod = PasswordTransformationMethod.getInstance()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence, start: Int, count: Int, after:
            Int) {

            }

            override fun onTextChanged(string: CharSequence, start: Int, before: Int, count: Int) {
                isValidPassword = string.length >= PASSWORD_LEN
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        hint = context.getString(R.string.password)

        if (!isValidPassword) {
            error = context.getString(R.string.password_not_valid, PASSWORD_LEN)
        }
    }
}