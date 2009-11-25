package org.matheclipse.core.reflection.system;

import java.util.List;
import java.util.SortedMap;

import org.matheclipse.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.eval.util.Options;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;

import apache.harmony.math.BigInteger;

import edu.jas.arith.BigRational;
import edu.jas.arith.ModInteger;
import edu.jas.arith.ModIntegerRing;
import edu.jas.poly.GenPolynomial;
import edu.jas.ufd.FactorAbstract;
import edu.jas.ufd.FactorFactory;

public class Factor extends AbstractFunctionEvaluator {

	public Factor() {
	}

	@Override
	public IExpr evaluate(final IAST lst) {
		if (lst.size() != 2 && lst.size() != 3) {
			return null;
		}

		ExprVariables eVar = new ExprVariables(lst.get(1));
    if (!eVar.isSize(1)) {
      // factor only possible for univariate polynomials
      return null;
    }
		try {
			IExpr expr = F.eval(F.ExpandAll, lst.get(1));
			ASTRange r = new ASTRange(eVar.getVarList(), 1);
			List<IExpr> varList = r.toList();
			
			if (lst.size() == 3) {
				final EvalEngine engine = EvalEngine.get();
				final Options options = new Options(F.Factor, engine, lst, 2);
				IExpr option = options.getOption("Modulus");
				if (option != null && option instanceof IInteger) {
					try {
						// found "Modulus" option
						final BigInteger value = ((IInteger) option).getBigNumerator();
						int intValue = ((IInteger) option).toInt();
						ModIntegerRing modIntegerRing = new ModIntegerRing(intValue, value.isProbablePrime(32));
						JASConvert<ModInteger> jas =  new JASConvert<ModInteger>(varList, modIntegerRing);
						GenPolynomial<ModInteger> poly = jas.expr2Poly(expr);

						FactorAbstract<ModInteger> factorAbstract = FactorFactory.getImplementation(modIntegerRing);
						SortedMap<GenPolynomial<ModInteger>, Long> map = factorAbstract.baseFactors(poly);
						IAST result = F.Times();
						for (SortedMap.Entry<GenPolynomial<ModInteger>, Long> entry : map.entrySet()) {
							GenPolynomial<ModInteger> iPoly = entry.getKey();
							Long val = entry.getValue();
							result.add(F.Power(jas.modIntegerPoly2Expr(iPoly), F.integer(val)));
						}
						return result;
					} catch (ArithmeticException ae) {
						// toInt() conversion failed
						if (Config.DEBUG) {
							ae.printStackTrace();
						}
						return null; // no evaluation
					}
				}
			}
			JASConvert<BigRational> jas = new JASConvert<BigRational>(varList);
			GenPolynomial<BigRational> poly = jas.expr2Poly(expr);

			FactorAbstract<BigRational> factorAbstract = FactorFactory.getImplementation(BigRational.ONE);
			SortedMap<GenPolynomial<BigRational>, Long> map = factorAbstract.baseFactors(poly);
			IAST result = F.Times();
			for (SortedMap.Entry<GenPolynomial<BigRational>, Long> entry : map.entrySet()) {
				GenPolynomial<BigRational> key = entry.getKey();
				GenPolynomial<edu.jas.arith.BigInteger> iPoly = (GenPolynomial<edu.jas.arith.BigInteger>) jas.factorTerms(key)[2];
				Long val = entry.getValue();
				result.add(F.Power(jas.integerPoly2Expr(iPoly), F.integer(val)));
			}
			return result;
			
		} catch (Exception e) {
			if (Config.DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}

}