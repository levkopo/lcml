package lcml.tests

import lcml.LCMLObject
import org.junit.Test

class LCMLTests {

    @Test
    fun parse() {
        val lcmlObject = LCMLObject("""
            id = 4
            name = "V. V. Putin"
            birthday = -551664000l
            reignEnd = "endless"
        """.trimIndent())
        println(lcmlObject)
    }
}