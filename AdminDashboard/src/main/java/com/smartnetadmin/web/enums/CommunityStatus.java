package com.smartnetadmin.web.enums;

public enum CommunityStatus {
	Pending(0),
	active(1),
	paused(2);

	private final int value;

	private CommunityStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static CommunityStatus findByValue(int value) {
		switch (value) {
		case 0:
			return Pending;
		case 1:
			return active;
		case 2:
			return paused;
		default:
			return null;
		}
	}


}
