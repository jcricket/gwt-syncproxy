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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.RpcToken;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.AbstractSerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.SerializabilityUtil;

/**
 * @see com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter
 * @see com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader
 * @see com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter
 * @see com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader
 */
public class SyncClientSerializationStreamWriter extends
		AbstractSerializationStreamWriter {
	/**
	 * Enumeration used to provided typed instance writers.
	 */
	private enum ValueWriter {
		BOOLEAN {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				stream.writeBoolean(((Boolean) instance).booleanValue());
			}
		},
		BYTE {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				stream.writeByte(((Byte) instance).byteValue());
			}
		},
		CHAR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				stream.writeChar(((Character) instance).charValue());
			}
		},
		DOUBLE {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				stream.writeDouble(((Double) instance).doubleValue());
			}
		},
		FLOAT {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				stream.writeFloat(((Float) instance).floatValue());
			}
		},
		INT {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				stream.writeInt(((Integer) instance).intValue());
			}
		},
		LONG {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				stream.writeLong(((Long) instance).longValue());
			}
		},
		OBJECT {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) throws SerializationException {
				stream.writeObject(instance);
			}
		},
		SHORT {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				stream.writeShort(((Short) instance).shortValue());
			}
		},
		STRING {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				stream.writeString((String) instance);
			}
		};

		abstract void write(SyncClientSerializationStreamWriter stream,
				Object instance) throws SerializationException;
	}

	/**
	 * Enumeration used to provided typed vector writers.
	 */
	private enum VectorWriter {
		BOOLEAN_VECTOR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				boolean[] vector = (boolean[]) instance;
				stream.writeInt(vector.length);
				for (boolean element : vector) {
					stream.writeBoolean(element);
				}
			}
		},
		BYTE_VECTOR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				byte[] vector = (byte[]) instance;
				stream.writeInt(vector.length);
				for (byte element : vector) {
					stream.writeByte(element);
				}
			}
		},
		CHAR_VECTOR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				char[] vector = (char[]) instance;
				stream.writeInt(vector.length);
				for (char element : vector) {
					stream.writeChar(element);
				}
			}
		},
		DOUBLE_VECTOR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				double[] vector = (double[]) instance;
				stream.writeInt(vector.length);
				for (double element : vector) {
					stream.writeDouble(element);
				}
			}
		},
		FLOAT_VECTOR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				float[] vector = (float[]) instance;
				stream.writeInt(vector.length);
				for (float element : vector) {
					stream.writeFloat(element);
				}
			}
		},
		INT_VECTOR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				int[] vector = (int[]) instance;
				stream.writeInt(vector.length);
				for (int element : vector) {
					stream.writeInt(element);
				}
			}
		},
		LONG_VECTOR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				long[] vector = (long[]) instance;
				stream.writeInt(vector.length);
				for (long element : vector) {
					stream.writeLong(element);
				}
			}
		},
		OBJECT_VECTOR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) throws SerializationException {
				Object[] vector = (Object[]) instance;
				stream.writeInt(vector.length);
				for (Object element : vector) {
					stream.writeObject(element);
				}
			}
		},
		SHORT_VECTOR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				short[] vector = (short[]) instance;
				stream.writeInt(vector.length);
				for (short element : vector) {
					stream.writeShort(element);
				}
			}
		},
		STRING_VECTOR {
			@Override
			void write(SyncClientSerializationStreamWriter stream,
					Object instance) {
				String[] vector = (String[]) instance;
				stream.writeInt(vector.length);
				for (String element : vector) {
					stream.writeString(element);
				}
			}
		};

		abstract void write(SyncClientSerializationStreamWriter stream,
				Object instance) throws SerializationException;
	}

	private static void append(StringBuffer sb, String token) {
		assert token != null;
		sb.append(token);
		sb.append(RPC_SEPARATOR_CHAR);
	}

	/**
	 * Returns the {@link Class} instance to use for serialization. Enumerations
	 * are serialized as their declaring class while all others are serialized
	 * using their true class instance.
	 */
	private static Class<?> getClassForSerialization(Object instance) {
		assert instance != null;
		// Attempt to compensate for EnumMap
		/*
		 * if (instance instanceof Class<?>) { return (Class<?>) instance; }
		 * else
		 */if (instance instanceof Enum<?>) {
			 Enum<?> e = (Enum<?>) instance;
			 return e.getDeclaringClass();
		 } else {
			 return instance.getClass();
		 }
	}

	private static String quoteString(String str) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			switch (ch) {
			case 0:
				buffer.append("\\0");
				break;
			case '|':
				buffer.append("\\!");
				break;
			case '\\':
				buffer.append("\\\\");
				break;
			default:
				// buffer.append(ch);
				if (ch >= ' ' && ch <= 127) {
					buffer.append(ch);
				} else {
					String hex = Integer.toHexString(ch);
					buffer.append("\\u0000".substring(0, 6 - hex.length())
							+ hex);
				}
			}
		}

		return buffer.toString();
	}

	/**
	 * Map of {@link Class} objects to {@link ValueWriter}s.
	 */
	private static final Map<Class<?>, ValueWriter> CLASS_TO_VALUE_WRITER = new IdentityHashMap<Class<?>, ValueWriter>();

	/**
	 * Map of {@link Class} vector objects to {@link VectorWriter}s.
	 */
	private static final Map<Class<?>, VectorWriter> CLASS_TO_VECTOR_WRITER = new IdentityHashMap<Class<?>, VectorWriter>();

	static {
		CLASS_TO_VECTOR_WRITER
				.put(boolean[].class,
						SyncClientSerializationStreamWriter.VectorWriter.BOOLEAN_VECTOR);
		CLASS_TO_VECTOR_WRITER.put(byte[].class,
				SyncClientSerializationStreamWriter.VectorWriter.BYTE_VECTOR);
		CLASS_TO_VECTOR_WRITER.put(char[].class,
				SyncClientSerializationStreamWriter.VectorWriter.CHAR_VECTOR);
		CLASS_TO_VECTOR_WRITER.put(double[].class,
				SyncClientSerializationStreamWriter.VectorWriter.DOUBLE_VECTOR);
		CLASS_TO_VECTOR_WRITER.put(float[].class,
				SyncClientSerializationStreamWriter.VectorWriter.FLOAT_VECTOR);
		CLASS_TO_VECTOR_WRITER.put(int[].class,
				SyncClientSerializationStreamWriter.VectorWriter.INT_VECTOR);
		CLASS_TO_VECTOR_WRITER.put(long[].class,
				SyncClientSerializationStreamWriter.VectorWriter.LONG_VECTOR);
		CLASS_TO_VECTOR_WRITER.put(Object[].class,
				SyncClientSerializationStreamWriter.VectorWriter.OBJECT_VECTOR);
		CLASS_TO_VECTOR_WRITER.put(short[].class,
				SyncClientSerializationStreamWriter.VectorWriter.SHORT_VECTOR);
		CLASS_TO_VECTOR_WRITER.put(String[].class,
				SyncClientSerializationStreamWriter.VectorWriter.STRING_VECTOR);

		CLASS_TO_VALUE_WRITER.put(boolean.class,
				SyncClientSerializationStreamWriter.ValueWriter.BOOLEAN);
		CLASS_TO_VALUE_WRITER.put(byte.class,
				SyncClientSerializationStreamWriter.ValueWriter.BYTE);
		CLASS_TO_VALUE_WRITER.put(char.class,
				SyncClientSerializationStreamWriter.ValueWriter.CHAR);
		CLASS_TO_VALUE_WRITER.put(double.class,
				SyncClientSerializationStreamWriter.ValueWriter.DOUBLE);
		CLASS_TO_VALUE_WRITER.put(float.class,
				SyncClientSerializationStreamWriter.ValueWriter.FLOAT);
		CLASS_TO_VALUE_WRITER.put(int.class,
				SyncClientSerializationStreamWriter.ValueWriter.INT);
		CLASS_TO_VALUE_WRITER.put(long.class,
				SyncClientSerializationStreamWriter.ValueWriter.LONG);
		CLASS_TO_VALUE_WRITER.put(Object.class,
				SyncClientSerializationStreamWriter.ValueWriter.OBJECT);
		CLASS_TO_VALUE_WRITER.put(short.class,
				SyncClientSerializationStreamWriter.ValueWriter.SHORT);
		CLASS_TO_VALUE_WRITER.put(String.class,
				SyncClientSerializationStreamWriter.ValueWriter.STRING);
	}

	private StringBuffer encodeBuffer;

	private final String moduleBaseURL;

	private final String serializationPolicyStrongName;

	private SerializationPolicy serializationPolicy;
	private final Serializer serializer;
	private final RpcToken rpcToken;

	Logger logger = Logger.getLogger(SyncClientSerializationStreamWriter.class
			.getName());

	public SyncClientSerializationStreamWriter(Serializer serializer,
			String moduleBaseURL, String serializationPolicyStrongName,
			SerializationPolicy serializationPolicy, RpcToken rpcToken) {
		this.serializer = serializer;
		this.moduleBaseURL = moduleBaseURL;
		this.serializationPolicyStrongName = serializationPolicyStrongName;
		this.serializationPolicy = serializationPolicy;
		this.rpcToken = rpcToken;
		if (rpcToken != null) {
			addFlags(FLAG_RPC_TOKEN_INCLUDED);
		}
	}

	@Override
	protected void append(String token) {
		append(this.encodeBuffer, token);
	}

	@Override
	protected String getObjectTypeSignature(Object o) {
		Class<?> clazz = o.getClass();

		if (o instanceof Enum) {
			Enum<?> e = (Enum<?>) o;
			clazz = e.getDeclaringClass();
		}

		String typeName = clazz.getName();

		String serializationSignature = SerializabilityUtil
				.getSerializationSignature(clazz, this.serializationPolicy);
		if (serializationSignature != null) {
			typeName += "/" + serializationSignature;
		}
		return typeName;
	}

	@Override
	public void prepareToWrite() {
		super.prepareToWrite();
		this.encodeBuffer = new StringBuffer();

		// Write serialization policy info
		writeString(this.moduleBaseURL);
		writeString(this.serializationPolicyStrongName);
		if (hasFlags(FLAG_RPC_TOKEN_INCLUDED)) {
			try {
				serializeValue(this.rpcToken, this.rpcToken.getClass());
			} catch (SerializationException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void serialize(Object instance, String typeSignature)
			throws SerializationException {
		assert instance != null;

		Class<?> clazz = getClassForSerialization(instance);

		this.serializationPolicy.validateSerialize(clazz);

		serializeImpl(instance, clazz);
	}

	/**
	 * Serialize an instance that is an array. Will default to serializing the
	 * instance as an Object vector if the instance is not a vector of
	 * primitives, Strings or Object.
	 *
	 * @param instanceClass
	 * @param instance
	 * @throws SerializationException
	 */
	private void serializeArray(Class<?> instanceClass, Object instance)
			throws SerializationException {
		assert instanceClass.isArray();

		VectorWriter instanceWriter = CLASS_TO_VECTOR_WRITER.get(instanceClass);
		if (instanceWriter != null) {
			instanceWriter.write(this, instance);
		} else {
			SyncClientSerializationStreamWriter.VectorWriter.OBJECT_VECTOR
					.write(this, instance);
		}
	}

	private void serializeClass(Object instance, Class<?> instanceClass)
			throws SerializationException {
		assert instance != null;

		Field[] serializableFields = SerializabilityUtil
				.applyFieldSerializationPolicy(instanceClass,
						this.serializationPolicy);

		for (Field declField : serializableFields) {
			assert declField != null;

			boolean isAccessible = declField.isAccessible();
			boolean needsAccessOverride = !isAccessible
					&& !Modifier.isPublic(declField.getModifiers());
			if (needsAccessOverride) {
				// Override the access restrictions
				declField.setAccessible(true);
			}

			Object value;
			try {
				value = declField.get(instance);
				serializeValue(value, declField.getType());

			} catch (IllegalArgumentException e) {
				throw new SerializationException(e);

			} catch (IllegalAccessException e) {
				throw new SerializationException(e);
			}
		}

		Class<?> superClass = instanceClass.getSuperclass();
		if (this.serializationPolicy.shouldSerializeFields(superClass)) {
			serializeImpl(instance, superClass);
		}
	}

	/**
	 * @see com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter#serializeImpl
	 * @param instance
	 * @param instanceClass
	 * @throws SerializationException
	 */
	private void serializeImpl(Object instance, Class<?> instanceClass)
			throws SerializationException {
		assert instance != null;

		Class<?> customSerializer = SerializabilityUtil
				.hasCustomFieldSerializer(instanceClass);
		if (customSerializer != null) {
			// Use custom field serializer
			serializeWithCustomSerializer(customSerializer, instance,
					instanceClass);
		} else if (instanceClass.isArray()) {
			serializeArray(instanceClass, instance);
		} else if (instanceClass.isEnum()) {
			writeInt(((Enum<?>) instance).ordinal());
		} else {
			// Regular class instance
			serializeClass(instance, instanceClass);
		}
	}

	public void serializeValue(Object value, Class<?> type)
			throws SerializationException {
		ValueWriter valueWriter = CLASS_TO_VALUE_WRITER.get(type);
		if (valueWriter != null) {
			valueWriter.write(this, value);
		} else {
			// Arrays of primitive or reference types need to go through
			// writeObject.
			SyncClientSerializationStreamWriter.ValueWriter.OBJECT.write(this,
					value);
		}
	}

	private void serializeWithCustomSerializer(Class<?> customSerializer,
			Object instance, Class<?> instanceClass)
			throws SerializationException {
		this.logger.info("Serializing with Custom Serializer: "
				+ instanceClass.getName() + " - " + customSerializer.getName());
		try {
			assert !instanceClass.isArray();

			for (Method method : customSerializer.getMethods()) {
				if ("serialize".equals(method.getName())) {
					method.invoke(null, this, instance);
					return;
				}
			}
			throw new NoSuchMethodException("serialize");
		} catch (SecurityException e) {
			throw new SerializationException(e);

		} catch (NoSuchMethodException e) {
			throw new SerializationException(e);

		} catch (IllegalArgumentException e) {
			throw new SerializationException(e);

		} catch (IllegalAccessException e) {
			throw new SerializationException(e);

		} catch (InvocationTargetException e) {
			throw new SerializationException(e);
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		writeHeader(buffer);
		writeStringTable(buffer);
		writePayload(buffer);
		return buffer.toString();
	}

	private void writeHeader(StringBuffer buffer) {
		append(buffer, String.valueOf(getVersion()));
		append(buffer, String.valueOf(getFlags()));
	}

	/**
	 * @see ClientSerializationStreamWriter#writeLong
	 * @param fieldValue
	 */
	@Override
	public void writeLong(long fieldValue) {
		if (getVersion() == SERIALIZATION_STREAM_MIN_VERSION) {
			double[] parts;
			parts = makeLongComponents((int) (fieldValue >> 32),
					(int) fieldValue);
			assert parts.length == 2;
			writeDouble(parts[0]);
			writeDouble(parts[1]);
		} else {
			append(Utils.toBase64(fieldValue));
		}
	}

	private void writePayload(StringBuffer buffer) {
		buffer.append(this.encodeBuffer.toString());
	}

	private StringBuffer writeStringTable(StringBuffer buffer) {
		List<String> stringTable = getStringTable();
		append(buffer, String.valueOf(stringTable.size()));
		for (String s : stringTable) {
			append(buffer, quoteString(s));
		}
		return buffer;
	}
}
