package com.hc.framework.logger;

import java.awt.Color;

public enum LogType {
	CONSOLE_INFO("[INFO]", Color.white),
	CONSOLE_ERROR("[ERROR]", Color.red),
	CONSOLE_COMMAND("HC >> ", Color.white);
	
	private LogType(String type, Color color) {
		this.type = type;
		this.color = color;
	}
	
	@Override
	public String toString() {
		return this.type;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getType() {
		return type;
	}
	
	private final String type;
	private final Color color;
}
