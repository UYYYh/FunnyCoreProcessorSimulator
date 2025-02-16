package org.example

import x.Processor

fun main() {
    val mutableMap: MutableMap<String, String> =
        mutableMapOf(
            "hello" to "world",
            "123" to "456",
        )

    val program: String =
        """
        li 5 1 
li 4 0 
li 2 0
sub 0 0 1
add 2 2 5 
jp 0 -2
jz 0 3 
sub 2 2 5
add 0 0 1
sub 3 0 4
        """.trimIndent()

    val processor: Processor = Processor(program, 7, 10000)
    processor.execute()
    println(processor)
}
