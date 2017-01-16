package org.warp.picalculator;

import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawLine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nevec.rjm.BigDecimalMath;
import org.nevec.rjm.Rational;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.device.graphicengine.RAWFont;
import org.warp.picalculator.math.functions.AnteriorFunction;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.functions.equations.Equation;
import org.warp.picalculator.math.functions.equations.EquationsSystemPart;

import com.rits.cloning.Cloner;

public class Utils {

	public static final int scale = 24;
	public static final int displayScale = 8;

	public static final int scaleMode = BigDecimal.ROUND_HALF_UP;
	public static final RoundingMode scaleMode2 = RoundingMode.HALF_UP;

	public static DebugStream debug = new DebugStream();

	public static boolean debugOn;
	public static boolean debugThirdScreen;

	public static Cloner cloner = new Cloner();
	
	public static final class DebugStream extends StringWriter {

		public void println(String str) {
			if (debugOn) {
				System.err.println(str);
			}
		}

		int before = 0;
		boolean due = false;

	}

	public static boolean isInArray(String ch, String[] a) {
		boolean contains = false;
		for (String c : a) {
			if (c.equals(ch)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	private static final String[] regexNormalSymbols = new String[]{"\\", ".", "[", "]", "{", "}", "(", ")", "*", "+", "-", "?", "^", "$", "|"};
	
	public static String ArrayToRegex(String[] array) {
		String regex = null;
		for (String symbol : array) {
			boolean contained = false;
			for (String smb : regexNormalSymbols) {
				if (smb.equals(symbol)) {
					contained = true;
					break;
				}
			}
			if (contained) {
				if (regex != null) {
					regex += "|\\" + symbol;
				} else {
					regex = "\\" + symbol;
				}
			} else {
				if (regex != null) {
					regex += "|" + symbol;
				} else {
					regex = symbol;
				}
			}
		}
		return regex;
	}

	public static String[] concat(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] c = new String[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	public static String[] add(String[] a, String b) {
		int aLen = a.length;
		String[] c = new String[aLen + 1];
		System.arraycopy(a, 0, c, 0, aLen);
		c[aLen] = b;
		return c;
	}

	public static boolean areThereOnlySettedUpFunctionsSumsEquationsAndSystems(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Variable || fl.get(i) instanceof Sum || fl.get(i) instanceof SumSubtraction || fl.get(i) instanceof Subtraction || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof AnteriorFunction) {
					if (((AnteriorFunction) fl.get(i)).getVariable() == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionTwoValues) {
					if (((FunctionTwoValues) fl.get(i)).getVariable1() == null || ((FunctionTwoValues) fl.get(i)).getVariable2() == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlySettedUpFunctionsSumsMultiplicationsEquationsAndSystems(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Variable || fl.get(i) instanceof Multiplication || fl.get(i) instanceof Sum || fl.get(i) instanceof SumSubtraction || fl.get(i) instanceof Subtraction || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof AnteriorFunction) {
					if (((AnteriorFunction) fl.get(i)).getVariable() == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionTwoValues) {
					if (((FunctionTwoValues) fl.get(i)).getVariable1() == null || ((FunctionTwoValues) fl.get(i)).getVariable2() == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlySettedUpFunctionsEquationsAndSystems(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Variable || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof AnteriorFunction) {
					if (((AnteriorFunction) fl.get(i)).getVariable() == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionTwoValues) {
					if (((FunctionTwoValues) fl.get(i)).getVariable1() == null || ((FunctionTwoValues) fl.get(i)).getVariable2() == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlySettedUpFunctionsAndSystems(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Variable || fl.get(i) instanceof Equation || fl.get(i) instanceof EquationsSystemPart || fl.get(i) instanceof Expression)) {
				if (fl.get(i) instanceof AnteriorFunction) {
					if (((AnteriorFunction) fl.get(i)).getVariable() == null) {
						return false;
					}
				} else if (fl.get(i) instanceof FunctionTwoValues) {
					if (((FunctionTwoValues) fl.get(i)).getVariable1() == null || ((FunctionTwoValues) fl.get(i)).getVariable2() == null) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areThereOnlyEmptySNFunctions(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof AnteriorFunction) {
				if (((AnteriorFunction) fl.get(i)).getVariable() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereOnlyEmptyNSNFunctions(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof FunctionTwoValues && !(fl.get(i) instanceof Sum) && !(fl.get(i) instanceof SumSubtraction) && !(fl.get(i) instanceof Subtraction) && !(fl.get(i) instanceof Multiplication) && !(fl.get(i) instanceof Division)) {
				if (((FunctionTwoValues) fl.get(i)).getVariable1() == null && ((FunctionTwoValues) fl.get(i)).getVariable2() == null) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean areThereEmptyMultiplications(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof Multiplication || fl.get(i) instanceof Division) {
				if (((FunctionTwoValues) fl.get(i)).getVariable1() == null && ((FunctionTwoValues) fl.get(i)).getVariable2() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereEmptySums(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof Sum || fl.get(i) instanceof SumSubtraction || fl.get(i) instanceof Subtraction) {
				if (((FunctionTwoValues) fl.get(i)).getVariable1() == null && ((FunctionTwoValues) fl.get(i)).getVariable2() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereEmptySystems(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (fl.get(i) instanceof EquationsSystemPart) {
				if (((EquationsSystemPart) fl.get(i)).getVariable() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areThereOtherSettedUpFunctions(ArrayList<Function> fl) {
		for (int i = 0; i < fl.size(); i++) {
			if (!(fl.get(i) instanceof Number || fl.get(i) instanceof Variable || fl.get(i) instanceof Sum ||  fl.get(i) instanceof SumSubtraction || fl.get(i) instanceof Expression || fl.get(i) instanceof AnteriorFunction || fl.get(i) instanceof Multiplication || fl.get(i) instanceof Division)) {
				if (fl.get(i) instanceof AnteriorFunction) {
					if (((AnteriorFunction) fl.get(i)).getVariable() == null) {
						return true;
					}
				} else if (fl.get(i) instanceof FunctionTwoValues) {
					if (((FunctionTwoValues) fl.get(i)).getVariable1() == null || ((FunctionTwoValues) fl.get(i)).getVariable2() == null) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public static Rational getRational(BigDecimal str) {
		try {
			return getRational(str.toString());
		} catch (Error e) {
			//E' IMPOSSIBILE CHE VENGA THROWATO UN ERRORE
			return new Rational("0");
		}
	}

	public static Rational getRational(String str) throws Error {
		try {
			return new Rational(str);
		} catch (NumberFormatException ex) {
			if (new BigDecimal(str).compareTo(new BigDecimal(8000.0)) < 0 && new BigDecimal(str).compareTo(new BigDecimal(-8000.0)) > 0) {
				if (str.equals("-")) {
					str = "-1";
				}
				long bits = Double.doubleToLongBits(Double.parseDouble(str));

				long sign = bits >>> 63;
				long exponent = ((bits >>> 52) ^ (sign << 11)) - 1023;
				long fraction = bits << 12; // bits are "reversed" but that's
											// not a problem

				long a = 1L;
				long b = 1L;

				for (int i = 63; i >= 12; i--) {
					a = a * 2 + ((fraction >>> i) & 1);
					b *= 2;
				}

				if (exponent > 0)
					a *= 1 << exponent;
				else
					b *= 1 << -exponent;

				if (sign == 1)
					a *= -1;

				if (b == 0) {
					a = 0;
					b = 1;
				}

				return new Rational(new BigInteger(a + ""), new BigInteger(b + ""));
			} else {
				BigDecimal original = new BigDecimal(str);

				BigInteger numerator = original.unscaledValue();

				BigInteger denominator = BigDecimalMath.pow(BigDecimal.TEN, new BigDecimal(original.scale())).toBigIntegerExact();

				return new Rational(numerator, denominator);
			}
		}
	}

	public static BigDecimal rationalToIrrationalString(Rational r) {
		return BigDecimalMath.divideRound(new BigDecimal(r.numer()).setScale(Utils.scale, Utils.scaleMode), new BigDecimal(r.denom()).setScale(Utils.scale, Utils.scaleMode));
	}

	public static boolean equalsVariables(ArrayList<Variable> variables, ArrayList<Variable> variables2) {
		if (variables.size() != variables2.size()) {
			return false;
		} else {
			for (Variable v : variables) {
				if (!variables2.contains(v)) {
					return false;
				}
			}
			return true;
		}
	}

	public static void writeSquareRoot(Function var, int x, int y, boolean small) {
		var.setSmall(small);
		int w1 = var.getWidth();
		int h1 = var.getHeight();
		int wsegno = 5;
		int hsegno = h1 + 2;
		
		var.draw(x + wsegno, y + (hsegno - h1));

		glDrawLine(x + 1, y + hsegno - 3, x + 3, y + hsegno - 1);
		glDrawLine(x + 3, y + (hsegno - 1) / 2 + 1, x + 3, y + hsegno - 1);
		glDrawLine(x + 4, y, x + 4, y + (hsegno - 1) / 2);
		glDrawLine(x + 4, y, x + 4 + 1 + w1 + 1, y);
	}

	public static final int getFontHeight() {
		return getFontHeight(false);
	}

	public static final RAWFont getFont(boolean small) {
		return getFont(small, Main.zoomed);
	}

	public static final RAWFont getFont(boolean small, boolean zoomed) {
		return PIDisplay.fonts[getFontIndex(small, zoomed)];
	}

	public static final int getFontIndex(boolean small, boolean zoomed) {
		if (small) {
			if (zoomed) {
				return 3;
			} else {
				return 1;
			}
		} else {
			if (zoomed) {
				return 2;
			} else {
				return 0;
			}
		}
	}

	public static final int getFontHeight(boolean small) {
		return getFontHeight(small, Main.zoomed);
	}
	
	public static final int getFontHeight(boolean small, boolean zoomed) {
		if (small) {
			if (zoomed) {
				return PIDisplay.glyphsHeight[3];
			} else {
				return PIDisplay.glyphsHeight[1];
			}
		} else {
			if (zoomed) {
				return PIDisplay.glyphsHeight[2];
			} else {
				return PIDisplay.glyphsHeight[0];
			}
		}
	}
	
	public static byte[] convertStreamToByteArray(InputStream stream, long size) throws IOException {

	    // check to ensure that file size is not larger than Integer.MAX_VALUE.
	    if (size > Integer.MAX_VALUE) {
	        return new byte[0];
	    }

	    byte[] buffer = new byte[(int)size];
	    ByteArrayOutputStream os = new ByteArrayOutputStream();

	    int line = 0;
	    // read bytes from stream, and store them in buffer
	    while ((line = stream.read(buffer)) != -1) {
	        // Writes bytes from byte array (buffer) into output stream.
	        os.write(buffer, 0, line);
	    }
	    stream.close();
	    os.flush();
	    os.close();
	    return os.toByteArray();
	}

	public static int[] realBytes(byte[] bytes) {
		int len = bytes.length;
		int[] realbytes = new int[len];
		for (int i = 0; i < len; i++) {
			realbytes[i] = Byte.toUnsignedInt(bytes[i]);
		}
		return realbytes;
	}
	
	public static boolean allSolved(List<Function> expressions) throws Error {
		for (Function itm : expressions) {
			if (itm.isSolved() == false) {
				return false;
			}
		}
		return true;
	}

	public static Function[][] joinFunctionsResults(List<Function> l1, List<Function> l2) {
		int size1 = l1.size();
		int size2 = l2.size();
		int cur1 = 0;
		int cur2 = 0;
		int total = l1.size()*l2.size();
		Function[][] results = new Function[total][2];
		for (int i = 0; i < total; i++) {
			results[i] = new Function[]{l1.get(cur1), l2.get(cur2)};
			if (i % size2 == 0) {
				cur1+=1;
			}
			if (i % size1 == 0) {
				cur2+=1;
			}
			if (cur1 >= size1) cur1 = 0;
			if (cur2 >= size2) cur2 = 0;
		}
		return results;
	}

	public static boolean isNegative(Function b) {
		if (b instanceof Negative) {
			return true;
		} else if (b instanceof Number && ((Number) b).getTerm().compareTo(BigDecimal.ZERO) < 0) {
			return true;
		}
		return false;
	}

	public static CharSequence multipleChars(String string, int i) {
		String result = "";
		for (int j = 0; j < i; j++) {
			result+=string;
		}
		return result;
	}
	
	public static boolean isIntegerValue(BigDecimal bd) {
		 return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
	}

	public static <T> String arrayToString(T... data) {
		String sdata = "";
		for (T o : data) {
			sdata += ","+o.toString();
		}
		return sdata.substring(1);
	}

	public static String arrayToString(boolean... data) {
		String sdata = "";
		for (boolean o : data) {
			sdata += (o)?1:0;
		}
		return sdata;
	}
}