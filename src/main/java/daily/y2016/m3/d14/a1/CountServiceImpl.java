package daily.y2016.m3.d14.a1;

public class CountServiceImpl implements CountService {
	
	private int count = 0;
	
	public int count() {
		return count++;
	}
}
