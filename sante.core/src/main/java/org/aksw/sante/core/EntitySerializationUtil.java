package org.aksw.sante.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class EntitySerializationUtil {
	public static <T> byte[] write(T object) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();){
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(object);
			byte[] byteArray = bos.toByteArray();
			return byteArray;
		}
	}

	public static <T> T read(byte[] entity, Class<T> t) throws Exception {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(entity);){
			ObjectInput in = new ObjectInputStream(bis);
			T e = t.cast(in.readObject());
			return e;
		}
	}
}
