package com.app.signal.control_kit.field

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.addTextChangedListener
import com.app.signal.control_kit.R


class SearchField: LinearLayoutCompat {
    var text: Editable?
        get() = this.fieldInput.text
        set(value) {
            this.fieldInput.text = value
        }

    private lateinit var fieldInput: AppCompatEditText

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflateLayout()
        setup()
        applyAttributes(attrs)
    }

    private fun inflateLayout() {
        val content = View.inflate(context, R.layout.field_search, this)

        fieldInput = content.findViewById(R.id.field_search_input)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        context
            .theme.obtainStyledAttributes(attrs, R.styleable.SearchField, 0, 0)
            .also {
                fieldInput.hint = it.getString(R.styleable.SearchField_android_hint)
                it.recycle()
            }
    }

    private fun setup() {
        isSaveEnabled = true
        isClickable = false

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        fieldInput.setOnEditorActionListener { _, _, _ ->
            true
        }
    }

    fun addTextChangedListener(afterTextChanged: (text: Editable?) -> Unit = {}) {
        fieldInput.addTextChangedListener(
            afterTextChanged = afterTextChanged
        )
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isClickable
    }
}