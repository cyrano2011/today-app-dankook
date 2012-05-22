package com.TODAY.XMLParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAXHandler for Google Weather API
 */
public class WeatherHandler extends DefaultHandler {

	private WeatherSet myWeatherSet = null;
	private boolean in_forecast_information = false;
	private boolean in_current_conditions = false;
	private boolean in_forecast_conditions = false;

	private boolean usingSITemperature = false;

	public WeatherSet getWeatherSet() {
		return this.myWeatherSet;
	}

	public void startDocument() throws SAXException {
		this.myWeatherSet = new WeatherSet();
	}

	public void endDocument() throws SAXException {
		// Do nothing
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) 
		throws SAXException 
	{

		if (localName.equals("forecast_information")) {
			this.in_forecast_information = true;
		} else if (localName.equals("current_conditions")) {
			this.myWeatherSet.setWeatherCurrentCondition(new WeatherCurrentCondition());
			this.in_current_conditions = true;
		} else if (localName.equals("forecast_conditions")) {
			this.myWeatherSet.getWeatherForecastConditions().add(new WeatherForecastCondition());
			this.in_forecast_conditions = true;
		} else {
			String dataAttribute = atts.getValue("data");

			if (localName.equals("city")) {
			} else if (localName.equals("postal_code")) {
			} else if (localName.equals("latitude_e6")) {

			} else if (localName.equals("longitude_e6")) {

			} else if (localName.equals("forecast_date")) {
			} else if (localName.equals("current_date_time")) {
			} else if (localName.equals("unit_system")) {
				if (dataAttribute.equals("SI"))
					this.usingSITemperature = true;
			} else if (localName.equals("day_of_week")) {
				if (this.in_current_conditions) {
					this.myWeatherSet.getWeatherCurrentCondition().setDayofWeek(dataAttribute);
				} else if (this.in_forecast_conditions) {
					this.myWeatherSet.getLastWeatherForecastCondition().setDayofWeek(dataAttribute);
				}
			} else if (localName.equals("icon")) {
				if (this.in_current_conditions) {
					this.myWeatherSet.getWeatherCurrentCondition().setIconURL(dataAttribute);
				} else if (this.in_forecast_conditions) {
					this.myWeatherSet.getLastWeatherForecastCondition().setIconURL(dataAttribute);
				}
			} else if (localName.equals("condition")) {
				if (this.in_current_conditions) {
					this.myWeatherSet.getWeatherCurrentCondition().setCondition(dataAttribute);
				} else if (this.in_forecast_conditions) {
					this.myWeatherSet.getLastWeatherForecastCondition().setCondition(dataAttribute);
				}
			} else if (localName.equals("temp_f")) {
				this.myWeatherSet.getWeatherCurrentCondition()
						.setTempFahrenheit(Integer.parseInt(dataAttribute));
			} else if (localName.equals("temp_c")) {
				this.myWeatherSet.getWeatherCurrentCondition().setTempCelcius(
						Integer.parseInt(dataAttribute));
			} else if (localName.equals("humidity")) {
				this.myWeatherSet.getWeatherCurrentCondition().setHumidity(dataAttribute);
			} else if (localName.equals("wind_condition")) {
				this.myWeatherSet.getWeatherCurrentCondition().setWindCondition(dataAttribute);
			} else if (localName.equals("low")) {
				int temp = Integer.parseInt(dataAttribute);
				if (this.usingSITemperature) {
					this.myWeatherSet.getLastWeatherForecastCondition().setTempMinCelsius(temp);
				} else {
					this.myWeatherSet.getLastWeatherForecastCondition().setTempMinCelsius(fahrenheitToCelsius(temp));
				}
			} else if (localName.equals("high")) {
				int temp = Integer.parseInt(dataAttribute);
				if (this.usingSITemperature) {
					this.myWeatherSet.getLastWeatherForecastCondition().setTempMaxCelsius(temp);
				} else {
					this.myWeatherSet.getLastWeatherForecastCondition().setTempMaxCelsius(fahrenheitToCelsius(temp));
				}
			}
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
		throws SAXException 
	{
		if (localName.equals("forecast_information")) {
			this.in_forecast_information = false;
		} else if (localName.equals("current_conditions")) {
			this.in_current_conditions = false;
		} else if (localName.equals("forecast_conditions")) {
			this.in_forecast_conditions = false;
		}
	}

	public void characters(char ch[], int start, int length) {
 
	}
	
	public static int fahrenheitToCelsius(int tFahrenheit) {
		return (int) ((5.0f / 9.0f) * (tFahrenheit - 32));
	}

	public static int celsiusToFahrenheit(int tCelsius) {
		return (int) ((9.0f / 5.0f) * tCelsius + 32);
	}
	
}