package com.smartnetadmin.web.enums;

public enum UserType {
	root_admin(0),
	community_admin(1);

	private final int value;

	private UserType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static UserType findByValue(int value) {
		switch (value) {
		case 0:
			return root_admin;
		case 1:
			return community_admin;
		default:
			return null;
		}
	}


}
