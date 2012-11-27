package com.hc.framework.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Logger {
	public static void printMessage(LogType type, String message) {
		SetLoggerPrintln(type, message);
	}
	
	public static void printMessage(LogType type) {	
		SetLoggerPrint(type);
	}
	
	public static void SetLoggerPrint(LogType logType) {
		System.out.print(logType.getType());
	}
	
	public static void SetLoggerPrintln(LogType logType, String text) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.print("\n[" + dateFormat.format(date) + "]" + logType.getType() + text);
	}
}
