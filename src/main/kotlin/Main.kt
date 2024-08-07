package org.example

import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isPresent
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.concurrent.thread

object Holder : ThreadLocal<Int>()

val pool = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

// fun main() = runBlocking{
suspend fun main() {
    Holder.set(11)

    val responses = Channel<Int>(1)
    thread(name = "send1", start = true) {
        Thread.sleep(1000)
        println("Sending, value:${Holder.get()} on ${Thread.currentThread().name}")
        responses.trySend(1)
    }
    thread(name = "send2", start = true) {
        Thread.sleep(2000)
        println("Sending, value:${Holder.get()} on ${Thread.currentThread().name}")
        responses.trySend(2)
    }
    thread(name = "closer", start = true) {
        Thread.sleep(10000)
        println("Closing channel")
        responses.close()
    }
//     works(responses)
    fails(responses)
}

private suspend fun works(responses: Channel<Int>) {
    withContext(Holder.asContextElement()) {
        println("Pre-loop, present:${Holder.isPresent()} value:${Holder.get()} on ${Thread.currentThread().name}")
        for (i in responses) {
            withContext(pool) {
                // Put a breakpoint on the next line as well as `ThreadLocalElement.updateThreadContext`
                println("Receiving $i, present:${Holder.isPresent()} value:${Holder.get()} on ${Thread.currentThread().name}")
            }
        }
    }
}

private suspend fun fails(responses: Channel<Int>) {
    withContext(Holder.asContextElement()) {
        println("Pre-loop, present:${Holder.isPresent()} value:${Holder.get()} on ${Thread.currentThread().name}")
        for (i in responses) {
            // Put a breakpoint on the next line as well as `ThreadLocalElement.updateThreadContext`
            println("Receiving $i, present:${Holder.isPresent()} value:${Holder.get()} on ${Thread.currentThread().name}")
        }
    }
}
