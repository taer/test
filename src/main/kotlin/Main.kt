package org.example

import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isPresent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread


object Holder : ThreadLocal<Int>()

fun main() = runBlocking{
//suspend fun main() {
    Holder.set(11)

    val responses = Channel<Int>(1)
    thread(start = true) {
        Thread.sleep(1000)
        println("Send context, HP:${Holder.get()} on ${Thread.currentThread().name}")
        responses.trySend(1)
    }
    thread(start = true) {
        Thread.sleep(2000)
        println("Send context, HP:${Holder.get()} on ${Thread.currentThread().name}")
        responses.trySend(2)
    }
    withContext(Holder.asContextElement()) {
        println("In context, HP:${Holder.isPresent()}:${Holder.get()} on ${Thread.currentThread().name}")
        for (i in responses) {
            println("Receiving $i HP:${Holder.isPresent()}:${Holder.get()} on ${Thread.currentThread().name}")
        }
    }

}

