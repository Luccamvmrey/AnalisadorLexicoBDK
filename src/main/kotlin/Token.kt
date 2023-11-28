import types.TokenType

data class Token(
    var type: TokenType = TokenType.DEFAULT,
    var lexeme: String = "",
    var row: Int = 0,
    var column: Int = 0
)

fun Token.print() {
    println( """
        Token: $type
        Lexeme: $lexeme
        Row: $row
        Column: $column
        
    """.trimIndent()
    )
}