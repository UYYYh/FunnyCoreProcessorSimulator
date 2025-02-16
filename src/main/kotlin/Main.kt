package org.example

import x.Processor

fun main() {
    val program: String =
        """
li 2 0
li 4 0 
li 5 1
li 8 0
li 11 1
sub 3 0 4
sub 6 1 4
sub 9 1 4
shl 6 5
add 8 8 5
sub 7 0 6
jp 7 -3
sub 8 8 5
shl 9 8
sub 10 9 3
jp 10 4
sub 3 3 9
shl 11 8
bor 2 2 11
sub 8 8 5
sub 9 1 4
li 11 1
jp 8 -9
jz 8 -10
        """.trimIndent()

    val processor: Processor = Processor(program, 7, 12345)
    processor.execute()
    println(processor)
}
