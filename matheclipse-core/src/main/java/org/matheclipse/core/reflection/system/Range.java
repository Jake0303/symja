package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.List;

import java.util.ArrayList;
import java.util.List;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.eval.util.Iterator;
import org.matheclipse.core.expression.AST;
import org.matheclipse.core.generic.UnaryRangeFunction;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.generic.nested.TableGenerator;

public class Range implements IFunctionEvaluator {

	public Range() {
	}

	public IExpr evaluate(final IAST ast) {
		return evaluateTable(ast, List());
	}

	public IExpr evaluateTable(final IAST ast, final IAST resultList) {
		try {
			if ((ast.size() > 1) && (ast.size() <= 4)) {
				final EvalEngine engine = EvalEngine.get();
				final List<Iterator> iterList = new ArrayList<Iterator>();
				iterList.add(new Iterator(ast, null, engine));

				final TableGenerator<IExpr, IAST> generator = new TableGenerator<IExpr, IAST>(iterList, resultList,
						new UnaryRangeFunction(), AST.COPY);
				return (IExpr) generator.table();
			}
		} catch (final ClassCastException e) {
			// the iterators are generated only from IASTs
		}
		return null;
	}

	public IExpr numericEval(final IAST functionList) {
		return evaluate(functionList);
	}

	public void setUp(final ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}
}
