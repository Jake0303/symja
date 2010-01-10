package org.matheclipse.core.reflection.system;

import java.io.IOException;
import java.io.PrintStream;

import org.matheclipse.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

public class Definition extends AbstractFunctionEvaluator {

	public Definition() {
	}

	@Override
	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 2 && ast.get(1) instanceof ISymbol) {
			return null;
		}
		final EvalEngine engine = EvalEngine.get();
		PrintStream stream;
		stream = engine.getOutPrintStream(); 
		if (stream == null) {
			stream = System.out;
		}
		try {
			ISymbol symbol = (ISymbol) ast.get(1);
			stream.println(symbol.definitionToString());
		} catch (IOException e) {
			stream.println(e.getMessage());
			if (Config.DEBUG) {
				e.printStackTrace();
			}
		}

		return F.Null;
	}

}