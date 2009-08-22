package org.matheclipse.core.reflection.system;

import java.util.HashMap;
import java.util.HashSet;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.WrongNumberOfArguments;
import org.matheclipse.core.eval.interfaces.IFunctionEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IStringX;
import org.matheclipse.core.interfaces.ISymbol;

/**
 * Package[{&lt;list of public package rule headers&gt;}, {&lt;list of rules in
 * this package&gt;}}
 * 
 */
public class Package implements IFunctionEvaluator {

	public Package() {
	}

	public IExpr evaluate(final IAST ast) {
		if (ast.size() != 4 || !(ast.get(1) instanceof IStringX) || !ast.get(2).isList() || !ast.get(3).isList()) {
			throw new WrongNumberOfArguments(ast, 1, ast.size() - 1);
		}
		IAST symbols = (IAST) ast.get(2);
		HashMap<ISymbol, ISymbol> convertedSymbolMap = new HashMap<ISymbol, ISymbol>();
		HashSet<ISymbol> unprotectedSymbolSet = new HashSet<ISymbol>();

		ISymbol toSymbol;
		for (int i = 1; i < symbols.size(); i++) {
			IExpr expr = symbols.get(i);
			if (expr instanceof ISymbol) {
				unprotectedSymbolSet.add((ISymbol) expr);
				toSymbol = F.predefinedSymbol(((ISymbol) expr).toString());
				convertedSymbolMap.put((ISymbol) expr, toSymbol);
			}
		}
		IAST list = (IAST) ast.get(3);
		// determine "private package rule headers" in convertedSymbolMap
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i) instanceof IAST) {
				determineRuleHead((IAST) list.get(i), unprotectedSymbolSet, convertedSymbolMap);
			}
		}

		// convert the rules into a new list:
		IAST resultList = F.List();
		for (int i = 1; i < list.size(); i++) {
			resultList.add(convertSymbolsInExpr(list.get(i), convertedSymbolMap));
		}
		EvalEngine engine = EvalEngine.get();
		try {
			engine.setPackageMode(true);
			// evaluate the new converted rules
			for (int i = 1; i < resultList.size(); i++) {
				EvalEngine.eval(resultList.get(i));
			}
		} finally {
			engine.setPackageMode(false);
		}
		// System.out.println(resultList);
		return F.Null;
	}

	/**
	 * Determine the head symbol of the given rule
	 * 
	 * @param rule
	 * @param unprotectedSymbolSet
	 * @param convertedSymbolMap
	 */
	private void determineRuleHead(IAST rule, HashSet<ISymbol> unprotectedSymbolSet, HashMap<ISymbol, ISymbol> convertedSymbolMap) {
		ISymbol lhsHead;
		if (rule.size() > 1 && (rule.getHeader().equals(F.Set) || rule.getHeader().equals(F.SetDelayed))) {
			// determine the head to which this rule is associated
			lhsHead = null;
			if (rule.get(1) instanceof IAST) {
				lhsHead = ((IAST) rule.get(1)).topHead();
			} else if (rule.get(1) instanceof ISymbol) {
				lhsHead = (ISymbol) rule.get(1);
			}

			if (lhsHead != null && !unprotectedSymbolSet.contains(lhsHead)) {
				ISymbol toSymbol = convertedSymbolMap.get(lhsHead);
				if (toSymbol == null) {
					toSymbol = F.predefinedSymbol("@" + EvalEngine.getNextCounter() + lhsHead.toString());
					convertedSymbolMap.put(lhsHead, toSymbol);
				}
			}

		}
	}

	/**
	 * Convert all symbols which are keys in <code>convertedSymbols</code> in
	 * the given <code>expr</code> and return the resulting expression.
	 * 
	 * @param expr
	 * @param convertedSymbols
	 * @return
	 */
	private IExpr convertSymbolsInExpr(IExpr expr, HashMap<ISymbol, ISymbol> convertedSymbols) {
		IExpr result = expr;
		if (expr instanceof IAST) {
			result = convertSymbolsInList((IAST) expr, convertedSymbols);
		} else if (expr instanceof ISymbol) {
			ISymbol toSymbol = convertedSymbols.get((ISymbol) expr);
			if (toSymbol != null) {
				result = toSymbol;
			}
		}

		return result;
	}

	/**
	 * Convert all symbols which are keys in <code>convertedSymbols</code> in
	 * the given <code>ast</code> list and return the resulting list.
	 * 
	 * @param ast
	 * @param convertedSymbols
	 * @return
	 */
	private IAST convertSymbolsInList(IAST ast, HashMap<ISymbol, ISymbol> convertedSymbols) {
		IAST result = (IAST) ast.clone();
		for (int i = 0; i < result.size(); i++) {
			IExpr expr = result.get(i);
			if (expr instanceof IAST) {
				result.set(i, convertSymbolsInList((IAST) expr, convertedSymbols));
			} else if (expr instanceof ISymbol) {
				ISymbol toSymbol = convertedSymbols.get((ISymbol) expr);
				if (toSymbol != null) {
					result.set(i, toSymbol);
				}
			}
		}

		return result;
	}

	public IExpr numericEval(IAST functionList) {
		return null;
	}

	public void setUp(ISymbol symbol) {
		symbol.setAttributes(ISymbol.HOLDALL);
	}

}
