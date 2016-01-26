<<<<<<< HEAD
package rpcdemo.rpc.api.benchmark.dataobject;

import java.io.Serializable;

public class Person implements Serializable {
	
	String personId;
	String loginName;
	PersonStatus status;
	PersonInfo info;
	byte[] attachment;
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public PersonStatus getStatus() {
		return status;
	}
	public void setStatus(PersonStatus status) {
		this.status = status;
	}
	public PersonInfo getInfo() {
		return info;
	}
	public void setInfo(PersonInfo info) {
		this.info = info;
	}
	public byte[] getAttachment() {
		return attachment;
	}
	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
}
=======
package rpcdemo.rpc.api.benchmark.dataobject;

import java.io.Serializable;

public class Person implements Serializable {
	
	String personId;
	String loginName;
	PersonStatus status;
	PersonInfo info;
	byte[] attachment;
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public PersonStatus getStatus() {
		return status;
	}
	public void setStatus(PersonStatus status) {
		this.status = status;
	}
	public PersonInfo getInfo() {
		return info;
	}
	public void setInfo(PersonInfo info) {
		this.info = info;
	}
	public byte[] getAttachment() {
		return attachment;
	}
	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
}
>>>>>>> origin/master
