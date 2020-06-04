package main;

import database.DatabaseManager;
import webcrawler.WebCrawler;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		DatabaseManager.getInstance().checkForConnection();
		WebCrawler.getInstance().startCrawling();

	}

}
