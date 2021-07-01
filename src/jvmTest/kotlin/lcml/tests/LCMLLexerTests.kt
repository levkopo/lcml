package lcml.tests

import lcml.LCMLLexer
import lcml.Token
import org.junit.Test
import kotlin.test.assertEquals

class LCMLLexerTests {

    @Test
    fun integer() {
        assertEquals(
            Token.Type.INTEGER,
            LCMLLexer("2005i test").currentToken!!.type)
        assertEquals(
            Token.Type.INTEGER,
            LCMLLexer("-2005i test").currentToken!!.type)
    }

    @Test
    fun long() {
        assertEquals(
            Token.Type.LONG,
            LCMLLexer("2005l test").currentToken!!.type)
        assertEquals(
            Token.Type.LONG,
            LCMLLexer("-2005l test").currentToken!!.type)
    }

    @Test
    fun double() {
        assertEquals(
            Token.Type.DOUBLE,
            LCMLLexer("2005.5 test").currentToken!!.type)
        assertEquals(
            Token.Type.DOUBLE,
            LCMLLexer("-2005.5 test").currentToken!!.type)
        assertEquals(
            Token.Type.DOUBLE,
            LCMLLexer("-2005d test").currentToken!!.type)
    }

    @Test
    fun identifier() {
        assertEquals(
            Token.Type.IDENTIFIER,
            LCMLLexer("im russian!!!!!!!!!!!").currentToken!!.type)
    }

    @Test
    fun string() {
        assertEquals(
            Token.Type.STRING,
            LCMLLexer("\"some string\"").currentToken!!.type)
        assertEquals(
            Token.Type.STRING,
            LCMLLexer("'опять some string'").currentToken!!.type)
    }
}