package douglea.test3;

import java.util.Date;

public class TT {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String agreementCode = "";
		/*String uuid = StringUtil.getUUID().substring(0, 13);
		agreementCode = "881" + (new Date().getTime() + "").substring(8) + ((int) (Math.random() * 10000));*/
		String uuid = Math.abs(StringUtil.getUUID().hashCode())+"";
		if(uuid.length()<6) {
			agreementCode = (new Date().getTime() + "").substring(8 ,13) + randomStr(4);
		} else {
			agreementCode = uuid.substring(0, 6) + randomStr(3);
		}
		
		
		agreementCode = "881" + agreementCode;
		
		print(new Date().getTime());
		print((new Date().getTime()+"").substring(8,13));
		print((int)(Math.random() * 10));
		
		print((int) (Math.random() * 1000));
		print(uuid.length());
		print(uuid.substring(0, 6));
		print("881"+(new Date().getTime() + "").substring(8 ,13) + randomStr(4));
		print(agreementCode);
		print(agreementCode.length());
		
	}
	
	private static String randomStr(int length) {
		String temp = "";
		for(int i=1;i<=length;i++) {
			temp += randomNum();
		}
		return temp;
	}
	private static String randomNum() {
		return ((int)(Math.random()*10))+"";
	}
	
	
	public static void print(Object o) {
		System.out.println(o);
	}

}