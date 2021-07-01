package lcml

class LCMLObject(
    lexer: LCMLLexer?,
    end: (LCMLLexer) -> Boolean): LCMLElement<String>(lexer) {

    private val map = HashMap<String, Any?>()

    constructor(input: String): this(LCMLLexer(input), { lexer -> lexer.finished })
    constructor(): this(null, { false })

    init {
        while (lexer!=null&&!end(lexer)){
            if(lexer.currentToken!!.type!=Token.Type.IDENTIFIER)
                throw LCMLException(lexer, "Invalid input: expected name of parameter")

            val name = lexer.currentToken!!.value
            var attributes: LCMLObject? = null

            var type = lexer.moveAhead(Token.Type.OPEN).type
            if(type==Token.Type.OPEN){
                lexer.moveAhead()
                attributes = LCMLObject(lexer) { it.currentToken!!.type == Token.Type.CLOSE }

                type = lexer.moveAhead().type
            }

            map[name] = when(type) {
                Token.Type.OPEN_BRACKET -> {
                    lexer.moveAhead()
                    parseObject().apply {
                        this.attributes = attributes
                    }
                }

                Token.Type.OPEN_SQUARE_BRACKET -> {
                    lexer.moveAhead()
                    parseArray().apply {
                        this.attributes = attributes
                    }
                }

                Token.Type.ASSIGN -> {
                    if(attributes!=null)
                        throw Exception()

                    lexer.moveAhead()
                    parseValue()
                }

                else -> throw LCMLException(lexer, "Invalid input: expected value")
            }

            lexer.moveAhead()
        }
    }

    override fun toFullString(): String {
        return (if(attributes==null)
                    "" else "($attributes)")+"{$this}"
    }

    override fun toString(): String {
        var response = ""
        for((key, value) in map){
            response += when (value) {
                is LCMLElement<*> -> key+value.toFullString()
                is String -> "$key=\"$value\""
                is Int -> "$key=${value}i"
                is Long -> "$key=${value}l"
                else -> "$key=$value"
            }
        }

        return response
    }

    override fun get(key: String, defaultValue: Any?): Any? {
        return map[key]?:defaultValue
    }

    override fun getArray(key: String, defaultValue: LCMLArray?): LCMLArray? {
        return map[key]?.let {
            if(it is LCMLArray) it else defaultValue
        }?:defaultValue
    }

    override fun getObject(key: String, defaultValue: LCMLObject?): LCMLObject? {
        return map[key]?.let {
            if(it is LCMLObject) it else defaultValue
        }?:defaultValue
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return map[key]?.let {
            if(it is String) it else defaultValue
        }?:defaultValue
    }

    override fun getInt(key: String, defaultValue: Int?): Int? {
        return map[key]?.let {
            if(it is Int) it else defaultValue
        }?:defaultValue
    }

    override fun getLong(key: String, defaultValue: Long?): Long? {
        return map[key]?.let {
            if(it is Long) it else defaultValue
        }?:defaultValue
    }

    override fun getDouble(key: String, defaultValue: Double?): Double? {
        return map[key]?.let {
            if(it is Double) it else defaultValue
        }?:defaultValue
    }

    override fun getBoolean(key: String, defaultValue: Boolean?): Boolean? {
        return map[key]?.let {
            if(it is Boolean) it else defaultValue
        }?:defaultValue
    }

    override fun getElementAttributes(key: String): LCMLObject? {
        return map[key]?.let {
            if(it is LCMLElement<*>) it.attributes else null
        }
    }

    override fun put(key: String, value: Any?): LCMLObject {
        map[key] = value
        return this
    }
}