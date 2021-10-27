package br.com.brq.agatha.investimentos.viewModel.base

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object AppContextProvider : CoroutinesContextProvider {
    var coroutinesContextProviderDelegate: CoroutinesContextProvider? = null

    override val main: CoroutineContext by lazy {
        coroutinesContextProviderDelegate?.main ?: Dispatchers.Main
    }
    override val io: CoroutineContext by lazy {
        coroutinesContextProviderDelegate?.io ?: Dispatchers.IO
    }
}

interface CoroutinesContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
}

class TestContextProvider : CoroutinesContextProvider {
    override val main: CoroutineContext = Dispatchers.Unconfined
    override val io: CoroutineContext = Dispatchers.Unconfined
}