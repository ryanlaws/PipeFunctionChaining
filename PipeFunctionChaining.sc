// Implementation of pipe-based function chaining syntax
// Provided by user @jordan on scsynth.org
// https://scsynth.org/t/function-chaining-operators-opinions-thoughts-do-you-like-this-a-big-hopefully-discursive-post/6627
+Object {
	|> { |f| ^f.(this) }
	<| { |f|
		^if(f.isKindOf(Function),
			{ {|i| this.( f.(i) )} },
			{ this.(f) })
	}
}