package daily.y2016.m07.d05.headfirst.a2;

public class WeatherStation {

	public static void main(String[] args) {
		WeatherData weatherData = new WeatherData();
		
		CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherData);
	}
}
