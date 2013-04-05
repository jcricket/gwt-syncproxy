/*
 * Copyright www.gdevelop.com.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.server.rpc.impl.SerializabilityUtil;

public class RemoteServiceInvocationHandler implements InvocationHandler {
	private static final Map<Class<?>, ResponseReader> JPRIMITIVETYPE_TO_RESPONSEREADER = new HashMap<Class<?>, ResponseReader>();
	static {
		// JPRIMITIVETYPE_TO_RESPONSEREADER.put(Boolean.class,
		// ResponseReader.BOOLEAN);
		// JPRIMITIVETYPE_TO_RESPONSEREADER.put(Byte.class,
		// ResponseReader.BYTE);
		// JPRIMITIVETYPE_TO_RESPONSEREADER.put(Character.class,
		// ResponseReader.CHAR);
		// JPRIMITIVETYPE_TO_RESPONSEREADER.put(Double.class,
		// ResponseReader.DOUBLE);
		// JPRIMITIVETYPE_TO_RESPONSEREADER.put(Float.class,
		// ResponseReader.FLOAT);
		// JPRIMITIVETYPE_TO_RESPONSEREADER.put(Integer.class,
		// ResponseReader.INT);
		// JPRIMITIVETYPE_TO_RESPONSEREADER.put(Long.class,
		// ResponseReader.LONG);
		// JPRIMITIVETYPE_TO_RESPONSEREADER.put(Short.class,
		// ResponseReader.SHORT);
		// JPRIMITIVETYPE_TO_RESPONSEREADER.put(Void.class,
		// ResponseReader.VOID);

		JPRIMITIVETYPE_TO_RESPONSEREADER.put(boolean.class,
				ResponseReader.BOOLEAN);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(byte.class, ResponseReader.BYTE);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(char.class, ResponseReader.CHAR);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(double.class,
				ResponseReader.DOUBLE);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(float.class, ResponseReader.FLOAT);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(int.class, ResponseReader.INT);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(long.class, ResponseReader.LONG);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(short.class, ResponseReader.SHORT);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(void.class, ResponseReader.VOID);
	}

	private final String moduleBaseURL;
	private final String remoteServiceRelativePath;
	private final String serializationPolicyName;
	private final CookieManager cookieManager;
	private final boolean waitForInvocation;

	public RemoteServiceInvocationHandler(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			CookieManager cookieManager) {
		this(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName,
				cookieManager, false);
	}

	public RemoteServiceInvocationHandler(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			CookieManager cookieManager, boolean waitForInvocation) {
		this.moduleBaseURL = moduleBaseURL;
		this.remoteServiceRelativePath = remoteServiceRelativePath;
		this.serializationPolicyName = serializationPolicyName;
		this.cookieManager = cookieManager;
		this.waitForInvocation = waitForInvocation;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		RemoteServiceSyncProxy syncProxy = new RemoteServiceSyncProxy(
				moduleBaseURL, remoteServiceRelativePath,
				serializationPolicyName, cookieManager);
		Class<?> remoteServiceInft = method.getDeclaringClass();
		for (Class<?> intf : proxy.getClass().getInterfaces()) {
			if (RemoteService.class.isAssignableFrom(intf)) {
				remoteServiceInft = intf;
			}
		}

		SerializationStreamWriter streamWriter = syncProxy.createStreamWriter();

		AsyncCallback callback = null;
		Class<?>[] paramTypes = method.getParameterTypes();
		try {
			// Determine whether sync or async
			boolean isAsync = false;
			// String serviceIntfName =
			// method.getDeclaringClass().getCanonicalName();
			String serviceIntfName = remoteServiceInft.getCanonicalName();
			int paramCount = paramTypes.length;
			Class<?> returnType = method.getReturnType();
			if (method.getDeclaringClass().getCanonicalName().endsWith("Async")) {
				isAsync = true;
				serviceIntfName = serviceIntfName.substring(0,
						serviceIntfName.length() - 5);
				paramCount--;
				callback = (AsyncCallback) args[paramCount];

				// Determine the return type
				Class<?>[] syncParamTypes = new Class[paramCount];
				System.arraycopy(paramTypes, 0, syncParamTypes, 0, paramCount);
				Class<?> clazz;
				try {
					clazz = Class.forName(serviceIntfName);
				} catch (ClassNotFoundException e) {
					throw new InvocationException(
							"There are not sync version of " + serviceIntfName
									+ "Async");
				}
				Method syncMethod = clazz.getMethod(method.getName(),
						syncParamTypes);
				if (syncMethod != null) {
					returnType = syncMethod.getReturnType();
				} else {
					throw new InvocationException(
							"Sync & Async method does not match.");
				}
			}

			// Interface name
			streamWriter.writeString(serviceIntfName);
			// Method name
			streamWriter.writeString(method.getName());

			// Params count
			streamWriter.writeInt(paramCount);

			// Params type
			for (int i = 0; i < paramCount; i++) {
				// streamWriter.writeString(computeBinaryClassName(paramTypes[i]));
				streamWriter.writeString(SerializabilityUtil
						.getSerializedTypeName(paramTypes[i]));
			}

			// Params
			for (int i = 0; i < paramCount; i++) {
				writeParam(streamWriter, paramTypes[i], args[i]);
			}

			String payload = streamWriter.toString();
			if (isAsync) {
				final RemoteServiceSyncProxy syncProxy_2 = syncProxy;
				final Class<?> returnType_2 = returnType;
				final String payload_2 = payload;
				final AsyncCallback callback_2 = callback;
				Thread thread = new Thread() {
					@Override
					public void run() {
						Object result;
						try {
							result = syncProxy_2.doInvoke(
									getReaderFor(returnType_2), payload_2);
							if (callback_2 != null) {
								callback_2.onSuccess(result);
							}
						} catch (Throwable e) {
							if (callback_2 != null) {
								callback_2.onFailure(e);
							}
						}
					}
				};
				if (this.waitForInvocation) {
					thread.run();
				} else {
					thread.start();
				}
				return null;
			} else {
				return syncProxy.doInvoke(getReaderFor(returnType), payload);
			}
			/*
			 * Object result = syncProxy.doInvoke(getReaderFor(returnType),
			 * payload); if (callback != null){ callback.onSuccess(result); }
			 * return result;
			 */
		} catch (Throwable ex) {
			if (callback != null) {
				callback.onFailure(ex);
				return null;
			}
			Class<?>[] expClasses = method.getExceptionTypes();
			for (Class<?> clazz : expClasses) {
				if (clazz.isAssignableFrom(ex.getClass())) {
					throw ex;
				}
			}

			throw ex;
		}
	}

	private void writeParam(SerializationStreamWriter streamWriter,
			Class<?> paramType, Object paramValue)
			throws SerializationException {
		if (paramType == boolean.class) {
			streamWriter.writeBoolean((Boolean) paramValue);
			// } else if (paramType == Boolean.class){
			// streamWriter.writeBoolean((Boolean)paramValue);
		} else if (paramType == byte.class) {
			streamWriter.writeByte((Byte) paramValue);
			// } else if (paramType == Byte.class){
			// streamWriter.writeByte((Byte)paramValue);
		} else if (paramType == char.class) {
			streamWriter.writeChar((Character) paramValue);
			// } else if (paramType == Character.class){
			// streamWriter.writeChar((Character)paramValue);
		} else if (paramType == double.class) {
			streamWriter.writeDouble((Double) paramValue);
			// } else if (paramType == Double.class){
			// streamWriter.writeDouble((Double)paramValue);
		} else if (paramType == float.class) {
			streamWriter.writeFloat((Float) paramValue);
			// } else if (paramType == Float.class){
			// streamWriter.writeFloat((Float)paramValue);
		} else if (paramType == int.class) {
			streamWriter.writeInt((Integer) paramValue);
			// }else if (paramType == Integer.class){
			// streamWriter.writeInt((Integer)paramValue);
		} else if (paramType == long.class) {
			streamWriter.writeLong((Long) paramValue);
			// } else if (paramType == Long.class){
			// streamWriter.writeLong((Long)paramValue);
		} else if (paramType == short.class) {
			streamWriter.writeShort((Short) paramValue);
			// } else if (paramType == Short.class){
			// streamWriter.writeShort((Short)paramValue);
		} else if (paramType == String.class) {
			streamWriter.writeString((String) paramValue);
		} else {
			streamWriter.writeObject(paramValue);
		}
	}

	private ResponseReader getReaderFor(Class<?> type) {
		ResponseReader primitiveResponseReader = JPRIMITIVETYPE_TO_RESPONSEREADER
				.get(type);
		if (primitiveResponseReader != null) {
			return primitiveResponseReader;
		}

		if (type == String.class) {
			return ResponseReader.STRING;
		}
		if ((type == Void.class) || (type == void.class)) {
			return ResponseReader.VOID;
		}

		return ResponseReader.OBJECT;
	}
}
