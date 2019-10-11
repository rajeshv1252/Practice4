package com.evoke.practice4.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evoke.practice4.data.User
import com.evoke.practice4.data.repository.AppRepository
import com.evoke.practice4.utils.RxSchedule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val rxSchedule: RxSchedule
) : ViewModel() {


    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val homeLiveData: MutableLiveData<List<User>> = MutableLiveData()

    private val apiError: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        observeUsers()
    }

    fun observeUsers() {


        compositeDisposable.add(
            appRepository.getUsers()
                .subscribeOn(rxSchedule.io())
                .observeOn(rxSchedule.ui())
                .subscribe({
                    homeLiveData!!.value = it
                }, {
                    apiError!!.value = it.localizedMessage
                })
        )


    }

    fun fetchUsers(): LiveData<List<User>> {
        return homeLiveData!!
    }

    fun observeError(): LiveData<String> {
        return apiError
    }


}