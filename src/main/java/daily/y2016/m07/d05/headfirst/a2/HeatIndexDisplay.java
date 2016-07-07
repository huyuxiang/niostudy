package daily.y2016.m07.d05.headfirst.a2;

public class HeatIndexDisplay implements Observer, DisplayElement {

	float heatIndex = 0.0f;
	
	public HeatIndexDisplay(Observable observable) {
		observable.addObserver(this);
	}
	
	public void update(Observable observable, Object arg) {
		if(observable instanceof WeatherData) {
			WeatherData weatherData = (WeatherData) observable;
			float t = weatherData.getTemperature();
			float rh = weatherData.getHumidity();
			heatIndex = (float)(16.923f);
			
		}
	}
	
	public void display() {
		System.out.println("Heat index is " + heatIndex);
	}
}
