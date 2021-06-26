package lcml

abstract class LCMLElement(private val lexer: LCMLLexer) {
    var attributes: LCMLObject? = null

    fun parseValue(): Any? {
        return when(lexer.currentToken!!.type){
            Token.Type.NULL -> null
            Token.Type.BOOL -> lexer.currentToken!!.value == "true"
            Token.Type.DOUBLE -> lexer.currentToken!!.value.toDouble()
            Token.Type.LONG -> lexer.currentToken!!.value.toLong()
            Token.Type.INTEGER -> lexer.currentToken!!.value.toInt()
            Token.Type.STRING -> lexer.currentToken!!.value
            else -> throw Exception()
        }
    }

    fun parseObject(): LCMLObject {
        return LCMLObject(lexer) { lexer -> lexer.currentToken!!.type == Token.Type.CLOSE_BRACKET }
    }

    fun parseArray(): LCMLArray {
        return LCMLArray(lexer) { lexer -> lexer.currentToken!!.type == Token.Type.CLOSE_SQUARE_BRACKET }
    }

    abstract fun toFullString(): String
}