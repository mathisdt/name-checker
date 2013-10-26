package org.zephyrsoft.name.checker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Start {

	private static final Logger LOG = LoggerFactory.getLogger(Start.class);

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			LOG.error("no argument given - you must specify the file that you want to check");
			System.exit(1);
		}
		NameChecker checker = new NameChecker(args[0]);
		boolean checkOK = checker.check();
		if (!checkOK) {
			System.exit(2);
		}
	}

}
