package it.cavallium.warppi.math.rules.dsl.patterns;

import it.cavallium.warppi.math.Function;
import it.cavallium.warppi.math.MathContext;
import it.cavallium.warppi.math.functions.Undefined;
import it.cavallium.warppi.math.rules.dsl.VisitorPattern;

import java.util.*;

/**
 * Matches and generates <code>Undefined</code>.
 */
public class UndefinedPattern extends VisitorPattern {
	@Override
	public Optional<Map<String, Function>> visit(Undefined undefined) {
		return Optional.of(Collections.emptyMap());
	}

	@Override
	public Function replace(MathContext mathContext, Map<String, Function> subFunctions) {
		return new Undefined(mathContext);
	}

	@Override
	public Set<SubFunctionPattern> getSubFunctions() {
		return Collections.emptySet();
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof UndefinedPattern;
	}

	@Override
	public int hashCode() {
		return UndefinedPattern.class.hashCode();
	}
}