# PipeFunctionChaining
Implementation of pipe-based function chaining syntax

## Background 
Please see this thread: https://scsynth.org/t/function-chaining-operators-opinions-thoughts-do-you-like-this-a-big-hopefully-discursive-post/6627
The following is an abridged version of the above link.

## What it is
Two operators: `|>` and `<|`

## Forward chaining: `|>` operator
A way of chaining function calls together. Here, each _ becomes the result of the previous expression.

### With UGens
```SuperCollider
SinOsc.ar(440) |> LPF.ar(_, \lpf.kr(15000)) |> HPF.ar(_, \hpf.kr(20)) 
|> Out.ar(\out.kr(0), _);
```
A sine wave that is filtered by a low pass, then high pass, then outputted.

### With functions
```SuperCollider
~f = {|i|  "f".postln; i + 1; };
~g = {|i|  "g".postln; i * 2; };
~h = {|i|  "h".postln; i - 2; };

2 |> ~g |> ~f |> ~h;

// returns : g f h -> 3
```

## Backward chaining: `<|` operator
Executed from the right to the left.

### With functions
```SuperCollider
~h <| ~f <| ~g <| 2
// returns : g f h -> 3
```

### With UGens
```SuperCollider
Out.ar(\out.kr(0), _) <| HPF.ar(_, \hpf.kr(20)) <| LPF.ar(_, \lpf.kr(15000)) <| SinOsc.ar(440)
```

## Gotchas
It can get weird - the following is valid.
```SuperCollider
~f = {|i|  "f".postln; i + 1; };
~g = {|i|  "g".postln; i * 2; };
~h = {|i|  "h".postln; i - 2; };

~f <| ( ~g <| 1 |> ~h |> ~g ) |> ~h
// These are equivalent
(1 |> ~g |> ~h |> ~g |> ~f |> ~h)
```

### Brackets
For forward chaining, you need to add brackets around each function
```SuperCollider
1 |> (_ + 1) |> (_ * 2); // works
1 |> _ + 1 |> _ * 2; // does not
```

It gets weirder with backwards…
```SuperCollider
_ * 2 <|  _ + 1 <| 1 // this will evaluate, but returns the wrong result.
(_ * 2) <| ( _ + 1) <| 1 // correct
```
However this mostly goes away if you call messages with a . on the underscore.

### Multi-arity functions are messy.
```SuperCollider
-1 |> (_.neg) |> (_ * 3.5) |> { |r| 
	2 |> (_.sin) |> { |two|
		4 |> _.pow(two) |> _.round(r)
	}
}
```
