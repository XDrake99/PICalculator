/*
SETTINGS: (please don't move this part)
 PATH=__INSERT_PACKAGE_WITH_CLASS_NAME__
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
//Imports


import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule
 * a * 1 = a
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "NumberRule2";
	}

	// Rule type
	@Override
	public RuleType getRuleType() {
		return RuleType.CALCULATION;
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
			var root = f.getMathContext();
			var mult = f;
			if (mult.getParameter1() instanceof Number) {
				var numb = mult.getParameter1();
				if (numb.equals(new Number(root, 1))) {
					isExecutable = true;
				}
			}
			if (mult.getParameter2() instanceof Number) {
				var numb = mult.getParameter2();
				if (numb.equals(new Number(root, 1))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			var a = null;
			var aFound = false;
			var mult = f;
			if (aFound == false & mult.getParameter1() instanceof Number) {
				var numb = mult.getParameter1();
				if (numb.equals(new Number(root, 1))) {
					a = mult.getParameter2();
					aFound = true;
				}
			}
			if (aFound == false && mult.getParameter2() instanceof Number) {
				var numb = mult.getParameter2();
				if (numb.equals(new Number(root, 1))) {
					a = mult.getParameter1();
					aFound = true;
				}
			}
	
			result.add(a);
			return result;
		} else {
			return null;
		}
	}
}
