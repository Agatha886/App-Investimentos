package br.com.brq.agatha.investimentos.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.brq.agatha.investimentos.repository.MoedaApiDataSource
import java.lang.IllegalArgumentException

class HomeViewModel(val dataSource: MoedaApiDataSource) : ViewModel() {
    var quandoFinaliza: () -> Unit = {}
    private  val eventRetornoDaApi = MutableLiveData<RetornoStadeApi>()
    val viewModelRetornoDaApi:LiveData<RetornoStadeApi> = eventRetornoDaApi

    fun buscaDaApi() {
        dataSource.buscaDaApi {
            eventRetornoDaApi.postValue(it)
            quandoFinaliza()
        }
    }

    class HomeViewModelFactory(private val context: Context): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(dataSource = MoedaApiDataSource(context)) as T
                }
                else -> {
                    throw IllegalArgumentException("Unknow ViewModel class")
                }
            }
        }
    }
}

