package daily.y2016.m07.d05.headfirst.a1;

public class HeatIndexDisplay implements Observer, DisplayElement {

	float heatIndex = 0.0f;
	private WeatherData weatherData;
	
	public HeatIndexDisplay(WeatherData weatherData) {
		this.weatherData = weatherData;
		weatherData.registerObserver(this);
	}
	
	public void update(float t, float rh, float pressure) {
		heatIndex = computeHeatIndex(t, rh);
		display();
	}
	
	private float computeHeatIndex(float t, float rh) {
		float index = 16.923f;
		return index;
	}
	
	public void display() {
		System.out.println("Heat index is " + heatIndex);
	}
}
