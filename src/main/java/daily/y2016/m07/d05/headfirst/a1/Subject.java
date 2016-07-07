package daily.y2016.m07.d05.headfirst.a1;

public interface Subject {

	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObservers();
}
