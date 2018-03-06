package com.ptc.integrity.automation.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

/**
 * The Class MethodUtils.
 */
public class MethodUtils {
	
	/**
	 * Invoke method.
	 *
	 * @param object the object
	 * @param methodName the method name
	 * @param args the args
	 * @return the object
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public static Object invokeMethod(final Object object,
			final String methodName, Object... args)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		args = ArrayUtils.nullToEmpty(args);
		final Class<?>[] parameterTypes = ClassUtils.toClass(args);
		return invokeMethod(object, methodName, args, parameterTypes);
	}

	/**
	 * Invoke method.
	 *
	 * @param object the object
	 * @param methodName the method name
	 * @param args the args
	 * @param parameterTypes the parameter types
	 * @return the object
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public static Object invokeMethod(final Object object,
			final String methodName, Object[] args, Class<?>[] parameterTypes)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
		args = ArrayUtils.nullToEmpty(args);
		final Method method = getMatchingAccessibleMethod(object.getClass(),
				methodName, parameterTypes);
		if (method == null) {
			throw new NoSuchMethodException("No such accessible method: "
					+ methodName + "() on object: "
					+ object.getClass().getName());
		}
		return method.invoke(object, args);
	}

	/**
	 * Gets the matching accessible method.
	 *
	 * @param cls the cls
	 * @param methodName the method name
	 * @param parameterTypes the parameter types
	 * @return the matching accessible method
	 */
	public static Method getMatchingAccessibleMethod(final Class<?> cls,
			final String methodName, final Class<?>... parameterTypes) {
		try {
			final Method method = cls.getMethod(methodName, parameterTypes);
			return method;
		} catch (final NoSuchMethodException e) { // NOPMD - Swallow the
													// exception
		}
		// search through all methods
		Method bestMatch = null;
		final Method[] methods = cls.getMethods();
		for (final Method method : methods) {
			// compare name and parameters
			if (method.getName().equalsIgnoreCase(methodName)
					&& ClassUtils.isAssignable(parameterTypes,
							method.getParameterTypes(), true)) {
				// get accessible version of method
				bestMatch = org.apache.commons.lang3.reflect.MethodUtils
						.getAccessibleMethod(method);

			}
		}
		return bestMatch;
	}

}
