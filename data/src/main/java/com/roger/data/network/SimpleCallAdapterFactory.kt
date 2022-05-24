package com.roger.data.network

import com.roger.data.dto.ApiResult
import com.roger.domain.common.DomainError
import com.roger.domain.common.ErrorHandler
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class SimpleCallAdapterFactory @Inject constructor(
    private val rxJava3CallAdapterFactory: RxJava3CallAdapterFactory,
    private val errorHandler: ErrorHandler
) : CallAdapter.Factory() {

    @Suppress("UNCHECKED_CAST")
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        return RxJava3ReAuthCallAdapter(
            rxJava3CallAdapterFactory.get(
                returnType,
                annotations,
                retrofit
            ) as CallAdapter<Any, Any>?,
            errorHandler
        )
    }

    private class RxJava3ReAuthCallAdapter<R>(
        private val wrappedAdapter: CallAdapter<R, Any>?,
        private val errorHandler: ErrorHandler
    ) : CallAdapter<R, Any> {

        override fun adapt(call: Call<R>): Any {
            val rxObject = wrappedAdapter?.adapt(call)

            return if (rxObject is Observable<*>) {
                val reAuth = { observable: Observable<Throwable> ->
                    val retryCount = AtomicInteger(0)

                    observable.flatMap { throwable ->
                        retryCount.incrementAndGet()

                        //Exceed max retry limit or server error
                        if (retryCount.get() >= MAX_RETRY || (throwable is HttpException && throwable.code() >= 500)) {
                            return@flatMap Observable.error(throwable)
                        }
                        val retryObservable = Observable.timer(500, TimeUnit.MILLISECONDS)
                        if (throwable is DomainError || throwable is HttpException) {
                            return@flatMap Observable.error(errorHandler.parseThrowable(throwable))
                        } else {
                            retryObservable
                        }
                    }
                }
                rxObject.subscribeOn(Schedulers.io())
                    .flatMap {
                        return@flatMap if (it is ApiResult<*> && it.status >= 400) {
                            Observable.error(
                                errorHandler.parseError(
                                    it.status,
                                    it.message,
                                    it.data
                                )
                            )
                        } else {
                            Observable.just(it)
                        }
                    }.retryWhen(reAuth)
            } else {
                val reAuth = { flowable: Flowable<Throwable> ->
                    val retryCount = AtomicInteger(0)

                    flowable.flatMap { throwable ->
                        retryCount.incrementAndGet()

                        // Exceed max retry limit
                        if (retryCount.get() >= MAX_RETRY) {
                            return@flatMap Flowable.error(throwable)
                        }

                        // Server error
                        if ((throwable is HttpException) && throwable.code() >= 500) {
                            return@flatMap Flowable.error(throwable)
                        }

                        val retryFlowable = Flowable.timer(500, TimeUnit.MILLISECONDS)
                        if (throwable is DomainError || throwable is HttpException) {
                            Flowable.error(errorHandler.parseThrowable(throwable))
                        } else {
                            retryFlowable
                        }
                    }
                }
                when (rxObject) {
                    is Single<*> -> rxObject.subscribeOn(Schedulers.io())
                        .flatMap {
                            if (it is ApiResult<*> && it.status >= 400) {
                                Single.error(
                                    errorHandler.parseError(
                                        it.status,
                                        it.message,
                                        it.data
                                    )
                                )
                            } else {
                                Single.just(it)
                            }
                        }.retryWhen(reAuth)

                    is Flowable<*> -> rxObject.subscribeOn(Schedulers.io())
                        .flatMap {
                            if (it is ApiResult<*> && it.status >= 400) {
                                if (it.status >= 400) {
                                    Flowable.error(
                                        errorHandler.parseError(
                                            it.status,
                                            it.message,
                                            it.data
                                        )
                                    )
                                } else {
                                    Flowable.just(it)
                                }
                            } else {
                                Flowable.just(it)
                            }
                        }.retryWhen(reAuth)

                    is Maybe<*> -> rxObject.subscribeOn(Schedulers.io())
                        .flatMap {
                            if (it is ApiResult<*> && it.status >= 400) {
                                Maybe.error(errorHandler.parseError(it.status, it.message, it.data))
                            } else {
                                Maybe.just(it)
                            }
                        }.retryWhen(reAuth)

                    is Completable -> rxObject.subscribeOn(Schedulers.io())

                    else -> throw IllegalArgumentException("$javaClass type is not supported")
                }
            }
        }

        override fun responseType(): Type? = wrappedAdapter?.responseType()
    }

    companion object {
        private const val MAX_RETRY = 4
    }
}