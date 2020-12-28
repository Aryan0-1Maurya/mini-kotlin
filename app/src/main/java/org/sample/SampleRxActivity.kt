package org.sample

import android.os.Bundle
import com.minikorp.grove.ConsoleLogTree
import com.minikorp.grove.Grove
import mini.LoggerInterceptor
import mini.MiniGen
import mini.rx.android.activities.FluxRxActivity
import mini.rx.flowable
import org.sample.databinding.HomeActivityBinding

class SampleRxActivity : FluxRxActivity() {

    private val dispatcher = MiniGen.newDispatcher()
    private val dummyStore = DummyStore()

    private lateinit var binding: HomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stores = listOf(dummyStore)
        MiniGen.subscribe(dispatcher, stores)
        stores.forEach { it.initialize() }

        dummyStore.flowable()
            .subscribe {
                binding.demoText.text = it.text
            }
            .track()

        Grove.plant(ConsoleLogTree())
        dispatcher.addInterceptor(LoggerInterceptor(stores, { tag, msg ->
            Grove.tag(tag).d { msg }
        }))

        dispatcher.dispatch(ActionTwo("2"))
    }
}