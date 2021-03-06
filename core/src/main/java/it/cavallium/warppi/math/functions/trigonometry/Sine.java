package it.cavallium.warppi.math.functions.trigonometry;

import it.cavallium.warppi.gui.expression.blocks.Block;
import it.cavallium.warppi.gui.expression.blocks.BlockContainer;
import it.cavallium.warppi.gui.expression.blocks.BlockSine;
import it.cavallium.warppi.math.Function;
import it.cavallium.warppi.math.FunctionSingle;
import it.cavallium.warppi.math.MathContext;
import it.cavallium.warppi.util.Error;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Sine extends FunctionSingle {

	public Sine(final MathContext root, final Function value) {
		super(root, value);
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof Sine) {
			final FunctionSingle f = (FunctionSingle) o;
			return parameter.equals(f.getParameter());
		}
		return false;
	}

	@Override
	public Sine clone() {
		return new Sine(mathContext, parameter == null ? null : parameter.clone());
	}

	@Override
	public Sine clone(MathContext c) {
		return new Sine(c, parameter == null ? null : parameter.clone(c));
	}

	@Override
	public ObjectArrayList<Block> toBlock(final MathContext context) throws Error {
		final ObjectArrayList<Block> result = new ObjectArrayList<>();
		final ObjectArrayList<Block> sub = getParameter(0).toBlock(context);
		final BlockSine bs = new BlockSine();
		final BlockContainer bpc = bs.getNumberContainer();
		for (final Block b : sub) {
			bpc.appendBlockUnsafe(b);
		}
		bpc.recomputeDimensions();
		bs.recomputeDimensions();
		result.add(bs);
		return result;
	}

	@Override
	public <Argument, Result> Result accept(final Function.Visitor<Argument, Result> visitor, final Argument argument) {
		return visitor.visit(this, argument);
	}
}
