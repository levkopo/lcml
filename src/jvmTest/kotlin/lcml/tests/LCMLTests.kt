package lcml.tests

import lcml.LCMLArray
import lcml.LCMLObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LCMLTests {

    @Test
    fun parseObject() {
        val lcmlObject = LCMLObject("""
            id = 4i
            name = "V. V. Putin"
            birthday = -551664000l
            reignEnd = "endless"
        """.trimIndent())

        assertEquals(4, lcmlObject.getInt("id"), "Int not parsed")
        assertEquals("V. V. Putin", lcmlObject.getString("name"), "String not parsed")
        assertEquals(-551664000L, lcmlObject.getLong("birthday"), "Negative long not parsed")
        assertEquals("endless", lcmlObject.getString("reignEnd"), "String #2 not parsed") //Эх...
        assertNull(lcmlObject.getLong("deathTime"), "Unknown value in object")
    }

    @Test
    fun createObject(){
        val lcmlObject = LCMLObject()
            .put("string", "yes")
            .put("bool", true)
            .put("integer", 1)
            .put("long", 1L)
            .put("double", 1.toDouble())
            .put("valnull", null)

        val reparseObject = LCMLObject(lcmlObject.toString())
        assertEquals("yes", reparseObject.getString("string"), "String not encoded")
        assertEquals(true, reparseObject.getBoolean("bool"), "Boolean not encoded")
        assertEquals(1, reparseObject.getInt("integer"), "Int not encoded")
        assertEquals(1L, reparseObject.getLong("long"), "Long not encoded")
        assertEquals(1.toDouble(), reparseObject.getDouble("double"), "Double not encoded")
        assertNull(reparseObject.get("valnull"), "Null not encoded")
    }

    @Test
    fun parseArray() {
        val ignoreList = LCMLArray("""
            "VitiaCat"
            "Unknown Number"
            123i
            456l
        """.trimIndent())

        assertEquals("VitiaCat", ignoreList.getString(0))
        assertEquals("Unknown Number", ignoreList.getString(1))
        assertEquals(123, ignoreList.getInt(2))
        assertEquals(456L, ignoreList.getLong(3))
    }

    @Test
    fun exampleTest() {
        val lcmlObject = LCMLObject("""
            document {
                title = "LCML simple document"
                authors [
                    "levkopo"
                ]
            }

            database {
                server = "192.168.0.1"
                port = 8923i
                maxConnections = 50i
            }

            servers (
                default = 2i
            ) [
                (type = "alpha"){
                    ip = "127.0.0.1"
                    port = 80i
                }
                (type = "beta"){
                    ip = "127.0.0.2"
                    port = 80i
                }
                (type = "release"){
                    ip = "127.0.0.3"
                    port = 433i
                }
            ]
        """.trimIndent())
        println(lcmlObject)
    }
}