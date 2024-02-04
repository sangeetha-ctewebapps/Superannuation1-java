package com.lic.epgs.surrender.constants;

import java.util.Arrays;
import java.util.List;

public interface PolicySurrenderContansts {

	public static List<String> inprogressMaker() {
		return Arrays.asList("1","3","8");
	}
	
	public static List<String> existingMaker() {
		return Arrays.asList("4","5");
	}

	public static List<String> inprogressChecker() {
		return Arrays.asList("2");
	}

	public static List<String> existingChecker() {
		return Arrays.asList("4","5");
	}

	String INVALID_SURRENDER_ID = "Invalid Surrender";
}
