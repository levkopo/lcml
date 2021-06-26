package lcml.tests

import lcml.LCMLObject
import org.junit.Test

class LCMLObjectTests {

    @Test
    fun parse() {
        val lcmlObject = LCMLObject("""
            test = 123
            
            object(
                attribute = 123
            ){
                test = "Da"
                testd = true
            }
            
            array(
                test = 234
            )[123 {test=123}]
        """.trimIndent())
        println(lcmlObject)
    }
}