package com.lic.epgs.policyservicing.policylvl.constants;

import java.util.Arrays;
import java.util.List;

public interface PolicyLevelMergerContansts {

	public static List<Integer> inprogressMaker() {
		return Arrays.asList(1,3,8);
	}
	
	public static List<Integer> existingMaker() {
		return Arrays.asList(4,5);
	}

	public static List<Integer> inprogressChecker() {
		return Arrays.asList(2);
	}

	public static List<Integer> existingChecker() {
		return Arrays.asList(4,5);
	}

	String INVALID_MERGE_ID = "Invalid Merger";
	public static final String POLICY_MERGED = "17";
}
