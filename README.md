# CobwebBrainfuck


`CobwebBrainfuck` is a brainfuck implementation written in `Java` and `Kotlin`. It supports custom memory length and 
compilation to `C11`.

## License

This program is distributed under the MIT license, [see details](LICENSE.md).

## Brainfuck

[Brainfuck](https://en.wikipedia.org/wiki/Brainfuck) is an esoteric programming language. This version of the language 
is slightly different from the original, see [here](SPECIFICATION.md).


## Running the app

```
java -jar CobwebBrainfuck.jar options
```

### Options

* `c`, `-c`, `--c`, `compile`, `-compile`, `--compile` turns on compilation mode
  * `source`, `-source`, `--source` sets input path.
  * `destination`, `-destination`, `--destination` sets output path.

* `i`, `-i`, `--i`, `interpret`, `-interpret`, `--interpret` turns on interpretation mode.
  * `source`, `-source`, `--source` sets input path.
  * `memory`, `-memory`, `--memory`, `mem`, `-mem`, `--mem` `[value greater than 1]`  (optional) sets the number of 
memory cells (default ***30000***).
* `h`, `-h`, `--h`, `help`, `-help`, `--help` displays help info.