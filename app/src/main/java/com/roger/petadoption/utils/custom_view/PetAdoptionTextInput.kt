package com.roger.petadoption.utils.custom_view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ViewPetadoptionTextInputBinding

class PetAdoptionTextInput : ConstraintLayout {
    private var binding: ViewPetadoptionTextInputBinding? = null

    var endIconTint: Int? = null
        set(value) {
            field = value ?: return
            binding?.til?.setEndIconTintList(
                ContextCompat.getColorStateList(
                    context,
                    value
                )
            )
        }

    var endIconMode: Int? = TextInputLayout.END_ICON_NONE
        set(value) {
            field = value ?: return
            binding?.til?.endIconMode = value
        }

    var isEnabled: Boolean? = null
        set(value) {
            field = value ?: return
            changeEnable(value)
        }

    var title: String? = null
        set(value) {
            field = value ?: ""
            binding?.tvTilTitle?.text = value ?: ""
        }

    var hint: String? = null
        set(value) {
            field = value
            binding?.tieTil?.hint = value
        }

    var text: String? = null
        get() {
            return binding?.tieTil?.text?.toString()
        }
        set(value) {
            if (field == value) {
                return
            }
            field = value ?: ""
            binding?.tieTil?.run {
                setText(value ?: "")
                setSelection(value?.length ?: 0)
            }
        }

    var descText: String? = null
        set(value) {
            field = value
            binding?.tvDesc?.run {
                text = value ?: ""
                isVisible = !value.isNullOrEmpty()
            }
        }

    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.endIconMode = binding?.til?.endIconMode ?: 0
        ss.isEnabled = binding?.til?.isEnabled ?: true
        ss.title = binding?.tvTilTitle?.text?.toString() ?: ""
        ss.hint = binding?.tieTil?.hint?.toString() ?: ""
        ss.text = binding?.tieTil?.text?.toString() ?: ""
        ss.descText = descText ?: ""
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val ss = state as? SavedState
        super.onRestoreInstanceState(ss?.superState)
        if (endIconMode == null) {
            endIconMode = ss?.endIconMode
        }
        if (isEnabled == null) {
            isEnabled = ss?.isEnabled
        }
        if (title == null) {
            title = ss?.title
        }
        if (hint == null) {
            hint = ss?.hint
        }
        if (text == null) {
            text = ss?.text
        }
        if (descText == null) {
            descText = ss?.descText
        }
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable?>?) {
        super.dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable?>?) {
        super.dispatchThawSelfOnly(container)
    }

    fun getEditText(): TextInputEditText? = binding?.tieTil

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val root = LayoutInflater.from(context).inflate(R.layout.view_petadoption_text_input, this, true)
        binding = ViewPetadoptionTextInputBinding.bind(root)

        context.obtainStyledAttributes(
            attrs, R.styleable.PetAdoptionTextInput, 0, 0
        ).run {
            binding?.run {
                val isEnabled =
                    getBoolean(R.styleable.PetAdoptionTextInput_TextInput_isEnabled, true)
                changeEnable(isEnabled)

                val isTitleVisible =
                    getBoolean(R.styleable.PetAdoptionTextInput_TextInput_isTitleVisible, true)
                tvTilTitle.visibility =
                    if (isTitleVisible) View.VISIBLE else View.GONE

                val titleResId =
                    getResourceId(
                        R.styleable.PetAdoptionTextInput_TextInput_title,
                        0
                    ).takeIf { it != 0 }
                tvTilTitle.text = titleResId?.let { context.getString(titleResId) }

                val hintResId =
                    getResourceId(
                        R.styleable.PetAdoptionTextInput_TextInput_hint,
                        0
                    ).takeIf { it != 0 }
                tieTil.hint = hintResId?.let { context.getString(it) }


                val textResId =
                    getResourceId(
                        R.styleable.PetAdoptionTextInput_TextInput_text,
                        0
                    ).takeIf { it != 0 }
                tieTil.setText(textResId?.let { context.getString(it) })


                val inputType: Int =
                    getInt(R.styleable.PetAdoptionTextInput_android_inputType, InputType.TYPE_CLASS_TEXT)
                tieTil.inputType = inputType

                val endIconResId = getResourceId(R.styleable.PetAdoptionTextInput_endIconDrawable, 0)
                    .takeIf { it != 0 }
                til.endIconDrawable =
                    endIconResId?.let { context.getDrawable(it) }

                val endIconMode =
                    getInt(R.styleable.PetAdoptionTextInput_endIconMode, TextInputLayout.END_ICON_NONE)
                til.endIconMode = endIconMode

                val descTextResId = getResourceId(
                    R.styleable.PetAdoptionTextInput_TextInput_descText, 0
                ).takeIf { it != 0 }
                if (descTextResId != null) {
                    descText = context.getString(descTextResId)
                } else {
                    tvDesc.visibility = GONE
                }

                val countEnable = getBoolean(R.styleable.PetAdoptionTextInput_counterEnabled, false)
                til.isCounterEnabled = countEnable

                val counterMaxLength =
                    getInt(R.styleable.PetAdoptionTextInput_counterMaxLength, 0).takeIf { it != 0 }
                counterMaxLength?.let {
                    til.counterMaxLength = it
                }

                val maxLength =
                    getInt(R.styleable.PetAdoptionTextInput_android_maxLength, 0).takeIf { it != 0 }
                maxLength?.let {
                    tieTil.filters = arrayOf<InputFilter>(LengthFilter(maxLength))
                }

                recycle()
            }
        }
    }

    private fun changeEnable(isEnabled: Boolean) {
        binding?.til?.run {
            this.isEnabled = isEnabled
            setBoxBackgroundColorResource(if (isEnabled) R.color.white else R.color.white_gray)
            setBoxStrokeColorStateList(
                ContextCompat.getColorStateList(
                    context,
                    if (isEnabled) R.color.colorNeutral_N4_placeholder else R.color.colorNeutral_N5_div
                )!!
            )
        }
    }

    internal class SavedState : BaseSavedState {
        var endIconMode: Int = 0
        var isEnabled: Boolean = true
        var title: String = ""
        var hint: String = ""
        var text: String = ""
        var descText: String = ""

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            endIconMode = source.readInt()
            isEnabled = source.readInt() == 1
            title = source.readString() ?: ""
            hint = source.readString() ?: ""
            text = source.readString() ?: ""
            descText = source.readString() ?: ""
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.run {
                writeInt(endIconMode)
                writeInt(if (isEnabled) 1 else 0)
                writeString(title)
                writeString(hint)
                writeString(text)
                writeString(descText)
            }
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel) = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }
}
