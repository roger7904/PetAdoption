package com.roger.petadoption.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.roger.petadoption.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected lateinit var params: Bundle
    protected var binding: VB? = null
        private set
    private var onBackPressedCallback: OnBackPressedCallback? = null
    private var toast: Toast? = null
    private var progressBar: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bindFragmentListener(context)
        activity?.onBackPressedDispatcher?.run {
            addCallback(this@BaseFragment, DefaultOnBackPressedCallback())
            onBackPressedCallback = addOnBackPressedCallback()?.also {
                addCallback(this@BaseFragment, it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        params = arguments ?: Bundle.EMPTY
        if (savedInstanceState == null) {
            initParam(params)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        binding = initViewBinding(inflater, container, savedInstanceState)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressBar)
        getViewModel()?.subscribeViewEvent(this::handleViewEvent)?.addTo(compositeDisposable)
        initView(savedInstanceState)
    }

    override fun onDestroyView() {
        getViewModel()?.resetViewEventPublisher()
        binding = null
        compositeDisposable.clear()
        super.onDestroyView()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        activity?.invalidateOptionsMenu()
    }

    protected fun setHomeAsUpVisible(show: Boolean) {
        val activity: AppCompatActivity? = activity as? AppCompatActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    protected fun setHomeAsUpIndicator(@DrawableRes drawableId: Int) {
        val activity: AppCompatActivity? = activity as? AppCompatActivity
        activity?.supportActionBar?.setHomeAsUpIndicator(drawableId)
    }

    protected open fun handleViewEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.Loading -> {
                progressBar?.run {
                    setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.transparent)
                    )
                    isVisible = true
                }
            }

            is ViewEvent.Done -> {
                progressBar?.isVisible = false
            }

            is ViewEvent.Error -> {
                Toast.makeText(requireContext(), event.message ?: "", Toast.LENGTH_SHORT).show()
            }

            is ViewEvent.UnknownError -> {
                val errMsg = event.error?.message ?: getString(R.string.unknown_error)
                Toast.makeText(requireContext(), errMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    protected open fun showToast(
        @StringRes resId: Int? = null,
        text: String? = null,
        duration: Int = Toast.LENGTH_SHORT,
    ) {
        activity ?: return
        val toastText = when {
            resId != null -> getString(resId)
            text != null -> text
            else -> ""
        }
        toast = toast?.apply {
            setText(toastText)
            setDuration(duration)
            show()
        } ?: Toast.makeText(activity, toastText, duration).apply { show() }
    }

    fun findFragmentByTag(tag: String?): Fragment? {
        return childFragmentManager.findFragmentByTag(tag)
    }

    protected fun getVisibleChildFragment(): Fragment? {
        return childFragmentManager.fragments.firstOrNull { it != null && it.isVisible }
    }

    protected fun addFragment(
        containerId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name,
        anim: FragmentAnim? = null,
    ): FragmentTransaction {
        return childFragmentManager.beginTransaction().addFragment(containerId, fragment, tag, anim)
    }

    protected fun FragmentTransaction.addFragment(
        containerId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name,
        anim: FragmentAnim? = null,
    ): FragmentTransaction {
        return this.apply {
            setFragmentAnimation(this, anim)
            this.add(containerId, fragment, tag)
        }
    }

    protected fun replaceFragment(
        containerId: Int,
        fragment: Fragment,
        backStackName: String? = fragment::class.java.name,
        addToBackStack: Boolean = true,
        anim: FragmentAnim = FragmentAnim.SLIDE_HORIZONTAL,
    ): FragmentTransaction {
        return childFragmentManager.beginTransaction()
            .replaceFragment(containerId, fragment, backStackName, addToBackStack, anim)
    }

    protected fun FragmentTransaction.replaceFragment(
        containerId: Int,
        fragment: Fragment,
        backStackName: String? = fragment::class.java.name,
        addToBackStack: Boolean = true,
        anim: FragmentAnim = FragmentAnim.SLIDE_HORIZONTAL,
    ): FragmentTransaction {
        return this.apply {
            setFragmentAnimation(this, anim)
            this.replace(containerId, fragment, backStackName)
            if (addToBackStack) {
                addToBackStack(backStackName)
            }
        }
    }

    protected fun removeFragment(tag: String): FragmentTransaction {
        return childFragmentManager.beginTransaction().removeFragment(tag)
    }

    protected fun FragmentTransaction.removeFragment(
        tag: String,
    ): FragmentTransaction {
        val fragment = childFragmentManager.findFragmentByTag(tag)
        return this.apply {
            if (fragment != null) {
                this.remove(fragment)
            }
        }
    }

    protected fun FragmentTransaction.safeCommit() {
        if (childFragmentManager.isStateSaved) {
            this.commitAllowingStateLoss()
        } else {
            this.commit()
        }
    }

    protected fun popFragment(name: String? = null, include: Boolean = false): Boolean {
        childFragmentManager.run {
            if (backStackEntryCount == 0) {
                return false
            }
            if (name != null) {
                popBackStack(name, if (include) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
            } else {
                popBackStack()
            }
            return true
        }
    }

    protected fun showDialog(fragment: DialogFragment) {
        val fm = childFragmentManager
        if (fm.isStateSaved || fm.isDestroyed) {
            return
        }
        val tagName = fragment.javaClass.name
        if (findFragmentByTag(tagName) == null
            && (activity as? BaseActivity<*>)?.findFragmentByTag(tagName) == null
        ) {
            fragment.showNow(fm, fragment.javaClass.name)
        }
    }

    private fun dismissDialog(fragment: DialogFragment) {
        val tagName = fragment.javaClass.name
        if (findFragmentByTag(tagName) != null)
            fragment.dismiss()
    }

    private fun setFragmentAnimation(transaction: FragmentTransaction, anim: FragmentAnim?) {
        if (anim == null) {
            transaction.setCustomAnimations(0, R.anim.slide_out_to_left)
            return
        }
        when (anim) {
            FragmentAnim.SLIDE_VERTICAL -> {
                transaction.setCustomAnimations(
                    R.anim.slide_in_from_bottom,
                    R.animator.fragment_fade_exit,
                    R.animator.fragment_fade_enter,
                    R.anim.slide_out_to_bottom
                )
            }
            FragmentAnim.SLIDE_HORIZONTAL -> {
                transaction.setCustomAnimations(
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left,
                    R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right
                )
            }
            FragmentAnim.FADE -> {
                transaction.setCustomAnimations(
                    R.animator.fragment_fade_enter,
                    R.animator.fragment_fade_exit,
                    R.animator.fragment_fade_enter,
                    R.animator.fragment_fade_exit
                )
            }
        }
    }

    protected abstract fun initParam(data: Bundle)

    protected abstract fun getViewModel(): BaseViewModel?

    protected abstract fun bindFragmentListener(context: Context)

    protected abstract fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): VB

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected open fun addOnBackPressedCallback(): OnBackPressedCallback? {
        return null
    }

    private inner class DefaultOnBackPressedCallback : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (childFragmentManager.backStackEntryCount != 0) {
                popFragment()
            } else {
                isEnabled = false
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    protected enum class FragmentAnim { SLIDE_HORIZONTAL, SLIDE_VERTICAL, FADE }
}