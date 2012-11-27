package com.hc.framework.commands;

import java.lang.reflect.Method;

import com.hc.framework.annotations.Command;

public class CommandHandle {
	private final Method m;
	private final Command c;
	
	public CommandHandle(Command c, Method m) {
		this.m = m;
		this.c = c;
	}
	
	public String getAnnotationDescription() {
		return c.description();
	}
	
	public String getAnnotationName() {
		return c.name();
	}
	
	public String getAnnotationDelimiter() {
		return c.delimiter();
	}
	
	public String[] getArgs(String command) {
		// Remove the command from the string first.
		String temp = command.replace(c.name() + c.delimiter(), "");
		
		// Then split the arguments
		String[] args = temp.split(c.delimiter());

		return args;
	}
	
	public Method getMethod() {
		return m;
	}
}
