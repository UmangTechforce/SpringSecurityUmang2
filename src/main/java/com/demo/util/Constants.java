package com.demo.util;

import java.util.function.Predicate;

public class Constants {
	
	
	public static final Predicate<String> EMPTY_STRING = str -> 
    str != null && !str.trim().isEmpty(); 
}
