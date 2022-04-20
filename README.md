KSON (Kong's Object Notation) is a lightweight data-interchange format.
It is easy for humans to read and write. It is easy for machines to parse and generate.
It has a build-in lisp-like interperter.

KSON is built on three structures:
- A collection of name/value pairs. In various languages, this is realized as an object, record, struct, dictionary, hash table, keyed list, or associative array.
- An ordered array of values. In most languages, this is realized as an array, vector, or sequence.
- An ordered list of values. In most languages, this is realized as a single linked list


int64
```
5
```

double
```
5.2
6.987e54
-6.987E54
-6.987e+54
-6.987E-54
```
string
```
"abc"
"String\" 中文 \\ / \b \f \n \r \t \u1337 "
```

word
```
abc
__F_u_N_c_1__
```

symbol
```
$length
```

map
```
{
    "m": 1,
    n: 2
}
```

array
```
[1 2 3]
```

list
```
( 1 2 3 )
```


function call
```
(+ (- (* 3.1 2) 0.3) (/ 6 3))               => 7.9
```

subscript:
use number values as array index
use string (eg. "m") or symbol (eg. $t) values as map keys
```
(@ [[{m: [2 {t:2}], n:2}]] 0 0 "m" 1 $t)    => 2
```

method call
```
(. {m:2} (put "a" 1) (keys))            => ["m", "a"]
```

function value in a map is treated as a method
```
(. { add2: (func [obj x y] (+ x y)) }
    (add2 1 2)
)
```

quote as data
```
(% (/ 6 3)) => (% (/ 6 3))
```
