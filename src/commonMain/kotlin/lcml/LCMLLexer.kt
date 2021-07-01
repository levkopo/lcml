package lcml

val BLANK_CHARS = setOf(8.toChar(), 9.toChar(),
    11.toChar(), 12.toChar(), 32.toChar())

class LCMLException(lexer: LCMLLexer, message: String):
    Exception("$message ${lexer.position}")

class LCMLLexer(originalInput: String){
    var input = StringBuilder(originalInput)
    var currentToken: Token? = null
    var finished: Boolean = false
    var position = Position(1, 1)

    init {
        moveAhead()
    }

    fun moveAhead(needType: Token.Type? = null): Token {
        if(input.isEmpty()&&!finished) {
            finished = true
            return currentToken!!
        }

        if(finished) throw LCMLException(this, "Already finished: EOF")

        cleanup()
        if(findNextToken(needType)){
            while (currentToken?.type==Token.Type.NEW_LINE){
                position = Position(position.line+1, 1)

                if (input.isEmpty()) {
                    finished = true
                    throw LCMLException(this, "Already finished: EOF")
                }

                cleanup()
                findNextToken(needType)
            }

            return currentToken!!
        }

        finished = true
        if(input.isNotEmpty()){
            throw LCMLException(this, "Unexpected symbol: '" + input[0] + "'")
        }

        return currentToken!!
    }

    private fun cleanup() {
        var charsToDelete = 0

        while (BLANK_CHARS.contains(input[charsToDelete])) {
            charsToDelete++
            if (input.toString().length == charsToDelete) break
        }

        if (charsToDelete > 0) {
            position = Position(position.line, position.position+charsToDelete)
            input = input.deleteRange(0, charsToDelete)
        }
    }

    private fun findNextToken(needType: Token.Type? = null): Boolean {
        var tokenType = needType
        var tokenScan: MatchResult? = needType?.match(input.toString())

        if(tokenScan==null) {
            for (type in Token.Type.values()) {
                tokenScan = type.match(input.toString())
                tokenType = type
                if (tokenScan != null) break
            }
        }

        if(tokenScan!=null&&tokenType!=null){
            currentToken = Token(tokenType, tokenScan.groups.lastNotNull()!!.value)
            input = input.deleteRange(0, tokenScan.value.length)
            position = Position(position.line, position.position
                    +currentToken!!.value.length)

            return true
        }

        return false
    }
}