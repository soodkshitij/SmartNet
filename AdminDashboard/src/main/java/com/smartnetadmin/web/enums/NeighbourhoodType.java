package com.smartnetadmin.web.enums;

public enum NeighbourhoodType {
	neighbourhood(0),
	public_agency(1);

	private final int value;

	private NeighbourhoodType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static NeighbourhoodType findByValue(int value) {
		switch (value) {
		case 0:
			return neighbourhood;
		case 1:
			return public_agency;
		default:
			return null;
		}
	}


}
