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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.MethodNotSupportedException;

import com.gdevelop.gwt.syncrpc.exception.SyncProxyException;
import com.gdevelop.gwt.syncrpc.exception.SyncProxyException.InfoType;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RpcToken;
import com.google.gwt.user.client.rpc.RpcToken.RpcTokenImplementation;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.RpcTokenExceptionHandler;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.server.rpc.impl.SerializabilityUtil;

/**
 * Handles method call delegation from the Proxy interfaces
 *
 */
public class RemoteServiceInvocationHandler implements InvocationHandler {
	/**
	 * Attempts to determine the service interface class of the provided proxy
	 * object.
	 *
	 * @since 0.5
	 * @return the base service interface class
	 * @throws ClassNotFoundException
	 *             if attempt to find base interface from class ending in
	 *             "Async" fails
	 */
	protected static Class<?> determineProxyServiceBaseInterface(Object proxy)
			throws ClassNotFoundException {
		for (Class<?> clazz : proxy.getClass().getInterfaces()) {
			if (RemoteService.class.isAssignableFrom(clazz)) {
				return clazz;
			}
			if (clazz.getName().endsWith(SyncProxy.ASYNC_POSTFIX)) {
				return Class.forName(clazz.getName().replace(
						SyncProxy.ASYNC_POSTFIX, ""));
			}
			// if (!ServiceDefTarget.class.equals(clazz)
			// && !HasRpcToken.class.equals(clazz)
			// && !SerializationStreamFactory.class.equals(clazz)) {
			// srvcIntf = Class.forName(clazz.getName().replace("Async",
			// ""));
			// }
		}
		return null;
	}

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

	RpcToken token;

	String serviceEntryPoint;

	RpcTokenExceptionHandler rpcTokenExceptionHandler;

	Logger logger = Logger.getLogger(RemoteServiceInvocationHandler.class
			.getName());
	HasProxySettings settings;

	public RemoteServiceInvocationHandler(HasProxySettings settings) {
		this(settings.getModuleBaseUrl(), settings
				.getRemoteServiceRelativePath(), settings.getPolicyName(),
				settings.getCookieManager(), settings.isWaitForInvocation());
		this.settings = settings;
	}

	/**
	 * Legacy Support. Use
	 * {@link #RemoteServiceInvocationHandler(HasProxySettings)} where possible,
	 * this method may be deprecated in a future release
	 *
	 * Uses waitForInvocation default as false.
	 * 
	 * @deprecated since 0.5, preference to use
	 *             {@link #RemoteServiceInvocationHandler(HasProxySettings)}
	 */
	@Deprecated
	public RemoteServiceInvocationHandler(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			CookieManager cookieManager) {
		this(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName,
				cookieManager, false);
	}

	/**
	 * Legacy Support. Use
	 * {@link #RemoteServiceInvocationHandler(HasProxySettings)} where possible,
	 * this method may be deprecated in a future release.
	 *
	 * @deprecated since 0.5, preference to use
	 *             {@link #RemoteServiceInvocationHandler(HasProxySettings)}
	 */
	@Deprecated
	public RemoteServiceInvocationHandler(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			CookieManager cookieManager, boolean waitForInvocation) {
		this.settings = new ProxySettings(moduleBaseURL,
				remoteServiceRelativePath, serializationPolicyName,
				cookieManager, waitForInvocation);
	}

	private ResponseReader getReaderFor(Class<?> type) {
		this.logger.finer("Getting reader for: " + type.getName());
		ResponseReader primitiveResponseReader = JPRIMITIVETYPE_TO_RESPONSEREADER
				.get(type);
		if (primitiveResponseReader != null) {
			return primitiveResponseReader;
		}

		if (type == String.class) {
			return ResponseReader.STRING;
		}
		if (type == Void.class || type == void.class) {
			return ResponseReader.VOID;
		}

		return ResponseReader.OBJECT;
	}

	/**
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected Object handleHasProxySettings(Object proxy, Method method,
			Object[] args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		return method.invoke(this.settings, args);
	}

	/**
	 *
	 * Handles method invocations to the {@link HasRpcToken} interface
	 * implemented by the service. Also handles Annotation of service with
	 * {@link RpcTokenImplementation}
	 *
	 * @throws ClassNotFoundException
	 *             if base service interface class cannot be found if actual
	 *             implemented interface is Async
	 *
	 * @since 0.5
	 */
	protected Object handleHasRpcToken(Object proxy, Method method,
			Object[] args) throws MethodNotSupportedException,
			NoSuchMethodException, ClassNotFoundException {
		if (HasRpcToken.class.getMethod("setRpcToken", RpcToken.class).equals(
				method)) {
			// Check if service has annotation defining the Token class and
			// that this token matches the specified class
			Class<?> srvcIntf = determineProxyServiceBaseInterface(proxy);
			if (srvcIntf != null) {
				RpcTokenImplementation rti = srvcIntf
						.getAnnotation(RpcTokenImplementation.class);
				// Replace $ in class name in order to handle inner classes
				if (rti != null
						&& !args[0].getClass().getName().replace("$", ".")
								.equals(rti.value())) {
					throw new RpcTokenException("Incorrect Token Class. Got "
							+ args[0].getClass().getName() + " but expected: "
							+ rti.value());
				}
			}

			this.token = (RpcToken) args[0];
			return null;
		} else if (HasRpcToken.class.getMethod("getRpcToken").equals(method)) {
			return this.token;
		} else if (HasRpcToken.class.getMethod("setRpcTokenExceptionHandler",
				RpcTokenExceptionHandler.class).equals(method)) {
			this.rpcTokenExceptionHandler = (RpcTokenExceptionHandler) args[0];
			return null;
		} else if (HasRpcToken.class.getMethod("setRpcTokenExceptionHandler",
				RpcTokenExceptionHandler.class).equals(method)) {
			return this.rpcTokenExceptionHandler;
		}
		throw new MethodNotSupportedException("Method: " + method.getName()
				+ " in class: " + method.getDeclaringClass().getName()
				+ " not defined for class: " + proxy.getClass().getName());
	}

	/**
	 * Handles method invocations to the {@link ServiceDefTarget} interface
	 * implemented by the service.
	 */
	protected Object handleServiceDefTarget(Object proxy, Method method,
			Object[] args) throws Throwable {
		if (ServiceDefTarget.class.getMethod("getSerializationPolicyName")
				.equals(method)) {
			return this.settings.getPolicyName();
		} else if (ServiceDefTarget.class.getMethod("setServiceEntryPoint",
				String.class).equals(method)) {
			this.serviceEntryPoint = (String) args[0];
			// Modify current base and relative Path to newly specific
			// serviceEntryPoint assuming that base path is part of
			// serviceEntryPoint
			// TODO May not be a valid assumption
			if (this.serviceEntryPoint.contains(this.settings
					.getModuleBaseUrl())) {
				this.settings
						.setRemoteServiceRelativePath(this.serviceEntryPoint
								.split(this.settings.getModuleBaseUrl())[1]);
			} else {
				this.logger.warning("Unable to determine base (orig: "
						+ this.settings.getModuleBaseUrl() + ") against: "
						+ this.serviceEntryPoint);
				throw new SyncProxyException(
						determineProxyServiceBaseInterface(proxy),
						InfoType.SERVICE_BASE_DELTA);
			}
			return null;
		} else if (ServiceDefTarget.class.getMethod("getServiceEntryPoint")
				.equals(method)) {
			return this.serviceEntryPoint;
		}
		// TODO handle all methods
		throw new MethodNotSupportedException("Method: " + method.getName()
				+ " in class: " + method.getDeclaringClass().getName()
				+ " not defined for class: " + proxy.getClass().getName());
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		this.logger.info("Invoking " + method.getName() + " on proxy: "
				+ proxy.getClass().getName());
		if (this.logger.getLevel() != null
				&& this.logger.getLevel().intValue() <= Level.CONFIG.intValue()) {
			String intfs = "";
			for (Class<?> intf : proxy.getClass().getInterfaces()) {
				intfs += intf.getName() + ",";
			}
			this.logger.config("Proxy has interfaces: " + intfs);
		}
		// Handle case where method call is GWT Client Intf
		// (ServiceDefTarget,HasRpcToken)
		if (ServiceDefTarget.class.getName().equals(
				method.getDeclaringClass().getName())) {
			this.logger
					.info("Handling invocation of ServiceDefTarget Interface");
			return handleServiceDefTarget(proxy, method, args);
		} else if (HasRpcToken.class.getName().equals(
				method.getDeclaringClass().getName())) {
			this.logger.info("Handling invocation of HasRpcToken Interface");
			return handleHasRpcToken(proxy, method, args);
		} else if (HasProxySettings.class.getName().equals(
				method.getDeclaringClass().getName())) {
			this.logger
			.info("Handling invocation of HasProxySettings Interface");
			return handleHasProxySettings(proxy, method, args);
		}
		RemoteServiceSyncProxy syncProxy = new RemoteServiceSyncProxy(
				this.settings.getModuleBaseUrl(),
				this.settings.getRemoteServiceRelativePath(),
				this.settings.getPolicyName(),
				this.settings.getCookieManager(), this.token,
				this.rpcTokenExceptionHandler);
		// Handle delegation of calls to the RemoteServiceProxy hierarchy
		if (SerializationStreamFactory.class.getName().equals(
				method.getDeclaringClass().getName())) {
			this.logger
					.info("Handling invocation of SerializationStreamFactory Interface");
			return method.invoke(syncProxy, args);
		}

		// // Get Service Interface
		Class<?> remoteServiceIntf = method.getDeclaringClass();

		SerializationStreamWriter streamWriter = syncProxy.createStreamWriter();

		AsyncCallback<?> callback = null;
		Class<?>[] paramTypes = method.getParameterTypes();
		try {
			// Determine whether sync or async
			boolean isAsync = false;
			// String serviceIntfName =
			// method.getDeclaringClass().getCanonicalName();
			String serviceIntfName = remoteServiceIntf.getCanonicalName();
			int paramCount = paramTypes.length;
			Class<?> returnType = method.getReturnType();
			if (method.getDeclaringClass().getCanonicalName().endsWith("Async")) {
				this.logger.info("Invoking as an Async Service");
				isAsync = true;
				serviceIntfName = serviceIntfName.substring(0,
						serviceIntfName.length() - 5);
				paramCount--;
				callback = (AsyncCallback<?>) args[paramCount];

				// Determine the return type
				Class<?>[] syncParamTypes = new Class[paramCount];
				System.arraycopy(paramTypes, 0, syncParamTypes, 0, paramCount);
				Class<?> clazz;
				try {
					clazz = Class.forName(serviceIntfName);
				} catch (ClassNotFoundException e) {
					throw new InvocationException(
							"There is no sync version of " + serviceIntfName
									+ "Async");
				}
				Method syncMethod = null;
				try {
					syncMethod = clazz.getMethod(method.getName(),
							syncParamTypes);
					this.logger.fine("Sync Method determined: "
							+ syncMethod.getName());
				} catch (NoSuchMethodException nsme) {
					String temp = "";
					for (Class<?> cl : syncParamTypes) {
						temp += cl.getSimpleName() + ",";
					}
					throw new NoSuchMethodException("SPNoMeth "
							+ method.getName() + " class "
							+ clazz.getSimpleName() + " params " + temp);
				}
				if (syncMethod != null) {
					returnType = syncMethod.getReturnType();
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
			this.logger.config("Payload: " + payload);
			if (isAsync) {
				this.logger.info("Making Remote call as Async");
				final RemoteServiceSyncProxy syncProxy_2 = syncProxy;
				final Class<?> returnType_2 = returnType;
				final String payload_2 = payload;
				final AsyncCallback callback_2 = callback;

				// Separate Thread since this should be de-synchronized
				Thread thread = new Thread() {
					@Override
					public void run() {
						Object result;
						try {
							result = syncProxy_2.doInvoke(
									getReaderFor(returnType_2), payload_2);
							// Check to make sure response should be processed,
							// or not in case of situation such as
							// RpcTokenException handled by a separate handler
							if (!syncProxy_2.shouldIgnoreResponse()
									&& callback_2 != null) {
								callback_2.onSuccess(result);
							}
						} catch (Throwable e) {
							if (callback_2 != null) {
								callback_2.onFailure(e);
							}
						}
					}
				};
				if (this.settings.isWaitForInvocation()) {
					thread.run();
				} else {
					thread.start();
				}
				return null;
			} else {
				this.logger.info("Making Remote call as Sync");
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
}
