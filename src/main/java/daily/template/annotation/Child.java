package daily.template.annotation;

import javax.management.DescriptorKey;

@Description(desc="这是类注解", author="ashuo", age=23)
public class Child implements Person {
	
	@Override
	@Description(desc="这是方法注解", author="ashuo", age=23)
	public String name() {
		return null;
	}
	
	@Override
	public int age() {
		return 0;
	}
	
	@Override
	public void sing() {
		
	}
}
