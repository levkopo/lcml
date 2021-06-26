package lcml

class Token(val type: Type, val value: String) {

    enum class Type(regex: String) {
        NEW_LINE ("\\R"),
        ASSIGN ("\\="),
        OPEN ("\\("),
        CLOSE ("\\)"),
        COMMA (","),
        OPEN_SQUARE_BRACKET("\\["),
        CLOSE_SQUARE_BRACKET("\\]"),
        OPEN_BRACKET ("\\{"),
        CLOSE_BRACKET ("\\}"),

        BOOL ("true|false"),
        NULL ("null"),
        STRING ("\"([^\"]+)\"|\"()\"|\'([^\']+)\'|\'()\'"),
        DOUBLE ("\\d*\\.\\d+"),
        LONG ("(\\d+)[lL]"),
        INTEGER ("\\d+"),
        IDENTIFIER ("(\\w+)");

        private val regex = Regex("^$regex")

        fun match(s: String): MatchResult? {
            return regex.find(s)
        }
    }
}