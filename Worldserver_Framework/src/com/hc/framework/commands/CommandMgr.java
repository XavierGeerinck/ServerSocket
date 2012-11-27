package com.hc.framework.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.hc.framework.annotations.Command;

public class CommandMgr {
	private final HashMap<String, CommandHandle> chatCommands;
	private final Class<?> c;
	private final Object w;
	
	public CommandMgr(Object w, Class<?> c) {
		chatCommands = new HashMap<String, CommandHandle>();
		this.w = w;
		this.c = c;
	}
	
	public void loadChatCommands() {
		// Check the annotation in all the methods.
		for (Method m : c.getDeclaredMethods()) {
			
			// Get the annotations for that method
			Command annotation = m.getAnnotation(Command.class);
			
			if (annotation != null) {
				chatCommands.put(annotation.name(), new CommandHandle(annotation, m));
			} 
		}
	}
	
	public String getDescription(String command) {
		return chatCommands.get(command).getAnnotationDescription();
	}
	
	public void parse(String command) {
		if (command.length() == 0)
			return;
		
		for (CommandHandle ch : chatCommands.values()) {
			if (command.contains(ch.getAnnotationName())) {
				try {
					ch.getMethod().invoke(w, (Object)ch.getArgs(command));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
