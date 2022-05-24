package com.roger.petadoption.ui.base

import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.*
import androidx.viewbinding.ViewBinding
import com.roger.petadoption.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected lateinit var params: Bundle
    protected var binding: VB? = null
        private set
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = initViewBinding().apply {
            setContentView(root)
        }
        params = intent.extras ?: Bundle.EMPTY
        if (savedInstanceState == null) {
            initParam(params)
        }
        getViewModel()
            .subscribeViewEvent(this::handleViewEvent)
            .addTo(compositeDisposable)
        initView(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                var hideKeyboard = true
                for (viewType in binding?.root?.touchables!!) {
                    if (viewType is EditText) {
                        val outRect = Rect()
                        viewType.getGlobalVisibleRect(outRect)
                        if (outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                            hideKeyboard = false
                        }
                    }
                }
                if (hideKeyboard) {
                    val insetsController = ViewCompat.getWindowInsetsController(binding!!.root)
                    insetsController?.hide(WindowInsetsCompat.Type.ime())
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    protected open fun handleViewEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.Loading -> {
            }

            is ViewEvent.Done -> {
            }

            is ViewEvent.Error -> {
                Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
            }

            is ViewEvent.UnknownError -> {
                val errMsg = event.error?.message ?: getString(R.string.unknown_error)
                Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }


    protected open fun showToast(
        @StringRes resId: Int? = null,
        text: String? = null,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        val toastText = when {
            resId != null -> getString(resId)
            text != null -> text
            else -> ""
        }
        toast = toast?.apply {
            setText(toastText)
            setDuration(duration)
            show()
        } ?: Toast.makeText(this, toastText, duration).apply { show() }
    }

    fun findFragmentByTag(tag: String?): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }

    protected fun getVisibleFragment(): Fragment? {
        return supportFragmentManager.fragments.firstOrNull { it.isAdded && it.isVisible }
    }

    protected fun switchBottomNavigationFragment(
        containerId: Int, shownFragment: Fragment, tag: String? = shownFragment::class.java.name
    ) {
        supportFragmentManager.commit(supportFragmentManager.isStateSaved) {
            val visibleFragment = getVisibleFragment()

            if (visibleFragment == shownFragment)
                return@commit
            if (visibleFragment != null) {
                hide(visibleFragment)
            }
            setCustomAnimations(R.animator.fragment_open_enter, R.animator.fragment_close_exit)
            if (!shownFragment.isAdded) {
                add(containerId, shownFragment, tag)
            } else {
                show(shownFragment)
            }
        }
    }

    protected fun addFragment(
        containerId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name,
        anim: FragmentAnim? = null
    ): FragmentTransaction {
        return supportFragmentManager.beginTransaction()
            .addFragment(containerId, fragment, tag, anim)
    }

    protected fun FragmentTransaction.addFragment(
        containerId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name,
        anim: FragmentAnim? = null
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
        anim: FragmentAnim = FragmentAnim.SLIDE_HORIZONTAL
    ): FragmentTransaction {
        return supportFragmentManager.beginTransaction()
            .replaceFragment(containerId, fragment, backStackName, addToBackStack, anim)
    }

    protected fun FragmentTransaction.replaceFragment(
        containerId: Int,
        fragment: Fragment,
        backStackName: String? = fragment::class.java.name,
        addToBackStack: Boolean = true,
        anim: FragmentAnim = FragmentAnim.SLIDE_HORIZONTAL
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
        return supportFragmentManager.beginTransaction().removeFragment(tag)
    }

    protected fun FragmentTransaction.removeFragment(
        tag: String
    ): FragmentTransaction {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        return this.apply {
            if (fragment != null) {
                this.remove(fragment)
            }
        }
    }

    protected fun FragmentTransaction.safeCommit() {
        if (supportFragmentManager.isStateSaved) {
            this.commitAllowingStateLoss()
        } else {
            this.commit()
        }
    }

    protected fun popFragment(name: String? = null, include: Boolean = false): Boolean {
        supportFragmentManager.run {
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
        val fm = supportFragmentManager
        if (fm.isStateSaved || fm.isDestroyed) {
            return
        }
        val tagName = fragment.javaClass.name
        if (findFragmentByTag(tagName) == null
            && (getVisibleFragment() as? BaseFragment<*>)?.findFragmentByTag(tagName) == null
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

    protected abstract fun getViewModel(): BaseViewModel

    protected abstract fun initViewBinding(): VB

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected enum class FragmentAnim { SLIDE_HORIZONTAL, SLIDE_VERTICAL, FADE }
}
