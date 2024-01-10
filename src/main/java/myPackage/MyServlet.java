package myPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String api_key = "7297998881ec8bca9750033a1c07b691";
		String city = request.getParameter("city");
//		System.out.println(city);
//		doGet(request, response);
		String api_url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + api_key;
		
		//		API Integration
		URL url = new URL(api_url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		// 		Reading the data from Network
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		
		//		Store the data in StringBuilder	
		StringBuilder responseContent = new StringBuilder();
		
		//		To take input from reader
		Scanner scanner = new Scanner(reader);
		
		while(scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}
		scanner.close();
//		System.out.println(responseContent);
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(),JsonObject.class);
		System.out.println(jsonObject);
		
		long dateTimeStamp = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(dateTimeStamp).toString();
		
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatueCelcius = (int)(temperatureKelvin - 273.15);
		
		int humidity = jsonObject.getAsJsonObject("main").get("wind").getAsInt();
		
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", temperatueCelcius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
//		{"coord":{"lon":77.2311,"lat":28.6128},"weather":[{"id":741,"main":"Fog","description":"fog","icon":"50d"}],
//			"base":"stations","main":{"temp":283.24,"feels_like":282.92,"temp_min":283.24,"temp_max":283.24,
//			"pressure":1022,"humidity":100},"visibility":600,"wind":{"speed":1.54,"deg":320},"clouds":{"all":100},
//			"dt":1704774406,"sys":{"type":1,"id":9165,"country":"IN","sunrise":1704764694,"sunset":1704802219},
//			"timezone":19800,"id":1261481,"name":"New Delhi","cod":200}
	}

}
