package org.warp.picalculator.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.functions.equations.Equation;
import org.warp.picalculator.math.functions.equations.EquationsSystem;
import org.warp.picalculator.screens.MathInputScreen;
import org.warp.picalculator.screens.SolveEquationScreen;

public class Calculator {

	public AngleMode angleMode = AngleMode.DEG;
	public boolean exactMode = false;
	
	public Calculator() {
	}

	public Function parseString(String string) throws Error {
		if (string.contains("{")) {
			if (!string.startsWith("{")) {
				throw new Error(Errors.SYNTAX_ERROR);
			}
			String[] parts = string.substring(1).split("\\{");
			EquationsSystem s = new EquationsSystem(null);
			for (String part : parts) {
				s.addFunctionToEnd(parseEquationString(part));
			}
			return s;
		} else if (string.contains("=")) {
			return parseEquationString(string);
		} else {
			return new Expression(null, string);
		}
	}
	
	public Function parseEquationString(String string) throws Error {
		String[] parts = string.split("=");
		if (parts.length == 1) {
			Equation e = new Equation(null, null, null);
			e.setVariable1(new Expression(e, parts[0]));
			e.setVariable2(new Number(e, BigInteger.ZERO));
			return e;
		} else if (parts.length == 2) {
			Equation e = new Equation(null, null, null);
			e.setVariable1(new Expression(e, parts[0]));
			e.setVariable2(new Expression(e, parts[1]));
			return e;
		} else {
			throw new Error(Errors.SYNTAX_ERROR);
		}
	}

	public ArrayList<Function> solveExpression(ArrayList<Function> input) throws Error {
		ArrayList<Function> results = new ArrayList<>();
		ArrayList<Function> partialResults = new ArrayList<>();
		for (Function f : input) {
			if (f instanceof Equation) {
				throw new IllegalArgumentException("Not an expression!");
			} else {
				results.add(f);
				while (Utils.allSolved(results) == false) {
					for (Function itm : results) {
						if (itm.isSolved() == false) {
							long t1 = System.currentTimeMillis();
							List<Function> dt = itm.solveOneStep();
							long t2 = System.currentTimeMillis();
							if (t2-t1 >= 3000) {
								throw new Error(Errors.TIMEOUT);
							}
							partialResults.addAll(dt);
						} else {
							partialResults.add(itm);
						}
					}
					results = new ArrayList<Function>(partialResults);
					partialResults.clear();
				}
			}
		}
		return results;
	}
	
	/*public void solve(EquationScreen equationScreen, char letter) throws Error {
		if (Calculator.currentSession == 0 && Calculator.sessions[0] instanceof EquationScreen) {
			EquationScreen es = (EquationScreen) Calculator.sessions[0];
			ArrayList<Function> f = es.f;
			if (f instanceof Equation) {
				List<Function> results = ((Equation)f).solve(letter);
				Collections.reverse(results);
				// add elements to al, including duplicates
				Set<Function> hs = new LinkedHashSet<>();
				hs.addAll(results);
				results.clear();
				results.addAll(hs);
				es.f2 = results;
				for (Function rf : es.f2) {
					rf.generateGraphics();
				}
			}
		}
	}*/

}