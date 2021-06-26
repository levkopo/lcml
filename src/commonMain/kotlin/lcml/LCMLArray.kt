package lcml

class LCMLArray(lexer: LCMLLexer,
                end: (LCMLLexer) -> Boolean): LCMLElement(lexer) {

    private val list = ArrayList<Any?>()

    constructor(input: String) : this(LCMLLexer(input), { lexer -> lexer.finished })

    init {
        while (!end(lexer)) {
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
                is LCMLElement -> value.toFullString()
                is String -> "\"$value\""
                else -> value.toString()
            }
        }

        return response
    }
}