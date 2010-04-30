package org.matheclipse.core.visit;

import static org.matheclipse.core.expression.F.C1;
import static org.matheclipse.core.expression.F.C2;
import static org.matheclipse.core.expression.F.Cos;
import static org.matheclipse.core.expression.F.Power;
import static org.matheclipse.core.expression.F.Sin;
import static org.matheclipse.core.expression.F.pattern;
import junit.framework.TestCase;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Tests visitors
 */
public class VisitorTestCase extends TestCase {

	public VisitorTestCase(String name) {
		super(name);
		F.initSymbols(null, null);
	}

	/**
	 */
	public void testHashValueVisitor() {
		HashValueVisitor v = new HashValueVisitor(1);
		IExpr expr = F.Power(F.Sin(F.Log(F.C1)), F.C2);
		int hash = expr.accept(v);
		assertEquals(hash, -1895901688);
		
		v.setUp();
		expr = F.Power(F.Sin(F.Cos(F.C3)), F.C2);
		hash = expr.accept(v);
		assertEquals(hash, -1895901688);
		
		v.setUp();
		expr = F.Power(F.Sin(F.pattern("x")), F.C2);
		hash = expr.accept(v);
		assertEquals(hash, -1895901688);
		
		v.setUp();
		expr = F.Power(F.Cos(F.Sin(F.C3)), F.C2);
		hash = expr.accept(v);
		assertEquals(hash, -1896372423);
	}
}
