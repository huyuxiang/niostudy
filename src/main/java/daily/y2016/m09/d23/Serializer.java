package daily.y2016.m09.d23;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Serializer {

	private static ThreadLocal<Kryo> kryoThreadLocal;
	
	static {
		kryoThreadLocal = new ThreadLocal<Kryo>() {
			@Override
			public Kryo initialValue() {
				return new Kryo();
			}
		};
	}
	
	public static byte[] serialize(Object object) {
		if(object ==null) {
			throw new RuntimeException("object is null");
		}
		
		Kryo kryo = kryoThreadLocal.get();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Output output = new Output(bos);
		kryo.writeObject(output, object);
		output.close();
		
		return bos.toByteArray();
	}
	
	public static <T> T deserialize(byte[] data, Class<T> clazz) {
		if(data==null|| data.length ==0) {
			throw new RuntimeException("data is null");
		}
		
		Kryo kryo = kryoThreadLocal.get();
		InputStream stream = new BufferedInputStream(
				new ByteArrayInputStream(data));
		
		Input input = new Input(stream);
		T object = kryo.readObject(input, clazz);
		
		return object;
	}
}
