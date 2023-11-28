package types

import Token
import isAmpersand
import isCharacter
import isComment
import isEndOfLine
import isEquals
import isInvalid
import isLessThanGreaterThan
import isMathOperator
import isNegation
import isNumber
import isPipe
import isReservedWord
import isSymbol
import isText
import java.nio.file.Files
import java.nio.file.Paths

class Lexer(
    inputFile: String
) {
    private lateinit var text: CharArray
    private lateinit var currentLexeme: String
    private var next = 0
    private var row = 1
    private var column = 0

    init {
        try {
            val bytes = Files.readAllBytes(Paths.get(inputFile))
            text = String(bytes).toCharArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun lex(): Token? {
        var state = 0
        var currentToken = Token()
        var isValid = true
        currentLexeme = ""

        while (true) {
            if (next == text.size) {
                return null
            }

            var c = text[next]
            next++
            column++

            if (c == '\n') {
                row++
                column = 0
            }

            when (state) {
                0 -> {
                    if (c.isWhitespace()) {
                        state = 0
                    } else if (c.isLetter()) {
                        state = 1
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isNumber()) {
                        state = 4
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isEquals()) {
                        state = 7
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isLessThanGreaterThan()) {
                        state = 10
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isMathOperator()) {
                        state = 12
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isAmpersand()) {
                        state = 14
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isPipe()) {
                        state = 16
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isNegation()) {
                        state = 18
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isText()) {
                        state = 21
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isCharacter()) {
                        state = 22
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isEndOfLine()) {
                        state = 23
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isSymbol()) {
                        state = 24
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else if (c.isComment()) {
                        state = 25
                        if (isValid) {
                            currentToken.column = column
                        }
                        appendToLexeme(c)
                    } else {
                        state = 0
                        isValid = false
                    }
                }

                1 -> {
                    if (c.isLetter() || c.isNumber()) {
                        state = 1
                        appendToLexeme(c)
                    } else if (c.isInvalid()) {
                        state = 1
                        isValid = false
                        appendToLexeme(c)
                    } else {
                        if (!isValid) {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.INVALID_CHARACTER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        } else if (currentLexeme.isReservedWord()) {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.RESERVED_WORD,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        } else {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.IDENTIFIER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        }
                    }
                }

                4 -> {
                    if (c.isNumber()) {
                        state = 4
                        appendToLexeme(c)
                    } else if (c.isInvalid()) {
                        state = 4
                        isValid = false
                        appendToLexeme(c)
                    } else {
                        if (!isValid) {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.INVALID_CHARACTER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        } else {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.NUMBER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        }
                    }
                }

                7 -> {
                    if (c.isEquals()) {
                        appendToLexeme(c)
                        if (!isValid) {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.INVALID_CHARACTER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        }
                        currentToken = currentToken.copy(
                            type = TokenType.COMPARISON_OP,
                            lexeme = currentLexeme,
                            row = row
                        )
                    } else if (c.isInvalid()) {
                        state = 7
                        isValid = false
                        appendToLexeme(c)
                    } else {
                        if (!isValid) {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.INVALID_CHARACTER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        } else {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.ASSIGNMENT_OP,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        }
                    }
                }

                10 -> {
                    if (!isValid) {
                        next--
                        column--
                        currentToken = currentToken.copy(
                            type = TokenType.INVALID_CHARACTER,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    } else {
                        appendToLexeme(c)
                        currentToken = currentToken.copy(
                            type = TokenType.COMPARISON_OP,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    }
                }

                12 -> {
                    if (c.isEquals()) {
                        appendToLexeme(c)
                        currentToken = currentToken.copy(
                            type = TokenType.ASSIGNMENT_OP,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    } else if (c.isInvalid()) {
                        state = 12
                        isValid = false
                        appendToLexeme(c)
                    } else {
                        if (!isValid) {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.INVALID_CHARACTER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        } else {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.MATH_OPERATOR,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        }
                    }
                }

                14 -> {
                    if (c.isAmpersand()) {
                        appendToLexeme(c)
                        currentToken = currentToken.copy(
                            type = TokenType.COMPARISON_OP,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    } else if (c.isInvalid()) {
                        state = 17
                        isValid = false
                        appendToLexeme(c)
                    } else {
                        if (!isValid) {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.INVALID_CHARACTER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        } else {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.COMPARISON_OP,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        }
                    }
                }

                16 -> {
                    if (c.isPipe()) {
                        appendToLexeme(c)
                        currentToken = currentToken.copy(
                            type = TokenType.COMPARISON_OP,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    } else if (c.isInvalid()) {
                        state = 16
                        isValid = false
                        appendToLexeme(c)
                    } else {
                        if (!isValid) {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.INVALID_CHARACTER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        } else {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.COMPARISON_OP,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        }
                    }
                }

                18 -> {
                    if (c.isEquals()) {
                        appendToLexeme(c)
                        currentToken = currentToken.copy(
                            type = TokenType.COMPARISON_OP,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    } else if (c.isInvalid()) {
                        state = 18
                        isValid = false
                        appendToLexeme(c)
                    } else {
                        if (!isValid) {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.INVALID_CHARACTER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        } else {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.NEGATION,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        }
                    }
                }

                21 -> {
                    if (c.isText()) {
                        appendToLexeme(c)
                        currentToken = currentToken.copy(
                            type = TokenType.TEXT,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    } else {
                        state = 21
                        appendToLexeme(c)
                    }
                }

                22 -> {
                    if (c.isCharacter()) {
                        appendToLexeme(c)
                        currentToken = currentToken.copy(
                            type = TokenType.CHARACTER,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    } else {
                        state = 22
                        appendToLexeme(c)
                    }
                }

                23 -> {
                    if (!isValid) {
                        next--
                        column--
                        currentToken = currentToken.copy(
                            type = TokenType.INVALID_CHARACTER,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    } else {
                        appendToLexeme(c)
                        currentToken = currentToken.copy(
                            type = TokenType.END_OF_LINE,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    }
                }

                24 -> {
                    if (c.isSymbol()) {
                        state = 24
                        appendToLexeme(c)
                    } else if (c.isSymbol()) {
                        state = 26
                        appendToLexeme(c)
                        isValid = false
                    } else {
                        if (!isValid) {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.INVALID_CHARACTER,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        } else {
                            next--
                            column--
                            currentToken = currentToken.copy(
                                type = TokenType.SYMBOL,
                                lexeme = currentLexeme,
                                row = row
                            )
                            return currentToken
                        }
                    }
                }

                25 -> {
                    if (c == '\r' || c == '\n') {
                        next--
                        column--
                        currentToken = currentToken.copy(
                            type = TokenType.COMMENT,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    } else if (!isValid) {
                        next--
                        column--
                        currentToken = currentToken.copy(
                            type = TokenType.INVALID_CHARACTER,
                            lexeme = currentLexeme,
                            row = row
                        )
                        return currentToken
                    } else {
                        state = 25
                        appendToLexeme(c)
                    }
                }
            }
        }
    }

    fun appendToLexeme(c: Char) {
        currentLexeme += c
    }
}
