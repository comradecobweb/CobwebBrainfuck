@file:JvmName("Utils")

fun areBracketsValid(code: String): Boolean {
    if (code.isEmpty()) return true
    if (!(code.contains('[') && code.contains(']'))) return true

    var open = 0

    for (c in code) {
        if (c == '[') open++
        else if (open == 0 && c == ']') return false
        else if (c == ']') open--
    }

    return open == 0
}