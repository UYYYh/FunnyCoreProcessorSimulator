package x

import kotlin.reflect.KFunction
import kotlin.reflect.jvm.isAccessible

class Processor(
    program: String,
    divisor: Int,
    dividend: Int,
) {
    private val programLines = program.split("\n")
    private var pointer: Int = 0
    private var cycles: Int = 0
    private val registers: MutableMap<Int, Int> =
        mutableMapOf(
            0 to dividend,
            1 to divisor,
        )
    private val stringToFunctionMap: Map<String, KFunction<Unit>> =
        mapOf(
            "add" to ::add,
            "mul" to ::mul,
            "sub" to ::sub,
            "shl" to ::shl,
            "shr" to ::shr,
            "bor" to ::bor,
            "band" to ::band,
            "li" to ::li,
            "jz" to ::jz,
            "jp" to ::jp,
        )

    fun execute() {
        while (pointer < programLines.size) {
            executeLine(programLines[pointer].trim())
            pointer++
        }
    }

    fun getRegisterValues(): MutableMap<Int, Int> = registers

    override fun toString(): String =
        "Cycles: $cycles\n Registers:\n" +
            registers
                .toSortedMap()
                .map { (k, v) -> "R$k: $v" }
                .joinToString("\n")

    private fun executeLine(line: String) {
        println(line)
        println(registers.toString() + "$pointer")
        val components = line.split(" ")
        val opcode: String = components[0]
        val function: KFunction<Unit> = stringToFunctionMap[opcode]!!
        function.isAccessible = true
        val args = components.drop(1).map { it.toInt() }.toTypedArray()
        function.call(*args)
        recordCycle(opcode)
    }

    private fun recordCycle(opcode: String) {
        if (opcode in listOf("jz", "jp", "mul")) {
            cycles += 2
        }
        cycles++
    }

    private fun add(
        d: Int,
        a: Int,
        b: Int,
    ) {
        registers.compute(d) { _, _ -> registers[a]!! + registers[b]!! }
    }

    private fun sub(
        d: Int,
        a: Int,
        b: Int,
    ) {
        registers.compute(d) { _, _ -> registers[a]!! - registers[b]!! }
    }

    private fun mul(
        d: Int,
        a: Int,
        b: Int,
    ) {
        registers.compute(d) { _, _ -> registers[a]!! * registers[b]!! }
    }

    private fun shl(
        d: Int,
        a: Int,
    ) {
        registers.compute(d) { _, v -> v!! shl registers[a]!! }
    }

    private fun shr(
        d: Int,
        a: Int,
    ) {
        registers.compute(d) { _, v -> v!! shr registers[a]!! }
    }

    private fun bor(
        d: Int,
        a: Int,
        b: Int,
    ) {
        registers.compute(d) { _, _ -> registers[a]!! or registers[b]!! }
    }

    private fun band(
        d: Int,
        a: Int,
        b: Int,
    ) {
        registers.compute(d) { _, _ -> registers[a]!! and registers[b]!! }
    }

    private fun li(
        d: Int,
        i: Int,
    ) {
        registers.compute(d) { _, _ -> i }
    }

    private fun jz(
        a: Int,
        i: Int,
    ) {
        if (registers[a]!! == 0) {
            pointer += adjust(i)
        }
    }

    private fun jp(
        a: Int,
        i: Int,
    ) {
        if (registers[a]!! > 0) {
            pointer += adjust(i)
        }
    }

    private fun adjust(i: Int): Int = i - 1
}
