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

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.AbstractSerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.SerializabilityUtil;


/**
 * @see com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter
 * @see com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader
 * @see com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter
 * @see com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader
 */
public class SyncClientSerializationStreamWriter extends AbstractSerializationStreamWriter{
  /**
   * Enumeration used to provided typed instance writers.
   */
  private enum ValueWriter {
    BOOLEAN {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        stream.writeBoolean(((Boolean) instance).booleanValue());
      }
    },
    BYTE {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        stream.writeByte(((Byte) instance).byteValue());
      }
    },
    CHAR {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        stream.writeChar(((Character) instance).charValue());
      }
    },
    DOUBLE {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        stream.writeDouble(((Double) instance).doubleValue());
      }
    },
    FLOAT {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        stream.writeFloat(((Float) instance).floatValue());
      }
    },
    INT {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        stream.writeInt(((Integer) instance).intValue());
      }
    },
    LONG {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        stream.writeLong(((Long) instance).longValue());
      }
    },
    OBJECT {
      void write(SyncClientSerializationStreamWriter stream, Object instance)
          throws SerializationException {
        stream.writeObject(instance);
      }
    },
    SHORT {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        stream.writeShort(((Short) instance).shortValue());
      }
    },
    STRING {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        stream.writeString((String) instance);
      }
    };

    abstract void write(SyncClientSerializationStreamWriter stream, Object instance)
        throws SerializationException;
  }

  /**
   * Enumeration used to provided typed vector writers.
   */
  private enum VectorWriter {
    BOOLEAN_VECTOR {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        boolean[] vector = (boolean[]) instance;
        stream.writeInt(vector.length);
        for (int i = 0, n = vector.length; i < n; ++i) {
          stream.writeBoolean(vector[i]);
        }
      }
    },
    BYTE_VECTOR {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        byte[] vector = (byte[]) instance;
        stream.writeInt(vector.length);
        for (int i = 0, n = vector.length; i < n; ++i) {
          stream.writeByte(vector[i]);
        }
      }
    },
    CHAR_VECTOR {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        char[] vector = (char[]) instance;
        stream.writeInt(vector.length);
        for (int i = 0, n = vector.length; i < n; ++i) {
          stream.writeChar(vector[i]);
        }
      }
    },
    DOUBLE_VECTOR {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        double[] vector = (double[]) instance;
        stream.writeInt(vector.length);
        for (int i = 0, n = vector.length; i < n; ++i) {
          stream.writeDouble(vector[i]);
        }
      }
    },
    FLOAT_VECTOR {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        float[] vector = (float[]) instance;
        stream.writeInt(vector.length);
        for (int i = 0, n = vector.length; i < n; ++i) {
          stream.writeFloat(vector[i]);
        }
      }
    },
    INT_VECTOR {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        int[] vector = (int[]) instance;
        stream.writeInt(vector.length);
        for (int i = 0, n = vector.length; i < n; ++i) {
          stream.writeInt(vector[i]);
        }
      }
    },
    LONG_VECTOR {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        long[] vector = (long[]) instance;
        stream.writeInt(vector.length);
        for (int i = 0, n = vector.length; i < n; ++i) {
          stream.writeLong(vector[i]);
        }
      }
    },
    OBJECT_VECTOR {
      void write(SyncClientSerializationStreamWriter stream, Object instance)
          throws SerializationException {
        Object[] vector = (Object[]) instance;
        stream.writeInt(vector.length);
        for (int i = 0, n = vector.length; i < n; ++i) {
          stream.writeObject(vector[i]);
        }
      }
    },
    SHORT_VECTOR {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        short[] vector = (short[]) instance;
        stream.writeInt(vector.length);
        for (int i = 0, n = vector.length; i < n; ++i) {
          stream.writeShort(vector[i]);
        }
      }
    },
    STRING_VECTOR {
      void write(SyncClientSerializationStreamWriter stream, Object instance) {
        String[] vector = (String[]) instance;
        stream.writeInt(vector.length);
        for (int i = 0, n = vector.length; i < n; ++i) {
          stream.writeString(vector[i]);
        }
      }
    };

    abstract void write(SyncClientSerializationStreamWriter stream, Object instance)
        throws SerializationException;
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
    CLASS_TO_VECTOR_WRITER.put(boolean[].class, SyncClientSerializationStreamWriter.VectorWriter.BOOLEAN_VECTOR);
    CLASS_TO_VECTOR_WRITER.put(byte[].class, SyncClientSerializationStreamWriter.VectorWriter.BYTE_VECTOR);
    CLASS_TO_VECTOR_WRITER.put(char[].class, SyncClientSerializationStreamWriter.VectorWriter.CHAR_VECTOR);
    CLASS_TO_VECTOR_WRITER.put(double[].class, SyncClientSerializationStreamWriter.VectorWriter.DOUBLE_VECTOR);
    CLASS_TO_VECTOR_WRITER.put(float[].class, SyncClientSerializationStreamWriter.VectorWriter.FLOAT_VECTOR);
    CLASS_TO_VECTOR_WRITER.put(int[].class, SyncClientSerializationStreamWriter.VectorWriter.INT_VECTOR);
    CLASS_TO_VECTOR_WRITER.put(long[].class, SyncClientSerializationStreamWriter.VectorWriter.LONG_VECTOR);
    CLASS_TO_VECTOR_WRITER.put(Object[].class, SyncClientSerializationStreamWriter.VectorWriter.OBJECT_VECTOR);
    CLASS_TO_VECTOR_WRITER.put(short[].class, SyncClientSerializationStreamWriter.VectorWriter.SHORT_VECTOR);
    CLASS_TO_VECTOR_WRITER.put(String[].class, SyncClientSerializationStreamWriter.VectorWriter.STRING_VECTOR);

    CLASS_TO_VALUE_WRITER.put(boolean.class, SyncClientSerializationStreamWriter.ValueWriter.BOOLEAN);
    CLASS_TO_VALUE_WRITER.put(byte.class, SyncClientSerializationStreamWriter.ValueWriter.BYTE);
    CLASS_TO_VALUE_WRITER.put(char.class, SyncClientSerializationStreamWriter.ValueWriter.CHAR);
    CLASS_TO_VALUE_WRITER.put(double.class, SyncClientSerializationStreamWriter.ValueWriter.DOUBLE);
    CLASS_TO_VALUE_WRITER.put(float.class, SyncClientSerializationStreamWriter.ValueWriter.FLOAT);
    CLASS_TO_VALUE_WRITER.put(int.class, SyncClientSerializationStreamWriter.ValueWriter.INT);
    CLASS_TO_VALUE_WRITER.put(long.class, SyncClientSerializationStreamWriter.ValueWriter.LONG);
    CLASS_TO_VALUE_WRITER.put(Object.class, SyncClientSerializationStreamWriter.ValueWriter.OBJECT);
    CLASS_TO_VALUE_WRITER.put(short.class, SyncClientSerializationStreamWriter.ValueWriter.SHORT);
    CLASS_TO_VALUE_WRITER.put(String.class, SyncClientSerializationStreamWriter.ValueWriter.STRING);
  }

  private static void append(StringBuffer sb, String token) {
    assert (token != null);
    sb.append(token);
    sb.append(RPC_SEPARATOR_CHAR);
  }

  /**
   * Returns the {@link Class} instance to use for serialization. Enumerations
   * are serialized as their declaring class while all others are serialized
   * using their true class instance.
   */
  private static Class<?> getClassForSerialization(Object instance) {
    assert (instance != null);

    if (instance instanceof Enum) {
      Enum<?> e = (Enum<?>) instance;
      return e.getDeclaringClass();
    } else {
      return instance.getClass();
    }
  }

  private StringBuffer encodeBuffer;
  
  private final String moduleBaseURL;

  private final String serializationPolicyStrongName;
  
  private SerializationPolicy serializationPolicy;

  private final Serializer serializer;

  public SyncClientSerializationStreamWriter(Serializer serializer,
      String moduleBaseURL, String serializationPolicyStrongName, SerializationPolicy serializationPolicy) {
    this.serializer = serializer;
    this.moduleBaseURL = moduleBaseURL;
    this.serializationPolicyStrongName = serializationPolicyStrongName;
    this.serializationPolicy = serializationPolicy;
  }

  @Override
  public void prepareToWrite() {
    super.prepareToWrite();
    encodeBuffer = new StringBuffer();

    // Write serialization policy info
    writeString(moduleBaseURL);
    writeString(serializationPolicyStrongName);
  }

  public void serializeValue(Object value, Class<?> type)
      throws SerializationException {
    ValueWriter valueWriter = CLASS_TO_VALUE_WRITER.get(type);
    if (valueWriter != null) {
      valueWriter.write(this, value);
    } else {
      // Arrays of primitive or reference types need to go through writeObject.
      SyncClientSerializationStreamWriter.ValueWriter.OBJECT.write(this, value);
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

  /**
   * @see ClientSerializationStreamWriter#writeLong
   * @param fieldValue
   */
  public void writeLong(long fieldValue) {
    if (getVersion() == SERIALIZATION_STREAM_MIN_VERSION) {
      double[] parts;
      parts = makeLongComponents((int) (fieldValue >> 32), (int) fieldValue);
      assert parts.length == 2;
      writeDouble(parts[0]);
      writeDouble(parts[1]);
    }else{
      append(Utils.toBase64(fieldValue));
    }
  }

  protected void append(String token) {
    append(encodeBuffer, token);
  }

  protected String getObjectTypeSignature(Object o) {
    Class<?> clazz = o.getClass();

    if (o instanceof Enum) {
      Enum<?> e = (Enum<?>) o;
      clazz = e.getDeclaringClass();
    }

    String typeName = clazz.getName();

    String serializationSignature = SerializabilityUtil.getSerializationSignature(clazz, serializationPolicy);
    if (serializationSignature != null) {
      typeName += "/" + serializationSignature;
    }
    return typeName;
  }

  protected void serialize(Object instance, String typeSignature) throws SerializationException{
    assert (instance != null);

    Class<?> clazz = getClassForSerialization(instance);

    serializationPolicy.validateSerialize(clazz);

    serializeImpl(instance, clazz);
  }

  private void writeHeader(StringBuffer buffer) {
    append(buffer, String.valueOf(getVersion()));
    append(buffer, String.valueOf(getFlags()));
  }

  private void writePayload(StringBuffer buffer) {
    buffer.append(encodeBuffer.toString());
  }

  private StringBuffer writeStringTable(StringBuffer buffer) {
    List<String> stringTable = getStringTable();
    append(buffer, String.valueOf(stringTable.size()));
    for (String s : stringTable) {
      append(buffer, quoteString(s));
    }
    return buffer;
  }
  
  private static String quoteString(String str){
    StringBuffer buffer = new StringBuffer();
    for (int i=0; i<str.length(); i++){
      char ch = str.charAt(i);
      switch (ch){
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
        if ((ch >= ' ') && (ch <= 127)){
          buffer.append(ch);
        }else{
          String hex = Integer.toHexString(ch);
          buffer.append("\\u0000".substring(0, 6-hex.length()) + hex);
        }
      }
    }
    
    return buffer.toString();
  }
  
  /**
   * @see com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter#serializeImpl
   * @param instance
   * @param instanceClass
   * @throws SerializationException
   */
  private void serializeImpl(Object instance, Class<?> instanceClass)
      throws SerializationException {
    assert (instance != null);
    
    Class<?> customSerializer = SerializabilityUtil.hasCustomFieldSerializer(instanceClass);
    if (customSerializer != null) {
      // Use custom field serializer
      serializeWithCustomSerializer(customSerializer, instance, instanceClass);
    } else if (instanceClass.isArray()) {
      serializeArray(instanceClass, instance);
    } else if (instanceClass.isEnum()) {
      writeInt(((Enum<?>) instance).ordinal());
    } else {
      // Regular class instance
      serializeClass(instance, instanceClass);
    }    
  }

  private void serializeWithCustomSerializer(Class<?> customSerializer,
      Object instance, Class<?> instanceClass) throws SerializationException {

    try {
      assert (!instanceClass.isArray());

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

  /**
   * Serialize an instance that is an array. Will default to serializing the
   * instance as an Object vector if the instance is not a vector of primitives,
   * Strings or Object.
   * 
   * @param instanceClass
   * @param instance
   * @throws SerializationException
   */
  private void serializeArray(Class<?> instanceClass, Object instance)
      throws SerializationException {
    assert (instanceClass.isArray());

    VectorWriter instanceWriter = CLASS_TO_VECTOR_WRITER.get(instanceClass);
    if (instanceWriter != null) {
      instanceWriter.write(this, instance);
    } else {
      SyncClientSerializationStreamWriter.VectorWriter.OBJECT_VECTOR.write(this, instance);
    }
  }

  private void serializeClass(Object instance, Class<?> instanceClass)
      throws SerializationException {
    assert (instance != null);

    Field[] serializableFields = SerializabilityUtil.applyFieldSerializationPolicy(instanceClass);
    for (Field declField : serializableFields) {
      assert (declField != null);

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
    if (serializationPolicy.shouldSerializeFields(superClass)) {
      serializeImpl(instance, superClass);
    }
  }
}
