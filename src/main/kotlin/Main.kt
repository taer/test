package org.example

import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isPresent
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors


object Holder :ThreadLocal<Int>()
val pool = Executors.newCachedThreadPool().asCoroutineDispatcher()
suspend fun main() {

    Holder.set(11)
    val chan = Channel<Unit>()

    withContext(pool + Holder.asContextElement()){
        launch {
            for (x in chan){
                println("Receiving Holder present is ${Holder.isPresent()} on ${Thread.currentThread().name}")
            }
        }
        launch {
            for (x in 0..10){
                println("Sending Holder present is ${Holder.isPresent()} on ${Thread.currentThread().name}")
                chan.send(Unit)
            }
        }
    }
}