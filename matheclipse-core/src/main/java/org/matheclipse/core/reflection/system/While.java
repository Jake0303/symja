package org.matheclipse.core.reflection.system;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.BreakException;
import org.matheclipse.core.eval.exception.ContinueException;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class While implements IFunctionEvaluator {

	public While() {
		super();
	}

	public IExpr evaluate(final IAST ast) {
		final EvalEngine engine = EvalEngine.get();
		if (ast.size() == 3) {
			while (engine.evaluate(ast.get(1)).equals(F.True)) {
				try {
					engine.evaluate(ast.get(2));
				} catch (final BreakException e) {
					return F.Null;
				} catch (final ContinueException e) {
					continue;
				}
			}
		}
		return F.Null;
	}

	public IExpr numericEval(final IAST ast) {
		return evaluate(ast);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

}
