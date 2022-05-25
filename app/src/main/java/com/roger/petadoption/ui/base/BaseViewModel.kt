package com.roger.petadoption.ui.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.roger.domain.common.DataResult
import com.roger.domain.common.DomainError
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import timber.log.Timber

open class BaseViewModel(private val state: SavedStateHandle) : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()
    protected val viewEventPublisher = BehaviorProcessor.create<ViewEvent>()

    override fun onCleared() {
        super.onCleared()
        viewEventPublisher.onComplete()
        compositeDisposable.clear()
    }

    fun resetViewEventPublisher() {
        viewEventPublisher.onNext(ViewEvent.None)
    }

    fun <T> Single<DataResult<T>>.sub(
        onError: ((Throwable?) -> Boolean)? = null,
        ignoreError: Boolean = false,
        dismissLoadingOnResult: Boolean = true,
        onSuccess: (T?) -> Unit = {}
    ): Disposable {
        return subscribeOn(Schedulers.io()).subscribe { result, throwable ->
            val throwable = throwable ?: (result as? DataResult.Failure<T>)?.error
            if (throwable != null) {
                if (ignoreError) {
                    return@subscribe
                }
                when (throwable) {
                    else -> {
                        val errorMessage = (throwable as? DomainError.CommonError)?.errorMessage
                        if (onError != null) {
                            val isErrorHandled = onError(throwable)
                            if (!isErrorHandled) {
                                viewEventPublisher.onNext(ViewEvent.Error(errorMessage))
                            }
                        } else {
                            viewEventPublisher.onNext(ViewEvent.Error(errorMessage))
                        }
                    }
                }
                return@subscribe
            }
            if (result is DataResult.Success<T>) {
                if (dismissLoadingOnResult) {
                    viewEventPublisher.onNext(ViewEvent.Done)
                }
                onSuccess(result.data)
            }
        }
    }

    fun subscribeViewEvent(eventHandler: (event: ViewEvent) -> Unit): Disposable {
        return viewEventPublisher
            .onBackpressureBuffer(128, true, true)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : BaseViewEventSubscriber() {
                override fun onEvent(event: ViewEvent) {
                    eventHandler(event)
                }
            }).addTo(compositeDisposable)
    }

    abstract class BaseSubscriber<T> : DisposableSubscriber<T>() {
        override fun onComplete() {

        }

        override fun onNext(t: T) {
            onData(t)
        }

        abstract fun onData(data: T)
    }

    abstract class BaseViewEventSubscriber : BaseSubscriber<ViewEvent>() {

        override fun onData(data: ViewEvent) {
            onEvent(data)
        }

        override fun onError(t: Throwable?) {
            Timber.e(t)
            onEvent(ViewEvent.UnknownError(t))
        }

        abstract fun onEvent(event: ViewEvent)
    }
}