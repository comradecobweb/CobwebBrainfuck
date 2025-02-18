class C(path: String, tab: String = "    ", newLine: String = "\n") :
    Compiler(
        path, tab, newLine, "template.c", Instructions(
            "plus();",
            "minus();",
            "left_arrow();",
            "right_arrow();",
            "comma();",
            "dot();",
            "while(memory[memory_pointer]) {",
            "};"
        )
    )