package expressoapp;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Searcher {
    private static int delay = 0;
    private static JavascriptExecutor js;
    private static String userSearch;
    private WebDriver driver;
    private ChromeOptions options;
    private ArrayList<Product> productsList;
    private int productsListCount = 0;
    private int startProductNumberSearch = 1;
    private int numberOfResults = 10;
}
