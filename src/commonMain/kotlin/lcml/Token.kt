package lcml

class Token(val type: Type, val value: String) {

    enum class Type(regex: String) {
        NEW_LINE ("\\R"),
        ASSIGN ("\\="),
        OPEN ("\\("),
        CLOSE ("\\)"),
        OPEN_SQUARE_BRACKET("\\["),
        CLOSE_SQUARE_BRACKET("\\]"),
        OPEN_BRACKET ("\\{"),
        CLOSE_BRACKET ("\\}"),

        BOOL ("true|false"),
        NULL ("null"),
        STRING ("\"([^\"]+)\"|\"()\"|\'([^\']+)\'|\'()\'"),
        DOUBLE ("\\d*\\.\\d+|\\-\\d*.\\d+"),
        LONG ("(\\d+|\\-\\d+)[lL]"),
        INTEGER ("\\d+|\\-\\d+"),
        IDENTIFIER ("(\\w+)");

        private val regex = Regex("^$regex")

        fun match(s: String): MatchResult? {
            return regex.find(s)
        }
    }
}