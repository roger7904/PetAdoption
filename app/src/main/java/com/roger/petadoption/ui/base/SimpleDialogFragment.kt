package com.roger.petadoption.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.roger.petadoption.R
import com.roger.petadoption.databinding.FragmentSimpleDialogBinding

class SimpleDialogFragment(private val clickEvent: ClickEvent?) : DialogFragment() {
    protected var title: String? = null
    protected var content: String? = null
    protected var btnConfirm: String? = null
    protected var btnCancel: String? = null
    protected lateinit var args: Bundle

    private var tvTitle: TextView? = null
    private var tvContent: TextView? = null
    private var tvCancel: TextView? = null
    private var tvConfirm: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = (arguments ?: Bundle()).apply {
            title = getString(ARG_TITLE, "")
            content = getString(ARG_CONTENT, "")
            btnConfirm = getString(ARG_BTN_CONFIRM)
            btnCancel = getString(ARG_BTN_CANCEL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSimpleDialogBinding.inflate(inflater, container, false).apply {
            this@SimpleDialogFragment.tvTitle = tvTitle
            this@SimpleDialogFragment.tvContent = tvContent
            this@SimpleDialogFragment.tvCancel = tvCancel
            this@SimpleDialogFragment.tvConfirm = tvConfirm
            onViewCreated(root, savedInstanceState)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle?.text = title ?: ""

        tvContent?.run {
            if (content.isNullOrEmpty()){
                visibility = View.GONE
            }
            text = content ?: ""
        }

        tvCancel?.run {
            text = btnCancel ?: getString(R.string.cancel)
            setOnClickListener {
                clickEvent?.onCancelClick()
                dismiss()
            }
        }

        tvConfirm?.run {
            text = btnConfirm ?: getString(R.string.confirm)
            setOnClickListener {
                clickEvent?.onConfirmClick()
                dismiss()
            }
        }
    }


    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = onCreateView(LayoutInflater.from(context), null, savedInstanceState).apply {
            onViewCreated(this, savedInstanceState)
        }
        val alertDialog = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_App_MaterialAlertDialog
        )
            .setView(root)
            .create()
        alertDialog.setCanceledOnTouchOutside(true)
        return alertDialog
    }

    interface ClickEvent {
        fun onConfirmClick()
        fun onCancelClick()
    }

    companion object {
        const val ARG_TITLE = "TITLE"
        const val ARG_CONTENT = "CONTENT"
        const val ARG_BTN_CONFIRM = "ConfirmButton"
        const val ARG_BTN_CANCEL = "CancelButton"

        fun newInstance(
            title: String,
            content: String? = null,
            btnConfirm: String,
            btnCancel: String,
            clickEvent: ClickEvent? = null,
        ) = SimpleDialogFragment(clickEvent).apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_CONTENT, content)
                putString(ARG_BTN_CONFIRM, btnConfirm)
                putString(ARG_BTN_CANCEL, btnCancel)
            }
        }
    }
}