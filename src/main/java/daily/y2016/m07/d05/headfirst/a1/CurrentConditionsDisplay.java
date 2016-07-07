package daily.y2016.m07.d05.headfirst.a1;

public class CurrentConditionsDisplay implements Observer,DisplayElement {

	private float temperature;
	private float humidity;
	private Subject weatherData;
	
	public CurrentConditionsDisplay(Subject weatherData) {
		this.weatherData = weatherData;
		weatherData.registerObserver(this);
	}
	
	public void update(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		display();
	}
	
	public void display(){
		System.out.println("Current conditions:" + temperature
				+ "F degress and " + humidity + "% humidity");
	}
}
