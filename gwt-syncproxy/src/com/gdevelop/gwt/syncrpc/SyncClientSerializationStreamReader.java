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


import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.AbstractSerializationStreamReader;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.SerializabilityUtil;
import com.google.gwt.user.server.rpc.impl.SerializedInstanceReference;

import java.io.BufferedReader;

import java.io.FileReader;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @see com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter
 * @see com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader
 * @see com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter
 * @see com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader
 */
public class SyncClientSerializationStreamReader extends AbstractSerializationStreamReader{
  private static final char JS_ESCAPE_CHAR = '\\';
  
  /**
   * Used to accumulate elements while deserializing array types. The generic
   * type of the BoundedList will vary from the component type of the array it
   * is intended to create when the array is of a primitive type.
   * 
   * @param <T> The type of object used to hold the data in the buffer
   */
  private static class BoundedList<T> extends LinkedList<T> {
    private final Class<?> componentType;
    private final int expectedSize;

    public BoundedList(Class<?> componentType, int expectedSize) {
      this.componentType = componentType;
      this.expectedSize = expectedSize;
    }

    @Override
    public boolean add(T o) {
      assert size() < getExpectedSize();
      return super.add(o);
    }

    public Class<?> getComponentType() {
      return componentType;
    }

    public int getExpectedSize() {
      return expectedSize;
    }
  }

  /**
   * Enumeration used to provided typed instance readers.
   */
  private enum ValueReader {
    BOOLEAN {
      Object readValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readBoolean();
      }
    },
    BYTE {
      Object readValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readByte();
      }
    },
    CHAR {
      Object readValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readChar();
      }
    },
    DOUBLE {
      Object readValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readDouble();
      }
    },
    FLOAT {
      Object readValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readFloat();
      }
    },
    INT {
      Object readValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readInt();
      }
    },
    LONG {
      Object readValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readLong();
      }
    },
    OBJECT {
      Object readValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readObject();
      }
    },
    SHORT {
      Object readValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readShort();
      }
    },
    STRING {
      Object readValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readString();
      }
    };

    abstract Object readValue(SyncClientSerializationStreamReader stream)
        throws SerializationException;
  }

  /**
   * Enumeration used to provided typed instance readers for vectors.
   */
  private enum VectorReader {
    BOOLEAN_VECTOR {
      protected Object readSingleValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readBoolean();
      }

      protected void setSingleValue(Object array, int index, Object value) {
        Array.setBoolean(array, index, (Boolean) value);
      }
    },
    BYTE_VECTOR {
      protected Object readSingleValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readByte();
      }

      protected void setSingleValue(Object array, int index, Object value) {
        Array.setByte(array, index, (Byte) value);
      }
    },
    CHAR_VECTOR {
      protected Object readSingleValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readChar();
      }

      protected void setSingleValue(Object array, int index, Object value) {
        Array.setChar(array, index, (Character) value);
      }
    },
    DOUBLE_VECTOR {
      protected Object readSingleValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readDouble();
      }

      protected void setSingleValue(Object array, int index, Object value) {
        Array.setDouble(array, index, (Double) value);
      }
    },
    FLOAT_VECTOR {
      protected Object readSingleValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readFloat();
      }

      protected void setSingleValue(Object array, int index, Object value) {
        Array.setFloat(array, index, (Float) value);
      }
    },
    INT_VECTOR {
      protected Object readSingleValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readInt();
      }

      protected void setSingleValue(Object array, int index, Object value) {
        Array.setInt(array, index, (Integer) value);
      }
    },
    LONG_VECTOR {
      protected Object readSingleValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readLong();
      }

      protected void setSingleValue(Object array, int index, Object value) {
        Array.setLong(array, index, (Long) value);
      }
    },
    OBJECT_VECTOR {
      protected Object readSingleValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readObject();
      }

      protected void setSingleValue(Object array, int index, Object value) {
        Array.set(array, index, value);
      }
    },
    SHORT_VECTOR {
      protected Object readSingleValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readShort();
      }

      protected void setSingleValue(Object array, int index, Object value) {
        Array.setShort(array, index, (Short) value);
      }
    },
    STRING_VECTOR {
      protected Object readSingleValue(SyncClientSerializationStreamReader stream)
          throws SerializationException {
        return stream.readString();
      }

      protected void setSingleValue(Object array, int index, Object value) {
        Array.set(array, index, value);
      }
    };

    protected abstract Object readSingleValue(
        SyncClientSerializationStreamReader stream) throws SerializationException;

    protected abstract void setSingleValue(Object array, int index, Object value);

    /**
     * Convert a BoundedList to an array of the correct type. This
     * implementation consumes the BoundedList.
     */
    protected Object toArray(Class<?> componentType, BoundedList<Object> buffer)
        throws SerializationException {
      if (buffer.getExpectedSize() != buffer.size()) {
        throw new SerializationException(
            "Inconsistent number of elements received. Received "
                + buffer.size() + " but expecting " + buffer.getExpectedSize());
      }

      Object arr = Array.newInstance(componentType, buffer.size());

      for (int i = 0, n = buffer.size(); i < n; i++) {
        setSingleValue(arr, i, buffer.removeFirst());
      }

      return arr;
    }

    Object read(SyncClientSerializationStreamReader stream,
        BoundedList<Object> instance) throws SerializationException {
      for (int i = 0, n = instance.getExpectedSize(); i < n; ++i) {
        instance.add(readSingleValue(stream));
      }

      return toArray(instance.getComponentType(), instance);
    }
  }

  /**
   * Map of {@link Class} objects to {@link ValueReader}s.
   */
  private static final Map<Class<?>, ValueReader> CLASS_TO_VALUE_READER = new IdentityHashMap<Class<?>, ValueReader>();

  /**
   * Map of {@link Class} objects to {@link VectorReader}s.
   */
  private static final Map<Class<?>, VectorReader> CLASS_TO_VECTOR_READER = new IdentityHashMap<Class<?>, VectorReader>();

  {
    CLASS_TO_VECTOR_READER.put(boolean[].class, SyncClientSerializationStreamReader.VectorReader.BOOLEAN_VECTOR);
    CLASS_TO_VECTOR_READER.put(byte[].class, SyncClientSerializationStreamReader.VectorReader.BYTE_VECTOR);
    CLASS_TO_VECTOR_READER.put(char[].class, SyncClientSerializationStreamReader.VectorReader.CHAR_VECTOR);
    CLASS_TO_VECTOR_READER.put(double[].class, SyncClientSerializationStreamReader.VectorReader.DOUBLE_VECTOR);
    CLASS_TO_VECTOR_READER.put(float[].class, SyncClientSerializationStreamReader.VectorReader.FLOAT_VECTOR);
    CLASS_TO_VECTOR_READER.put(int[].class, SyncClientSerializationStreamReader.VectorReader.INT_VECTOR);
    CLASS_TO_VECTOR_READER.put(long[].class, SyncClientSerializationStreamReader.VectorReader.LONG_VECTOR);
    CLASS_TO_VECTOR_READER.put(Object[].class, SyncClientSerializationStreamReader.VectorReader.OBJECT_VECTOR);
    CLASS_TO_VECTOR_READER.put(short[].class, SyncClientSerializationStreamReader.VectorReader.SHORT_VECTOR);
    CLASS_TO_VECTOR_READER.put(String[].class, SyncClientSerializationStreamReader.VectorReader.STRING_VECTOR);

    CLASS_TO_VALUE_READER.put(boolean.class, SyncClientSerializationStreamReader.ValueReader.BOOLEAN);
    CLASS_TO_VALUE_READER.put(byte.class, SyncClientSerializationStreamReader.ValueReader.BYTE);
    CLASS_TO_VALUE_READER.put(char.class, SyncClientSerializationStreamReader.ValueReader.CHAR);
    CLASS_TO_VALUE_READER.put(double.class, SyncClientSerializationStreamReader.ValueReader.DOUBLE);
    CLASS_TO_VALUE_READER.put(float.class, SyncClientSerializationStreamReader.ValueReader.FLOAT);
    CLASS_TO_VALUE_READER.put(int.class, SyncClientSerializationStreamReader.ValueReader.INT);
    CLASS_TO_VALUE_READER.put(long.class, SyncClientSerializationStreamReader.ValueReader.LONG);
    CLASS_TO_VALUE_READER.put(Object.class, SyncClientSerializationStreamReader.ValueReader.OBJECT);
    CLASS_TO_VALUE_READER.put(short.class, SyncClientSerializationStreamReader.ValueReader.SHORT);
    CLASS_TO_VALUE_READER.put(String.class, SyncClientSerializationStreamReader.ValueReader.STRING);
  }

  private List<String> results = new ArrayList<String>();
  private int index;
  
  private List<String> stringTable = new ArrayList<String>();
  
  private SerializationPolicy serializationPolicy;
  public SyncClientSerializationStreamReader(SerializationPolicy serializationPolicy) {
    this.serializationPolicy = serializationPolicy;
  }

  @Override
  public void prepareToRead(String encoded) throws SerializationException {
    encoded = deconcat(encoded);
    parse(encoded);
    index = results.size();
    super.prepareToRead(encoded);
    
    if ((getVersion() < SERIALIZATION_STREAM_MIN_VERSION) 
        || (getVersion() > SERIALIZATION_STREAM_VERSION)) {
      throw new IncompatibleRemoteServiceException("Expecting version between "
          + SERIALIZATION_STREAM_MIN_VERSION + " and "
          + SERIALIZATION_STREAM_VERSION + " from server, got " + getVersion()
          + ".");
    }
    
    buildStringTable();
  }

  protected Object deserialize(String typeSignature) throws SerializationException{
    Object instance = null;
    SerializedInstanceReference serializedInstRef = SerializabilityUtil.decodeSerializedInstanceReference(typeSignature);

    try {
      // Class<?> instanceClass = Class.forName(serializedInstRef.getName(),
      //    false, null);
      Class<?> instanceClass = Class.forName(serializedInstRef.getName());

      assert (serializationPolicy != null);

      try{
        serializationPolicy.validateDeserialize(instanceClass);
      }catch(SerializationException e){
        System.err.println("WARN: " + e.getMessage());
      }

      //TODO validateTypeVersions(instanceClass, serializedInstRef);

      Class<?> customSerializer = SerializabilityUtil.hasCustomFieldSerializer(instanceClass);

      int index = reserveDecodedObjectIndex();

      instance = instantiate(customSerializer, instanceClass);

      rememberDecodedObject(index, instance);

      Object replacement = deserializeImpl(customSerializer, instanceClass,
          instance);

      // It's possible that deserializing an object requires the original proxy
      // object to be replaced.
      if (instance != replacement) {
        rememberDecodedObject(index, replacement);
        instance = replacement;
      }

      return instance;

    } catch (ClassNotFoundException e) {
      throw new SerializationException(e);

    } catch (InstantiationException e) {
      throw new SerializationException(e);

    } catch (IllegalAccessException e) {
      throw new SerializationException(e);

    } catch (IllegalArgumentException e) {
      throw new SerializationException(e);

    } catch (InvocationTargetException e) {
      throw new SerializationException(e);

    } catch (NoSuchMethodException e) {
      throw new SerializationException(e);
    }
  }
  
  protected String getString(int index) {
    if (index == 0) {
      return null;
    }
    // index is 1-based
    assert (index > 0);
    assert (index <= stringTable.size());

    // index is 1-based
    return this.stringTable.get(index-1);
  }

  public boolean readBoolean() {
    return !results.get(--index).equals("0");
  }

  public byte readByte() {
    return Byte.parseByte(results.get(--index));
  }

  public char readChar() {
    return (char)Integer.parseInt(results.get(--index));
  }

  public double readDouble() {
    return Double.parseDouble(results.get(--index));
  }

  public float readFloat() {
    return Float.parseFloat(results.get(--index));
  }

  public int readInt() {
    return Integer.parseInt(results.get(--index));
  }

  public long readLong() {
    if (getVersion() == SERIALIZATION_STREAM_MIN_VERSION) {
      return (long) readDouble() + (long) readDouble();
    }else{
      String s = results.get(--index);
      // remove quotes
      s = s.substring(1, s.length() - 1);
      return Utils.longFromBase64(s);
    }
  }

  public short readShort() {
    return Short.parseShort(results.get(--index));
  }

  public String readString() {
    return getString(readInt());
  }
  
  /**
   * Parse response from GWT RPC
   * example: [3,23456,0,2,0,0,0,1,1,["dab.rpp.client.Person/1455343364","My dad name","GWT User"],0,5]
   * @param encoded
   */
  private void parse(String encoded){
//    encoded = encoded.substring(1, encoded.length()-1);
    if (encoded.endsWith("]")){
      encoded = encoded.substring(1, encoded.length()-1);
    }else{
      encoded = encoded.substring(1);
    }
    StringBuffer token = new StringBuffer();
    for (int i=0; i<encoded.length(); i++){
      char ch = encoded.charAt(i);
      if (ch == ','){
        results.add(token.toString());
        token = new StringBuffer();
        continue;
      }
      if (ch == '['){
        int pos = encoded.lastIndexOf(']');
        if (pos < 0){
          // TODO: throw exeption
        }
        results.add(encoded.substring(i+1, pos));
        i = pos+1;
        continue;
      }
      token.append(ch);
    }
    if (token.length() > 0){
      results.add(token.toString());
    }
  }
  
  private static final String PRELUDE = "].concat([";
  private static final String POSTLUDE1 = "],[";
  private static final String POSTLUDE = "])";
  private String deconcat(String encoded) {
    int start = encoded.indexOf(PRELUDE);
    if (start > 0){
      StringBuffer ret = new StringBuffer(encoded.length() - PRELUDE.length());
      ret.append(encoded.substring(0, start));
      
      start += PRELUDE.length();
      int end = encoded.indexOf(POSTLUDE1, start);
      while (end > 0){
        ret.append(",");
        ret.append(encoded.substring(start, end));
        
        start = end + POSTLUDE1.length();
        end = encoded.indexOf(POSTLUDE1, start);
      }

      end = encoded.indexOf(POSTLUDE, start);
      if (end > 0){
        ret.append(",");
        ret.append(encoded.substring(start, end+1));
        return ret.toString();
      }
    }
    
    return encoded;
  }
  
  private Object instantiate(Class<?> customSerializer, Class<?> instanceClass)
      throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException,
      NoSuchMethodException, SerializationException {
    if (customSerializer != null) {
      for (Method method : customSerializer.getMethods()) {
        if ("instantiate".equals(method.getName())) {
          return method.invoke(null, this);
        }
      }
      // Ok to not have one.
    }

    if (instanceClass.isArray()) {
      int length = readInt();
      // We don't pre-allocate the array; this prevents an allocation attack
      return new BoundedList<Object>(instanceClass.getComponentType(), length);
    } else if (instanceClass.isEnum()) {
      Enum<?>[] enumConstants = (Enum[]) instanceClass.getEnumConstants();
      int ordinal = readInt();
      assert (ordinal >= 0 && ordinal < enumConstants.length);
      return enumConstants[ordinal];
    } else {
      Constructor<?> constructor = instanceClass.getDeclaredConstructor();
      constructor.setAccessible(true);
      return constructor.newInstance();
    }
  }

  private Object deserializeImpl(Class<?> customSerializer,
      Class<?> instanceClass, Object instance) throws NoSuchMethodException,
      IllegalArgumentException, IllegalAccessException,
      InvocationTargetException, SerializationException, ClassNotFoundException {

    if (customSerializer != null) {
      deserializeWithCustomFieldDeserializer(customSerializer, instanceClass,
          instance);
    } else if (instanceClass.isArray()) {
      instance = deserializeArray(instanceClass, instance);
    } else if (instanceClass.isEnum()) {
      // Enums are deserialized when they are instantiated
    } else {
      deserializeClass(instanceClass, instance);
    }

    return instance;
  }

  private void deserializeWithCustomFieldDeserializer(
      Class<?> customSerializer, Class<?> instanceClass, Object instance)
      throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException {
    assert (!instanceClass.isArray());

    for (Method method : customSerializer.getMethods()) {
      if ("deserialize".equals(method.getName())) {
        method.invoke(null, this, instance);
        return;
      }
    }
    throw new NoSuchMethodException("deserialize");
  }

  /**
   * Deserialize an instance that is an array. Will default to deserializing as
   * an Object vector if the instance is not a primitive vector.
   * 
   * @param instanceClass
   * @param instance
   * @throws SerializationException
   */
  @SuppressWarnings("unchecked")
  private Object deserializeArray(Class<?> instanceClass, Object instance)
      throws SerializationException {
    assert (instanceClass.isArray());

    BoundedList<Object> buffer = (BoundedList<Object>) instance;
    VectorReader instanceReader = CLASS_TO_VECTOR_READER.get(instanceClass);
    if (instanceReader != null) {
      return instanceReader.read(this, buffer);
    } else {
      return SyncClientSerializationStreamReader.VectorReader.OBJECT_VECTOR.read(this, buffer);
    }
  }

  private void deserializeClass(Class<?> instanceClass, Object instance)
      throws SerializationException, IllegalAccessException,
      NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
    Field[] serializableFields = SerializabilityUtil.applyFieldSerializationPolicy(instanceClass);

    for (Field declField : serializableFields) {
      assert (declField != null);

      Object value = deserializeValue(declField.getType());

      boolean isAccessible = declField.isAccessible();
      boolean needsAccessOverride = !isAccessible
          && !Modifier.isPublic(declField.getModifiers());
      if (needsAccessOverride) {
        // Override access restrictions
        declField.setAccessible(true);
      }

      declField.set(instance, value);
    }

    Class<?> superClass = instanceClass.getSuperclass();
    if (serializationPolicy.shouldDeserializeFields(superClass)) {
      deserializeImpl(SerializabilityUtil.hasCustomFieldSerializer(superClass),
          superClass, instance);
    }
  }

  public Object deserializeValue(Class<?> type) throws SerializationException {
    ValueReader valueReader = CLASS_TO_VALUE_READER.get(type);
    if (valueReader != null) {
      return valueReader.readValue(this);
    } else {
      // Arrays of primitive or reference types need to go through readObject.
      return SyncClientSerializationStreamReader.ValueReader.OBJECT.readValue(this);
    }
  }
  
  private void buildStringTable(){
    String raw = results.get(--index);
    byte b1; byte b2; byte b3; byte b4;
    
    boolean startNewString = true;
    StringBuffer buffer = new StringBuffer();
    for (int i=0; i<raw.length(); i++){
      char ch = raw.charAt(i);
      if (startNewString){
        assert ch == '\"';
        startNewString = false;
        continue;
      }
      if (ch == '\"'){  // end-of-string
        this.stringTable.add(buffer.toString());
        
        buffer.setLength(0);
        startNewString = true;
        
        if (i != raw.length()-1){
          assert raw.charAt(i+1) == ',';
          i++;
        }
        continue;
      }
      if (ch == JS_ESCAPE_CHAR){
        i++;
        ch = raw.charAt(i);
        switch(ch){
        case '0':   // \0
          buffer.append('\u0000');
          break;
        case 'b':   // \b
          buffer.append('\b');
          break;
        case 't':   // \t
          buffer.append('\t');
          break;
        case 'n':   // \n
          buffer.append('\n');
          break;
        case 'f':   // \f
          buffer.append('\f');
          break;
        case 'r':   // \r
          buffer.append('\r');
          break;
        case '\"':   // \"
          buffer.append('\"');
          break;
        case '\\':   // \\
          buffer.append('\\');
          break;
        case 'x':   // \\xNN
          b1 = hex2byte(raw.charAt(++i));
          b2 = hex2byte(raw.charAt(++i));
          ch = (char)(b1*16 + b2);
          buffer.append(ch);
          break;
        case 'u':   // \\uNNNN
          b1 = hex2byte(raw.charAt(++i));
          b2 = hex2byte(raw.charAt(++i));
          b3 = hex2byte(raw.charAt(++i));
          b4 = hex2byte(raw.charAt(++i));
          ch = (char)(b1*16*16*16 + b2*16*16 + b3*16 + b4);
          buffer.append(ch);
          break;
        default:
          // TODO:
          System.out.println("???");
        }
      }else{
        buffer.append(ch);
      }
    }
  }
  
  private byte hex2byte(char ch){
    if ((ch >= '0') && (ch <= '9')){
      return (byte)(ch-'0');
    }
    if ((ch >= 'A') && (ch <= 'F')){
      return (byte)(ch-'A'+10);
    }
    if ((ch >= 'a') && (ch <= 'f')){
      return (byte)(ch-'a'+10);
    }
    
    return -1;
  }
  
  public static void main(String[] args) throws Exception{
    BufferedReader reader = new BufferedReader(new FileReader("C:/temp/large.txt"));
    String encoded = reader.readLine();
    SyncClientSerializationStreamReader s = new SyncClientSerializationStreamReader(new RemoteServiceSyncProxy.DummySerializationPolicy());
    s.prepareToRead(encoded);
  }
}
