package webcrawler;

import java.util.ArrayList;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebCrawler {
	
	private static WebCrawler single_instance = null;
	WebDriver myDriver;
	WebDriverWait wait;
	
	private WebCrawler() {
		System.setProperty("webdriver.chrome.driver", "C:\\facultate\\chromedriver.exe");
		initialize();
		
	}
	
	private void initialize() {
		myDriver = new ChromeDriver();
		wait = new WebDriverWait(myDriver, 10);
		
	}

	public static WebCrawler getInstance() {
		if(single_instance == null) {
			single_instance = new WebCrawler();
		}
		return single_instance;
	}
	
	public void startCrawling() throws InterruptedException {
		// IEEE Xplore
		//takeTheInformationsFromIeeeXplore("Radu", "Hobincu");
		// Scopus
		takeTheInformationsFromScopus("Radu", "Hobincu");
		// Science Direct
		takeTheInformationsFromScienceDirect("Fleur", "Tourneix");
		 
	}
	
	//"/.//" means "look under the selected element".
	private void takeTheInformationsFromIeeeXplore(String firstName, String lastName) throws InterruptedException {
		myDriver.get("https://ieeexplore.ieee.org/Xplore/home.jsp");
		System.out.println(myDriver.getTitle());
		Select select = new Select(myDriver.findElement(By.xpath("//select")));
		select.selectByVisibleText("Authors");
		//select.selectByValue("Authors");
		WebElement firstNameBox = myDriver.findElement(By.xpath("//input[@placeholder='First Name / Given Name']")); 
		firstNameBox.sendKeys("firstName");
		WebElement lastNameBox = myDriver.findElement(By.xpath("//input[@placeholder='Family Name / Last Name / Surname']")); 
		lastNameBox.sendKeys("lastName");
		WebElement searchButton = myDriver.findElement(By.className("search-icon"));
		searchButton.click();
		Thread.sleep(3000);
		List<WebElement> publications = myDriver.findElements(By.xpath("//*/h2/a"));
		String urlString = myDriver.getCurrentUrl();
		
		for(int i=0; i<publications.size(); i++) {
			if(publications.get(i).getText().equals("")) {
				publications.remove(i);
				i--;
			}
		}
		
		int size = publications.size();

		int k=0;
		for(int i = 0 ; i< size;i++) {
			List<WebElement> publications2 = myDriver.findElements(By.xpath("//*/h2/a"));
//			int publicationNumber = i+1;
//			String publicationXPath = "//*/h2/a[" + publicationNumber + "]";
//			WebElement publication = myDriver.findElement(By.xpath(publicationXPath));
			System.out.println("size :" + publications2.size());
			System.out.println("index :" +k);
			System.out.println(publications2.get(k).getText());
			String title = (publications2.get(k).getText());
			System.out.println(title);
			
			JavascriptExecutor executor = (JavascriptExecutor)myDriver;
			executor.executeScript("arguments[0].click();", (publications2.get(k)));
			Thread.sleep(2000);
			
			WebElement buttonForAllAuthors =  myDriver.findElement(By.id("authors-header"));
			executor.executeScript("arguments[0].click();", buttonForAllAuthors);
			
			List<String> authorsStringList = new ArrayList<>();
			int authorsNumber = myDriver.findElements(By.xpath("//div[@id='authors']/div")).size();
			
			for(int j=0; j<authorsNumber; j++) {
				int divNumber = j+1;
				String xPathString ="//section[@class='col-12 authors-tab document-tab']/div[" + divNumber + "]/xpl-author-item/div[1]/div[1]/div[1]/div[1]/a/span";
				authorsStringList.add(myDriver.findElement(By.xpath(xPathString)).getAttribute("innerHTML"));
			}
			for(String author : authorsStringList)
				System.out.println(author);
			
			String publisher = myDriver.findElement(By.xpath("//div[@class='u-pb-1 doc-abstract-publisher publisher-info-container black-tooltip']/span")).getAttribute("innerHTML");
			System.out.println(publisher);
			
			String location = myDriver.findElement(By.xpath("//div[@class='u-pb-1 doc-abstract-conferenceLoc']")).getText();
			location = location.replace("Conference Location:","").trim();
			System.out.println(location);
			
			String date = myDriver.findElement(By.xpath("//div[@class='u-pb-1 doc-abstract-confdate']")).getText();
			System.out.println(date);
			
			String abstractContent = myDriver.findElement(By.xpath("//div[@class='u-mb-1']")).getText();
			System.out.println(abstractContent);
			
//			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//			Date timestamp = new Date(System.currentTimeMillis());
//			System.out.println(formatter.format(timestamp));
//			Trebuie vorbit cu hobincu de timestamp!
			boolean isAvailable = false;
			if(null != myDriver.findElement(By.xpath("//div[@class='button layout-btn-blue']"))) {
				isAvailable = true;
				System.out.println("E disponibila pe site");
			}
			
			boolean isBook = true; //TREBUIE VORBIT CU HOBINCU
			
			String doi = myDriver.findElement(By.xpath("//div[@class='u-pb-1 stats-document-abstract-doi']/a")).getText();
			System.out.println(doi);
			
			
			
			
			myDriver.get(urlString);
			Thread.sleep(2000);
			k=k+2;
		}
			
		
	}
	private void takeTheInformationsFromScopus(String firstName, String lastName) {
		
	}
	private void takeTheInformationsFromScienceDirect(String firstName, String lastName) throws InterruptedException {
		myDriver.get("https://www.sciencedirect.com/");
		myDriver.findElement(By.xpath("//div[@class='input-container']/input[@aria-label='Authors']")).sendKeys(firstName + " " + lastName);;
		
		JavascriptExecutor executor = (JavascriptExecutor)myDriver;
		executor.executeScript("arguments[0].click();", (myDriver.findElement(By.xpath("//div[@id='aa-srp-search-submit-button']/button"))));
		
		Thread.sleep(1000);
		WebElement skipButton = myDriver.findElement(By.xpath("//button[@class='button modal-close-button button-anchor move-right move-top u-margin-s size-xs']"));
		if(skipButton != null)
			executor.executeScript("arguments[0].click();", skipButton);
		 
		String urlString = myDriver.getCurrentUrl();
		List<WebElement> publications = myDriver.findElements(By.xpath("//div[@class='result-item-content']/h2/span/a"));
		int size = publications.size();
		
		for(int k=0;k<size;k++) {
			List<WebElement> publications2 = myDriver.findElements(By.xpath("//div[@class='result-item-content']/h2/span/a"));
			String title = (publications2.get(k).getText());
			System.out.println(title);
			
			executor.executeScript("arguments[0].click();", (publications2.get(k)));
			Thread.sleep(2000);
			
			List<WebElement> givenNames = myDriver.findElements(By.xpath("//span[@class='text given-name']"));
			List<WebElement> surNames = myDriver.findElements(By.xpath("//span[@class='text surname']"));
			System.out.println(givenNames.size());
			System.out.println(surNames.size());
			
			List<String> authors = new ArrayList<String>();
			
			for(int i=0;i<givenNames.size();i++) {
				String authorName = givenNames.get(i).getText()+" "+surNames.get(i).getText();
				authors.add(authorName);
				System.out.println(authorName);
			}
			String publisher = myDriver.findElement(By.xpath("//a[@class='publication-title-link']")).getText();
			System.out.println(publisher);
			// anul (pana la a 2a virgula)
			System.out.println(myDriver.findElement(By.xpath("//div[@class='text-xs']")).getText());
			
			String doi = myDriver.findElement(By.xpath("//a[@class='doi']")).getText();
			System.out.println(doi);
			
			String abstractContent = myDriver.findElement(By.xpath("//div[@id='as0005']")).getText();
			System.out.println(abstractContent);
			
			myDriver.get(urlString);
			Thread.sleep(2000);
		}
}
	
}
