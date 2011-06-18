package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.interfaces.AbstractTrigArg1;
import org.matheclipse.core.expression.ComplexNum;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.Num;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.SyntaxError;

import apache.harmony.math.BigInteger;

public class Factorial2 extends AbstractTrigArg1 {

	public Factorial2() {
	}

	@Override
	public IExpr numericEvalD1(final Num arg1) {
		return null;
	}

	@Override
	public IExpr numericEvalDC1(final ComplexNum arg1) {
		return null;
	}

	public IInteger factorial(final IInteger iArg) {
		BigInteger result = BigInteger.ONE;
		final BigInteger biggi = iArg.getBigNumerator();
		BigInteger start;
		if (biggi.compareTo(BigInteger.ZERO) == -1) {
			result = BigInteger.valueOf(-1);
			if (biggi.isOdd()) {
				start = BigInteger.valueOf(-3);
			} else {
				start = BigInteger.valueOf(-2);
			}
			for (BigInteger i = start; i.compareTo(biggi) >= 0; i = i.plus(BigInteger.valueOf(-2))) {
				result = result.times(i);
			}
		} else {
			if (biggi.isOdd()) {
				start = BigInteger.valueOf(3);
			} else {
				start = BigInteger.valueOf(2);
			}
			for (BigInteger i = start; i.compareTo(biggi) <= 0; i = i.plus(BigInteger.valueOf(2))) {
				result = result.times(i);
			}
		}

		final IInteger i = F.integer(result);

		return i;
	}

	@Override
	public IExpr evaluateArg1(final IExpr arg1) {
		if (arg1 instanceof IInteger) {
			return factorial((IInteger) arg1);
		}
		return null;
	}

	@Override
	public void setUp(final ISymbol symbol) throws SyntaxError {
		symbol.setAttributes(ISymbol.LISTABLE | ISymbol.NUMERICFUNCTION);
		super.setUp(symbol);
	}
}