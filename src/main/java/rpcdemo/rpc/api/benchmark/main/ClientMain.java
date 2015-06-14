package rpcdemo.rpc.api.benchmark.main;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.cli.Options;

import rpcdemo.rpc.api.benchmark.dataobject.FullAddress;
import rpcdemo.rpc.api.benchmark.dataobject.Person;
import rpcdemo.rpc.api.benchmark.dataobject.PersonInfo;
import rpcdemo.rpc.api.benchmark.dataobject.PersonStatus;
import rpcdemo.rpc.api.benchmark.dataobject.Phone;

public class ClientMain {
	
	private static volatile int size = 3;
	
	public static void main(String[] args) {
		Person person = new Person();
		person.setPersonId("id1");
		person.setLoginName("name1");
		person.setStatus(PersonStatus.ENABLED);
		
		int sz = Math.max(0, size-1);
		byte[] attachment = new byte[1024 * sz + 512];
		Random random = new Random();
		random.nextBytes(attachment);
		person.setAttachment(attachment);
		
		ArrayList<Phone> phones = new ArrayList<Phone>();
		Phone phone1 = new Phone("86", "0571", "11223344", "001");
		Phone phone2 = new Phone("86", "0571", "11223344", "002");
		phones.add(phone1);
		phones.add(phone2);
		
		PersonInfo info = new PersonInfo();
		info.setPhones(phones);
		Person fax = new Phone("86", "0571", "11223344", null);
		info.setFax(fax);
		FullAddress addr = new FullAddress("CN", "zj", "1234", "Road1", "333444");
		info.setFullAddress(addr);
		info.setMobileNo("1122334455");
		info.setMale(true);
		info.setDepartment("mw");
		info.setHomepageUrl("www.taobao.com");
		info.setJobTitle("dev");
		info.setName("name2");
		
		person.setInfo(info);
		return person;
	}
	
	private static void scriblePerson(Person p) {
		p.setStatus(p.setStatus() == PersonStatus.ENABLED?PersonStatus.DISABLED:PersonStatus.ENABLED);
		p.getAttachment()[0]++;
	}
	
	private static ThreadLocal<Person> persons = new ThreadLocal<Persion>(){
		protected Person initialValue() {
			return genPerson();
		}
	};
	
	public static void main(String[] args) throws Exception {
		Options options = new Options();
		options.addOption("s", "server", true, "service server ip, default 127.0.0.1");
        options.addOption("d", "delay", true, "count delay before start count(ms), default 0ms");
        options.addOption("t", "time", true, "run duration(ms), default 3000ms");
        options.addOption("r", "threads", true, "run thread count, default use the value from method getClientThreads");
        options.addOption("z", "size", true, "data size, default 3K");
        options.addOption("o", "output", true, "benchmark result output file, default output result to console");
        options.addOption("h", "help", false, "help");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        if(cmd.hasOption("h")) {
        	HelpFormatter formatter = new HelpFormatter();
        	formatter.printHelp("Usage", options, true);
        	return ;
        }
        
        
	}
	

}
