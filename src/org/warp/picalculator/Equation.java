package org.warp.picalculator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nevec.rjm.NumeroAvanzatoVec;

import com.rits.cloning.Cloner;

public class Equation extends FunctionTwoValues {

	public Equation(Function value1, Function value2) {
		super(value1,value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.EQUATION;
	}
	
	@Override
	public List<Function> solveOneStep() throws Error {
		if (variable1 == null || variable2 == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ArrayList<Function> result = new ArrayList<>();
		if (stepsCount == 1) {
			if (((Number)variable2).getTerm().isBigInteger(false) && ((Number)variable2).getTerm().toBigInteger(false).compareTo(new BigInteger("0")) == 0) {
				result.add(this);
			} else {
				result.add(new Equation(new Subtraction((FunctionBase)variable1, (FunctionBase)variable2), new Number("0")));
			}
		} else {
			List<Function> l1 = new ArrayList<Function>();
			List<Function> l2 = new ArrayList<Function>();
			if (variable1.getStepsCount() >= stepsCount - 1) {
				l1.addAll(variable1.solveOneStep());
			} else {
				l1.add(variable1);
			}
			if (variable2.getStepsCount() >= stepsCount - 1) {
				l2.addAll(variable2.solveOneStep());
			} else {
				l2.add(variable2);
			}
			
			int size1 = l1.size();
			int size2 = l2.size();
			int cur1 = 0;
			int cur2 = 0;
			int total = l1.size()*l2.size();
			Function[][] results = new Function[total][2];
			for (int i = 0; i < total; i++) {
				results[i] = new Function[]{l1.get(cur1), l2.get(cur2)};
				if (cur1 < cur2 && cur2 % size1 == 0) {
					cur2+=1;
				} else if (cur2 < cur1 && cur1 % size2 == 0) {
					cur1+=1;
				}
				if (cur1 >= size1) cur1 = 0;
				if (cur2 >= size1) cur2 = 0;
			}
			for (Function[] f : results) {
				result.add(new Equation((FunctionBase)f[0], (FunctionBase)f[1]));
			}
			stepsCount=-1;
		}
		return result;
	}

	private int stepsCount = -1;
	@Override
	public int getStepsCount() {
		if (stepsCount == -1) {
			int val1 = variable1.getStepsCount();
			int val2 = variable2.getStepsCount();
			if (val1 > val2) {
				stepsCount = val1+1;
			} else {
				stepsCount = val2+1;
			}
		}
		return stepsCount;
	}
	
	public List<Function> solve(char variableCharacter) {
		@SuppressWarnings("unused")
		ArrayList<Equation> e;
		//TODO: WORK IN PROGRESS.
		//TODO: Finire. Fare in modo che risolva i passaggi fino a che non ce ne sono più
		return null;
	}
	
	//WORK IN PROGRESS
	public ArrayList<Equation> solveStep(char charIncognita) {
		ArrayList<Equation> result = new ArrayList<Equation>();
		result.add(this.clone());
		for (SolveMethod t : SolveMethod.techniques) {
			ArrayList<Equation> newResults = new ArrayList<Equation>();
			final int sz = result.size();
			for (int n = 0; n < sz; n++) {
				newResults.addAll(t.solve(result.get(n)));
			}
			Set<Equation> hs = new HashSet<>();
			hs.addAll(newResults);
			newResults.clear();
			newResults.addAll(hs);
			result = newResults;
		}
		// TODO: controllare se è a posto
		return result;
	}

	@Override
	public Equation clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}

}