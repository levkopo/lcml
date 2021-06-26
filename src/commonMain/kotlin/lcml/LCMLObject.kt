package lcml

class LCMLObject(
    lexer: LCMLLexer,
    end: (LCMLLexer) -> Boolean): LCMLElement(lexer) {

    private val map = HashMap<String, Any?>()

    constructor(input: String): this(LCMLLexer(input), { lexer -> lexer.finished })

    init {
        while (!end(lexer)){
            if(lexer.currentToken!!.type!=Token.Type.IDENTIFIER)
                throw Exception()

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

                else -> throw Exception()
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
                is LCMLElement -> key+value.toFullString()
                is String -> "$key=\"$value\""
                else -> "$key=$value"
            }
        }

        return response
    }
}