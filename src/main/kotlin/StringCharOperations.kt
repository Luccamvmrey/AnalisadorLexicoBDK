import reservedWords.ReservedWords

fun Char.isNumber(): Boolean = this in '0'..'9'

fun Char.isAddition(): Boolean = this == '+'

fun Char.isSubtraction(): Boolean = this == '-'

fun Char.isMultiplication(): Boolean = this == '*'

fun Char.isDivision(): Boolean = this == '/'

fun Char.isEquals(): Boolean = this == '='

fun Char.isEndOfLine(): Boolean = this == ';'

fun Char.isNegation(): Boolean = this == '!'

fun Char.isComment(): Boolean = this == '#'

fun Char.isPipe(): Boolean = this == '|'

fun Char.isAmpersand(): Boolean = this == '&'

fun Char.isText(): Boolean = this == '"'

fun Char.isCharacter(): Boolean = this == '\''

fun Char.isLessThanGreaterThan(): Boolean = this == '<' || this == '>'

fun Char.isMathOperator(): Boolean = this.isAddition() || this.isSubtraction() ||
        this.isMultiplication() || this.isDivision() || this == '%' || this == '^'

fun Char.isSymbol(): Boolean = this == '(' || this == ')' || this == '{' || this == '}' ||
        this == '[' || this == ']' || this == ',' || this == '.' || this == ':'

fun Char.isInvalid(): Boolean = this == '$' || this == '@' || this == '?' || this == '~'

fun String.isReservedWord(): Boolean {
    try {
        ReservedWords.valueOf(this.uppercase())
        return true
    } catch (e: IllegalArgumentException) {
        return false
    }
}

