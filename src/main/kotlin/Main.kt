import types.Lexer

fun main() {
    val lexer = Lexer("src/main/kotlin/input.txt")

    do {
        val token = lexer.lex()

        token?.print()
    } while (token != null)
}