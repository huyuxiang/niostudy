package daily.template.headfirst.ch11.a2;

public class CountServiceImpl implements CountService {

	private int count = 0;
	
	@Override
	public int count() {
		return count++;
	}

}
