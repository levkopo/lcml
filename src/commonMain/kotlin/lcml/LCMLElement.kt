package lcml

abstract class LCMLElement<GetterValue>(private val lexer: LCMLLexer?) {
    var attributes: LCMLObject? = null

    fun parseValue(): Any? {
        return when(lexer?.currentToken?.type){
            Token.Type.NULL -> null
            Token.Type.BOOL -> lexer.currentToken!!.value == "true"
            Token.Type.DOUBLE -> lexer.currentToken!!.value.toDouble()
            Token.Type.LONG -> lexer.currentToken!!.value.toLong()
            Token.Type.INTEGER -> lexer.currentToken!!.value.toInt()
            Token.Type.STRING -> lexer.currentToken!!.value
            else -> throw LCMLException(lexer!!, "Invalid input: expected primitive value")
        }
    }

    /** Getters */
    abstract fun get(key: GetterValue, defaultValue: Any? = null): Any?
    abstract fun getArray(key: GetterValue, defaultValue: LCMLArray? = null): LCMLArray?
    abstract fun getObject(key: GetterValue, defaultValue: LCMLObject? = null): LCMLObject?
    abstract fun getString(key: GetterValue, defaultValue: String? = null): String?
    abstract fun getInt(key: GetterValue, defaultValue: Int? = null): Int?
    abstract fun getLong(key: GetterValue, defaultValue: Long? = null): Long?
    abstract fun getDouble(key: GetterValue, defaultValue: Double? = null): Double?
    abstract fun getBoolean(key: GetterValue, defaultValue: Boolean? = null): Boolean?
    abstract fun getElementAttributes(key: GetterValue): LCMLObject?

    /** Put */
    abstract fun put(key: GetterValue, value: Any?): LCMLElement<GetterValue>

    fun parseObject(): LCMLObject {
        return LCMLObject(lexer) { lexer -> lexer.currentToken!!.type == Token.Type.CLOSE_BRACKET }
    }

    fun parseArray(): LCMLArray {
        return LCMLArray(lexer) { lexer -> lexer.currentToken!!.type == Token.Type.CLOSE_SQUARE_BRACKET }
    }

    abstract fun toFullString(): String
}