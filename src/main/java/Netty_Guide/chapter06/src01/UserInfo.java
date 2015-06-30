package Netty_Guide.chapter06.src01;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author 201407280166
 *
 */
public class UserInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String userName;
	
	private int userID;
	private int age;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public UserInfo buildUserName(String userName) {
		this.userName = userName;
		return this;
	}
	
	public UserInfo buildUserID(int userID) {
		this.userID = userID;
		return this;
	}
	
	
	public byte[] codeC() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] value = this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.userID);
		buffer.flip();
		value = null;
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}
	
	public byte[] codeC(ByteBuffer buffer) {
		buffer.clear();
		byte[] value = this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.userID);
		buffer.flip();
		value = null;
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	
}
