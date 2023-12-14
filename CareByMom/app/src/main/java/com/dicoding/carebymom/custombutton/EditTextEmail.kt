package com.dicoding.carebymom.custombutton

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.carebymom.R

class EditTextEmail : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (email.isValidEmail()) {
                    //do nothing
                } else {
                    setError(context.getString(R.string.invalid_email_address), null)
                }
            }
            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    fun String.isValidEmail(): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})([.]{1})(.{1,})"
        return this.isNotEmpty() && this.matches(emailRegex.toRegex())
    }
}