class BracketException(code: String) : Exception() {
    override val message: String
    init {
        var column = 1
        var line = 1

        var open = 0
        for (c in code) {
            if (c == '\n') {
                column = 0
                line++
                break
            } else if (c == '[') open++
            else if (open == 0 && c == ']') break
            else if (c == ']') open--
            column++
        }
        message = "Invalid bracket at line $line, column $column"
    }
}