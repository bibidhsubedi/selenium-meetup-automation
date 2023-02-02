import java.time.Duration;
import java.io.FileWriter;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MeetupEvent {

	private static WebDriver driver;
	private static String eventName;
	private static String hostName;
	private static String eventTime;
	private static String eventLocation;

	public static void main(String[] args) {
		launchBrowser();
		goToMeetup();
		enterKeyword("tennis");
		enterLocation("Brisbane");
		pressSearch();
		waitForFiveSeconds();
		selectEvent();
		captureEventDetails();
		writeEventDetailsToFile();
		waitForFiveSeconds();
		closeBrowser();
	}

	private static void launchBrowser() {
		// Set the path to the ChromeDriver executable
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\bibid\\Desktop\\Selenium Drivers\\chromedriver_win32\\chromedriver.exe");
		// Launch the GoogleChrome browser
		driver = new ChromeDriver();
	}

	private static void goToMeetup() {
		// Go to Meetup.com
		driver.get("https://www.meetup.com/");
	}

	private static void enterLocation(String location) {
		// Input a location
		driver.findElement(By.id("location-typeahead-searchLocation")).sendKeys(location);

		// Wait for the location list to load
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='listbox']")));

		// Press the down arrow key once
		driver.findElement(By.id("location-typeahead-searchLocation")).sendKeys(Keys.ARROW_DOWN);

		// Press the enter key
		driver.findElement(By.id("location-typeahead-searchLocation")).sendKeys(Keys.RETURN);
	}

	private static void enterKeyword(String keyword) {
		// Locate the search bar
		driver.findElement(By.id("search-keyword-input")).sendKeys(keyword);
	}

	private static void pressSearch() {
		// Click the Search button
		driver.findElement(By.xpath("//*[@id='page']/div[2]/main/div[1]/div[4]/div[1]/form/div[2]/input")).click();
	}

	private static void selectEvent() {
		// Select the first event
		driver.findElement(By.xpath("//h2[@class='text-gray7 font-medium text-base pt-0 pb-1 line-clamp-3']")).click();
	}

	private static void captureEventDetails() {
		// Capture the event details
		eventName = driver
				.findElement(By.cssSelector(".overflow-ellipsis.overflow-hidden.text-3xl.font-bold.leading-snug"))
				.getText();
		hostName = driver.findElement(By.cssSelector(".font-medium")).getText();
		eventTime = driver.findElement(By.cssSelector("time")).getText();
		eventLocation = driver.findElement(By.cssSelector("a[data-testid='venue-name-link']")).getText();
	}

	private static void writeEventDetailsToFile() {
		try {
			FileWriter fileWriter = new FileWriter("event_details.txt");
			fileWriter.write("Event Name: " + eventName + System.lineSeparator());
			fileWriter.write("Event Host: " + hostName + System.lineSeparator());
			fileWriter.write("Event Time: " + eventTime + System.lineSeparator());
			fileWriter.write("Event Location: " + eventLocation + System.lineSeparator());
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error writing to file: " + e.getMessage());
		}
	}

	private static void waitForFiveSeconds() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void closeBrowser() {
		driver.quit();
	}

}
