package org.example

import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isPresent
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread


object Holder : ThreadLocal<Int>()

suspend fun main() {
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
//    thread(start = true) {
//        Thread.sleep(3000)
//        println("Send context, HP:${Holder.get()} on ${Thread.currentThread().name}")
//        responses.trySend(3)
//    }
//    thread(start = true) {
//        Thread.sleep(4000)
//        println("Send context, HP:${Holder.get()} on ${Thread.currentThread().name}")
//        responses.trySend(4)
//    }

//    fails(responses)
//    fails2(responses)
    failsToo(responses)

}
private suspend fun failsToo(responses: Channel<Int>) {
    withContext(Holder.asContextElement()) {
        println("In context, HP:${Holder.isPresent()}:${Holder.get()} on ${Thread.currentThread().name}")
        for (i in responses) {
            println("Receiving $i HP:${Holder.isPresent()}:${Holder.get()} on ${Thread.currentThread().name}")
        }
    }
}

private suspend fun fails(responses: Channel<Int>) {
    withContext(Holder.asContextElement()) {
        println("In context, HP:${Holder.isPresent()}:${Holder.get()} on ${Thread.currentThread().name}")
        val x = flow {
            coroutineScope {
                for (i in responses) {
                    println("Receiving $i HP:${Holder.isPresent()}:${Holder.get()} on ${Thread.currentThread().name}")
                    emit(i)
                    println("emit $i HP:${Holder.isPresent()}:${Holder.get()} on ${Thread.currentThread().name}")
                }
            }
        }

        x.collect {
            println("Flow Receiving $it HP:${Holder.isPresent()}:${Holder.get()} on ${Thread.currentThread().name}")
        }


    }
}

private suspend fun fails2(responses: Channel<Int>) {
    withContext(Holder.asContextElement()) {
        println("In context, HP:${Holder.isPresent()}:${Holder.get()} on ${Thread.currentThread().name}")
        coroutineScope {
            for (i in responses) {
                println("Receiving $i HP:${Holder.isPresent()}:${Holder.get()} on ${Thread.currentThread().name}")
            }
        }
    }
}

