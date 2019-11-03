package com.api.mt.auth.util;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleMapper {
	
	private Logger log = LoggerFactory.getLogger(SimpleMapper.class);
	
	public Object assign(Object objectFrom, Object objectTo) {
		Class classFrom = objectFrom.getClass();
		Class classTo = objectTo.getClass();

		Method methodsFrom[] = classFrom.getDeclaredMethods();

		for (Method methodFrom : methodsFrom) {
			if ("get".equals(methodFrom.getName().substring(0, 3))) {
				try {
					Object outputGet = methodFrom.invoke(objectFrom, null);
					
					if (outputGet != null) {
						String strMethodName = "s" + methodFrom.getName().substring(1);
						
						Method methodTo = classTo.getMethod(strMethodName, outputGet.getClass());
						methodTo.invoke(objectTo, outputGet);
					}
				} catch (Exception e) {}
			}
		}
		
		return objectTo;
	}

}
