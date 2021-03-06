package it.cavallium.warppi.math;

import it.cavallium.warppi.math.rules.Rule;
import it.cavallium.warppi.util.Error;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Objects;

public abstract class FunctionSingle implements Function {

	private boolean simplified;
	
	/**
	 * Create a new instance of FunctionSingle. The Math Context will be the
	 * same of <strong>value</strong>'s.
	 *
	 * @throws NullPointerException
	 *             when value is null.
	 * @param value
	 *            The parameter of this function.
	 */
	public FunctionSingle(final Function value) throws NullPointerException {
		mathContext = value.getMathContext();
		parameter = value;
	}

	/**
	 * Create a new instance of FunctionSingle.
	 *
	 * @param mathContext
	 *            Math Context
	 */
	public FunctionSingle(final MathContext mathContext) {
		this.mathContext = mathContext;
		parameter = null;
	}

	/**
	 * Create a new instance of FunctionSingle.
	 *
	 * @param mathContext
	 *            Math Context
	 * @param value
	 *            The parameter of this function.
	 */
	public FunctionSingle(final MathContext mathContext, final Function value) {
		this.mathContext = mathContext;
		parameter = value;
	}

	protected final MathContext mathContext;

	/**
	 * Function parameter.<br>
	 * <u>MUST NOT BE MODIFIED IF ALREADY SET UP.</u>
	 */
	protected Function parameter;

	/**
	 *
	 * @return Parameter.
	 */
	public Function getParameter() {
		return parameter;
	}

	/**
	 *
	 * @param value
	 *            Parameter.
	 * @return A new instance of this function.
	 */
	public FunctionSingle setParameter(final Function value) {
		final FunctionSingle s = clone();
		s.parameter = value;
		return s;
	}

	@Override
	public FunctionSingle setParameter(final int index, final Function var) throws IndexOutOfBoundsException {
		if (index == 0) {
			return this.setParameter(var);
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public Function getParameter(final int index) throws IndexOutOfBoundsException {
		if (index == 0) {
			return this.getParameter();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public MathContext getMathContext() {
		return mathContext;
	}

	@Override
	public final ObjectArrayList<Function> simplify(final Rule rule) throws Error, InterruptedException {
		final ObjectArrayList<Function> simplifiedParam = parameter.simplify(rule);
		if (simplifiedParam == null) {
			return rule.execute(this);
		}

		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		for (final Function f : simplifiedParam) {
			result.add(this.setParameter(f));
		}

		return result;
	}

	@Override
	public abstract FunctionSingle clone();

	@Override
	public int hashCode() {
		return Objects.hash(parameter);
	}

	@Override
	public abstract boolean equals(Object o);

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + getParameter() + ")";
	}
}
