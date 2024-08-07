With the works code

suspend fun main
```
Pre-loop, present:true value:11 on main
Sending, value:null on send1
Receiving 1, present:true value:11 on pool-1-thread-1
Sending, value:null on send2
Receiving 2, present:true value:11 on pool-1-thread-2
Closing channel
```
fun main = runBlocking
```
Pre-loop, present:true value:11 on main
Sending, value:null on send1
Receiving 1, present:true value:11 on pool-1-thread-1
Sending, value:null on send2
Receiving 2, present:true value:11 on pool-1-thread-2
Closing channel
```

with the fails code, 

suspend fun main  <--- this is the broken one
```
Pre-loop, present:true value:11 on main
Sending, value:null on send1
Receiving 1, present:true value:null on send1
Sending, value:null on send2
Receiving 2, present:true value:null on send2
Closing channel
```
fun main = runBlocking
```
Pre-loop, present:true value:11 on main
Sending, value:null on send1
Receiving 1, present:true value:11 on main
Sending, value:null on send2
Receiving 2, present:true value:11 on main
Closing channel
```