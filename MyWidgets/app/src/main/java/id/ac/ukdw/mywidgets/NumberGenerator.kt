package id.ac.ukdw.mywidgets

import kotlin.random.Random


internal object NumberGenerator {
    fun generate(max: Int): Int {
        return Random.nextInt(0,max)
    }
}