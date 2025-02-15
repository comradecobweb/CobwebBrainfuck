# CobwebBrainfuck Implementation Specification


## Memory

The memory is a `char` array of `30000` elements. The pointer is initially set to `0` element.


## Instructions

### +

Increases the value of the currently selected cell. If it exceeds the maximum value for `char` type, the value is set 
to `0`.

### -

Decreases the value of the currently selected cell. If it exceeds `0`, the value is set to the maximum value for type
`char`.

### \>

Moves the pointer to the right. If the pointer has a value of `29999`, it will be set to `0`.

### <

Moves the pointer to the left. If the pointer is `0`, it will be set to `29999`.

### .

Displays the character stored in the currently selected memory cell.

### ,

Reads a single character from the user and stores it in memory in the currently selected cell. If more characters are
entered, the rest are ignored.

### [

If the value of the currently selected cell is different from `0`, it executes the code between the brackets. 
If the value of the current cell is `0`, it sets the pointer to the position of the nearest next `]`.

### ]

Sets the pointer value to the position of the nearest previous `[`.


### About brackets

Each `[` must be matched by a `]`. Brackets can be nested. A right bracket `]` cannot appear before a left `[`.