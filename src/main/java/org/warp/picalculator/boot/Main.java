package org.warp.picalculator.boot;

import java.util.Arrays;

import org.warp.picalculator.ConsoleUtils;
import org.warp.picalculator.PICalculator;

public class Main {
	public static void main(String[] args) throws Exception {
		new PICalculator(parseStartupArguments(args));
	}

	public static StartupArguments parseStartupArguments(final String[] a) {
		final StartupArgumentsImpl args = new StartupArgumentsImpl();
		Arrays.asList(a).stream().parallel().map(String::toLowerCase).forEach(arg -> parseArgument(args, arg));
		args.setHeadlessEngineForced(args.isHeadlessEngineForced() || args.isHeadless8EngineForced() || args.isHeadless256EngineForced() || args.isHeadless24bitEngineForced());
		return args;
	}

	public static void parseArgument(StartupArgumentsImpl args, String arg) {
		switch (arg) {
			case "-zoomed":
				args.setZoomed(true);
				break;
			case "-verbose":
				args.setVerboseLoggingEnabled(true);
				break;
			case "-noraspi":
				args.setRaspberryModeAllowed(false);
				break;
			case "nogui":
				args.setNoGUIEngineForced(true);
				break;
			case "ms-dos":
				args.setMSDOSModeEnabled(true);
				break;
			case "headless-8":
				args.setHeadless8EngineForced(true);
				break;
			case "headless-256":
				args.setHeadless256EngineForced(true);
				break;
			case "headless-24bit":
				args.setHeadless24bitEngineForced(true);
				break;
			case "-headless":
				args.setHeadlessEngineForced(true);
				break;
			case "html":
				args.setHTMLEngineForced(true);
				break;
			case "gpu":
				args.setGPUEngineForced(true);
				break;
			case "cpu":
				args.setCPUEngineForced(true);
				break;
			case "framebuffer":
				args.setFrameBufferEngineForced(true);
				break;
			case "-debug":
				args.setDebugEnabled(true);
				break;
			case "-uncached":
				args.setUncached(true);
				break;
			default:
				ConsoleUtils.out.println("Unrecognized argument " + arg);
				break;
		}
	}
}
