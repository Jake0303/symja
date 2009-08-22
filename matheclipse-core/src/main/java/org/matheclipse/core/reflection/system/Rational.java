package org.matheclipse.core.reflection.system;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;

public class Rational extends AbstractFunctionEvaluator {
	public final static Rational CONST = new Rational();

	public Rational() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if ((ast.size() == 3)) {
			try {
				final EvalEngine engine = EvalEngine.get();
				IExpr arg0 = ast.get(1);
				arg0 = engine.evaluate(arg0);
				IExpr arg1 = ast.get(2);
				arg1 = engine.evaluate(arg1);
				if ((arg0 instanceof IInteger) && (arg1 instanceof IInteger)) {
					return F.fraction((IInteger) arg0, (IInteger) arg1);
				}
			} catch (Exception e) {
				if (Config.SHOW_STACKTRACE) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
