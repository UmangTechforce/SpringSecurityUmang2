package com.demo.enums;

import java.util.HashSet;
import java.util.Set;

public enum Role {

	ADMIN("ADMIN"), EMPLOYEE("EMPLOYEE");

	private String role;

	private Role(String role) {

		this.role = role;
	}

	private static Set<String> roles = new HashSet<>();

	static {
		for (Role role : Role.values()) {
			roles.add(role.name());
		}
	}

	public String getRole() {
		return role;
	}

	public static Boolean getName(String role) {
		if (roles.contains(role.toUpperCase())) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
