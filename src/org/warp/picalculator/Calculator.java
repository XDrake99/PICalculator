package org.warp.picalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nevec.rjm.NumeroAvanzato;
import org.warp.device.PIDisplay;
import org.warp.engine.Screen;
import org.warp.picalculator.screens.EquationScreen;
import org.warp.picalculator.screens.SolveEquationScreen;

public class Calculator {

	public static String angleMode = "deg";
	public static Screen[] sessions = new Screen[5];
	public static int currentSession = 0;
	public static boolean haxMode = true;

	public static Function parseString(String string) throws Error {
		if (string.contains("{")) {
			if (!string.startsWith("{")) {
				throw new Error(Errors.SYNTAX_ERROR);
			}
			String[] parts = string.substring(1).split("\\{");
			EquationsSystem s = new EquationsSystem();
			for (String part : parts) {
				s.addVariableToEnd(parseEquationString(part));
			}
			return s;
		} else if (string.contains("=")) {
			return parseEquationString(string);
		} else {
			return new Expression(string);
		}
	}
	
	public static Function parseEquationString(String string) throws Error {
		String[] parts = string.split("=");
		if (parts.length == 1) {
			return new Equation(new Expression(parts[0]), new Number(NumeroAvanzato.ZERO));
		} else if (parts.length == 2) {
			return new Equation(new Expression(parts[0]), new Expression(parts[1]));
		} else {
			throw new Error(Errors.SYNTAX_ERROR);
		}
	}

	public static void solve() throws Error {
		if (Calculator.currentSession == 0 && Calculator.sessions[0] instanceof EquationScreen) {
			EquationScreen es = (EquationScreen) Calculator.sessions[0];
			Function f = es.f;
			if (f instanceof Equation) {
				PIDisplay.INSTANCE.setScreen(new SolveEquationScreen(es));
			} else {
				List<Function> results = new ArrayList<>();
				List<Function> partialResults = new ArrayList<>();
				results.add(es.f);
				while (Utils.allSolved(results) == false) {
					for (Function itm : results) {
						if (itm.getStepsCount() > 0) {
							partialResults.addAll(itm.solveOneStep());
						} else {
							partialResults.add(itm);
						}
					}
					results = new ArrayList<Function>(partialResults);
					partialResults.clear();
				}
				if (results.size() == 0) {
					
				} else {
					Collections.reverse(results);
					es.f2 = results;
					for (Function rf : es.f2) {
						rf.generateGraphics();
					}
				}
			}
		}
	}

	public static void solve(char letter) throws Error {
		if (Calculator.currentSession == 0 && Calculator.sessions[0] instanceof EquationScreen) {
			EquationScreen es = (EquationScreen) Calculator.sessions[0];
			Function f = es.f;
			if (f instanceof Equation) {
				List<Function> results = ((Equation)f).solve(letter);
				Collections.reverse(results);
				es.f2 = results;
				for (Function rf : es.f2) {
					rf.generateGraphics();
				}
			}
		}
	}

	public static void simplify() {
		
	}

}
