package lcml

class LCMLArray(lexer: LCMLLexer?,
                end: (LCMLLexer) -> Boolean): LCMLElement<Int>(lexer) {

    private val list = ArrayList<Any?>()
    constructor(input: String) : this(LCMLLexer(input), { lexer -> lexer.finished })
    constructor() : this(null, { false })

    init {
        while (lexer!=null&&!end(lexer)) {
            var attributes: LCMLObject? = null

            var type = lexer.moveAhead(Token.Type.OPEN).type
            if (type == Token.Type.OPEN) {
                lexer.moveAhead()
                attributes = LCMLObject(lexer) { it.currentToken!!.type == Token.Type.CLOSE }

                type = lexer.moveAhead().type
            }

            list.add(when (type) {
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
                    if (attributes != null)
                        throw Exception()

                    lexer.moveAhead()
                    parseValue()
                }

                else -> throw Exception()
            })

            lexer.moveAhead()
        }
    }

    override fun toFullString(): String {
        return (if (attributes == null)
            "" else "($attributes)") + "[$this]"
    }

    override fun toString(): String {
        var response = ""
        for (value in list) {
            response += when (value) {
                is LCMLElement<*> -> value.toFullString()
                is String -> "\"$value\""
                else -> value.toString()
            }
        }

        return response
    }

    override fun get(key: Int, defaultValue: Any?): Any? {
        return list[key]?:defaultValue
    }

    override fun getArray(key: Int, defaultValue: LCMLArray?): LCMLArray? {
        return list[key]?.let {
            if(it is LCMLArray) it else defaultValue
        }?:defaultValue
    }

    override fun getObject(key: Int, defaultValue: LCMLObject?): LCMLObject? {
        return list[key]?.let {
            if(it is LCMLObject) it else defaultValue
        }?:defaultValue
    }

    override fun getString(key: Int, defaultValue: String?): String? {
        return list[key]?.let {
            if(it is String) it else defaultValue
        }?:defaultValue
    }

    override fun getInt(key: Int, defaultValue: Int?): Int? {
        return list[key]?.let {
            if(it is Int) it else defaultValue
        }?:defaultValue
    }

    override fun getLong(key: Int, defaultValue: Long?): Long? {
        return list[key]?.let {
            if(it is Long) it else defaultValue
        }?:defaultValue
    }

    override fun getDouble(key: Int, defaultValue: Double?): Double? {
        return list[key]?.let {
            if(it is Double) it else defaultValue
        }?:defaultValue
    }

    override fun getBoolean(key: Int, defaultValue: Boolean?): Boolean? {
        return list[key]?.let {
            if(it is Boolean) it else defaultValue
        }?:defaultValue
    }

    override fun getElementAttributes(key: Int): LCMLObject? {
        return list[key]?.let {
            if(it is LCMLElement<*>) it.attributes else null
        }
    }

    override fun put(key: Int, value: Any?): LCMLArray {
        list.add(key, value)
        return this
    }

    fun put(value: Any?): LCMLArray {
        list.add(value)
        return this
    }
}