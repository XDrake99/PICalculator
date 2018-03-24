/*
SETTINGS: (please don't move this part)
 PATH=__INSERT_PACKAGE_WITH_CLASS_NAME__
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.warp.picalculator.ScriptUtils;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;
import org.warp.picalculator.math.rules.RulesManager;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.functions.Number;

/**
 * Expand rule
 * -(-a) = a
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "ExpandRule2";
	}

	// Rule type
	@Override
	public RuleType getRuleType() {
		return RuleType.EXPANSION;
	}

	/* Rule function
	   Returns:
	     - null if it's not executable on the function "f"
	     - An ObjectArrayList<Function> if it did something
	*/
	@Override
	public ObjectArrayList<Function> execute(Function f) {
		boolean isExecutable = false;
		if (f instanceof Multiplication) {
			Function fnc = f;
			if (fnc.getParameter1().equals(new Number(fnc.getMathContext(), -1))) {
				var expr = fnc.getParameter2();
				if (expr instanceof Sum) {
					isExecutable = true;
				} else if (expr instanceof Subtraction) {
					isExecutable = true;
				} else if (expr instanceof SumSubtraction) {
					isExecutable = true;
				}
			}
		} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
			Function fnc = f;
			var expr = fnc.getParameter2();
			if (expr instanceof Sum) {
				isExecutable = true;
			} else if (expr instanceof Subtraction) {
				isExecutable = true;
			} else if (expr instanceof SumSubtraction) {
				isExecutable = true;
			}
		}
		if (isExecutable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			var root = f.getMathContext();

			var expr = null;
			var fromSubtraction = 0;
			var subtraction = null;
			if (f instanceof Multiplication) {
				expr = f.getParameter2();
			} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
				expr = f.getParameter2();
				if (f instanceof Subtraction) {
					fromSubtraction = 1;
				} else {
					fromSubtraction = 2;
				}
			}

			if (f instanceof SumSubtraction) {
				
			}

			Function fnc = expr;
			if (fnc instanceof Sum) {
				var a = fnc.getParameter1();
				var b = fnc.getParameter2();
				var fnc2 = new Subtraction(root, new Multiplication(root, new Number(root, -1), a), b);
				if (fromSubtraction > 0) {
					subtraction = new Subtraction(root, f.getParameter1(), fnc2);
					result.add(subtraction);
				} else {
					result.add(fnc2);
				}
			} else if (fnc instanceof Subtraction) {
				var a = fnc.getParameter1();
				var b = fnc.getParameter2();
				var fnc2 = new Sum(root, new Multiplication(root, new Number(root, -1), a), b);
				if (fromSubtraction > 0) {
					subtraction = new Subtraction(root, f.getParameter1(), fnc2);
					result.add(subtraction);
				} else {
					result.add(fnc2);
				}
			} else if (fnc instanceof SumSubtraction) {
				var a = fnc.getParameter1();
				var b = fnc.getParameter2();
				var fnc2 = new Sum(root, new Multiplication(root, new Number(root, -1), a), b);
				var fnc3 = new Subtraction(root, new Multiplication(root, new Number(root, -1), a), b);
				if (fromSubtraction > 0) {
					subtraction = new SumSubtraction(root, f.getParameter1(), fnc2);
					result.add(subtraction);
					subtraction = new SumSubtraction(root, f.getParameter1(), fnc3);
					result.add(subtraction);
					result.add(subtraction);
				} else {
					result.add(fnc2);
					result.add(fnc2);
				}
			}
			return result;
		} else {
			return null;
		}
		
	}
}
