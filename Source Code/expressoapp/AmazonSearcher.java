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
import java.util.List;

public class AmazonSearcher {
    private static int delay = 0;
    private static JavascriptExecutor js;
    private WebDriver driver;
    private ChromeOptions options;
    private ArrayList<Product> productsList;
    private int productsListCount = 0;
    private int startProductNumberSearch;
    private int numberOfResults = 10;
    private static int maxErrorChecking = 4;

    public AmazonSearcher(){
        this(1,10);
    }

    public AmazonSearcher(int startProductNumberSearch, int numberOfResults){
        this.options = new ChromeOptions();
        this.configureChromeOptions();
        this.driver = new ChromeDriver(options);
        this.setDriverDimensions(1920, 1080);
        this.productsList = new ArrayList<>();
        AmazonSearcher.js = ((JavascriptExecutor) driver);
        this.startProductNumberSearch = startProductNumberSearch;
        this.numberOfResults = numberOfResults;
    }

    private void configureChromeOptions(){
        this.options.addArguments("disable-notifications");
        this.options.addArguments("--disable-infobars");
        this.options.addArguments("start-maximized");
        this.options.addArguments("disable-infobars");
        // this.options.setHeadless(true);
    }

    public WebDriver getDriver(){
        return this.driver;
    }

    public void setDriver(WebDriver driver){
        this.driver = driver;
    }

    public ChromeOptions getChromeOptions(){
        return this.options;
    }

    public void setChromeOptions(ChromeOptions options){
        this.options = options;
    }

    public ArrayList<Product> getProductsList(){
        return this.productsList;
    }

    public void setProductsList(ArrayList<Product> productsList){
        this.productsList = productsList;
    }

    public static int getDelay(){
        return AmazonSearcher.delay;
    }

    public void setDelay(int delay){
        AmazonSearcher.delay = delay;
    }

    public int getProductsListCount(){
        return this.productsListCount;
    }

    public void setProductsListCount(int productsListCount){
        this.productsListCount = productsListCount;
    }
    
    public void setDriverDimensions(int width, int height){
        Dimension driverDimensions = new Dimension(width, height);
        this.driver.manage().window().setSize(driverDimensions);
        this.driver.manage().window().maximize();
    }

    public int getstartProductSearchNumber(){
        return this.startProductNumberSearch;
    }

    public void setstartProductSearchNumber(int startProductNumberSearch){
        this.startProductNumberSearch = startProductNumberSearch;
    }

    public int getNumberOfResults(){
        return this.numberOfResults;
    }

    public void setNumberOfResults(int numberOfResults){
        this.numberOfResults = numberOfResults;
    }

    // public static String getSearch(){
    //     return LazadaSearcher.userSearch;
    // }
    
    // public static void setSearch(String userSearch){
    //     LazadaSearcher.userSearch = userSearch;
    // }

    public void search(String userSearch) throws IOException,InterruptedException{
        
        WebElement productElement;
        String xpathString;
        String productName, productLink, productImageLink, productPriceRange;

        int currentErrorCount = 0;
        int initialScroll;
        int currentPage = 1, totalPage = 0, resultsCounter = 0, totalResults = 0;
        int amazonProductGap = 0;
        int productDiv = this.startProductNumberSearch;

        this.driver.get("https://www.amazon.com/");

        Thread.sleep(1000);

        // Looks for the search bar in amazon website
        WebElement searchBar = driver.findElement(By.xpath("//*[@id=\"twotabsearchtextbox\"]"));
        searchBar.sendKeys(userSearch + "\n");

        Thread.sleep(1000);

        // currentPage = this.retrieveCurrentPage(driver);
        // totalPage = this.retrievetotalPage(driver);

        // Amazon product div start : 2
        // Amazon product div gap : div 7-9
        // Amazon product div gap resume: div 10
        // Amazon product div stop/end : 26

        // Page 1
        //  //*[@id="search"]/div[1]/div/div[1]/div/span[3]/div[2]/div[2]
        // //*[@id="search"]/div[1]/div/div[1]/div/span[3]/div[2]/div[20]

        // Page 2
        // Amazon start page number: //*[@id="search"]/div[1]/div/div[1]/div/span[3]/div[2]/div[1]
        // Amazon stop page number: //*[@id="search"]/div[1]/div/div[1]/div/span[3]/div[2]/div[22]

        // Page 3
        // //*[@id="search"]/div[1]/div/div[1]/div/span[3]/div[2]/div[1]
        // //*[@id="search"]/div[1]/div/div[1]/div/span[3]/div[2]/div[22]

        //  //*[@id="search"]/div[1]/div/div[1]/div/span[3]/div[2]/div[18]
        
        totalResults = this.retrieveTotalResults(driver);
        totalPage = totalResults / 15;
        System.out.println(totalResults);

        // if(this.startProductNumberSearch>10){
        //     initialScroll = (this.startProductNumberSearch / 10) * 2;
        //     int i = 0 ;
        //     while(i < initialScroll){
        //         AmazonSearcher.js.executeScript("window.scrollBy(0,1000)", "");
        //         // Thread.sleep(1000);
        //         System.out.println("Initial Scroll executed!");
        //         i++;
        //     }
        //     this.takeScreenshot("amazonScreenShot1");
        // }

        AmazonSearcher.js.executeScript("window.scrollBy(0,500)", "");

        while(resultsCounter < this.numberOfResults){
            Product newProduct = new Product();

        // if(productDiv >= 27){
        //     if(currentPage >= totalPage){
        //         break;
        //     }// inner if
            
        //     WebElement nextButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div/div[1]/div[3]/div/ul/li[9]/button"));
        //     nextButton.click();
        //     currentPage ++;
        //     Thread.sleep(1000);
        //     productDiv = 1;
        //     this.startProductNumberSearch = 1;
        //     System.out.println(String.format("\nPage %d of %d\n",currentPage,totalPage));
        // }//outer if


            xpathString = String.format("//*[@id=\"search\"]/div[1]/div/div[1]/div/span[3]/div[2]/div[%d]",productDiv);

            if(this.productExists(xpathString) == false){
                System.out.println(String.format("Product div[%d] does not exist",productDiv));
                
                if(currentErrorCount >= AmazonSearcher.maxErrorChecking){
                    break;
                }
                currentErrorCount++;
                continue;
            }// product exists checker
            
            productElement = driver.findElement(By.xpath(xpathString));
            
            newProduct = this.retrieveProductInfo(xpathString, productElement);
            
            System.out.println(String.format("Product div [%d]",productDiv));
            System.out.println("Product Link: " + newProduct.getProductLink());
            System.out.println("Product Image Link: " + newProduct.getProductImageLink());
            System.out.println("Product Name: " + newProduct.getProductName());
            System.out.println("Product Price: " + newProduct.getProductPrice());
            System.out.println("\n");

            if(resultsCounter > 2){
                AmazonSearcher.js.executeScript("window.scrollBy(0,1080)", "");
                Thread.sleep(2000);
                System.out.println("JScript executed");
            }//if statement 2

            if(newProduct.getProductLink() != null){
                resultsCounter++;
            }

            // this.takeScreenshot(String.format("amazonP%d",productDiv));
            productDiv++;
            if(productDiv >=5){
                break;
            }
            // break;
        }// while loop

        this.takeScreenshot("amazonScreenShot2");
        this.driver.quit();
    }// search function

    private boolean productExists(String xpathString){
        boolean productExists;
        try{
            productExists = this.driver.findElement(By.xpath(xpathString)).isDisplayed() ? true : false;
        }catch(Exception e){
            System.out.println("Product does not exist!");
            AmazonSearcher.js.executeScript("window.scrollBy(0,500)", "");
            System.out.println("Javascript Executed!");
            return false;
        }

        return true;
    }

    //RETTRIEVAL OF PRODUCT ELEMENTS

    private Product retrieveProductInfo(String xpathString,WebElement productElement) throws InterruptedException{
        String productLink;
        String productName;
        String productImageLink;
        String productPriceRange;

        Product newProduct = new Product();

        try{
            productLink = productElement.findElement(By.xpath(xpathString + "/div/span/div/div/div/div/div[2]/div[1]/div/div/span/a")).getAttribute("href");;
        }catch(Exception e){
            productLink = null;
        }

        // //*[@id="search"]/div[1]/div/div[1]/div/span[3]/div[2]/div[2]/div/span/div/div/div/div/div[2]/div[2]/div/div/div[1]/h2/a
        // /div/span/div/div/div/div/div[2]/div[2]/div/div/div[1]/h2/a
        try{
            productLink = productElement.findElement(By.xpath(xpathString + "/div/span/div/div/div/div/div[2]/div[2]/div/div/div[1]/h2/a")).getAttribute("href");
        }catch(Exception e){
            productLink = null;
        }

        try{
            // //*[@id=\"search\"]/div[1]/div/div[1]/div/span[3]/div[2]/div[%d]
            // //*[@id="search"]/div[1]/div/div[1]/div/span[3]/div[2]/div[2]/div/span/div/div/div/div/span/a/div/img
            productImageLink = productElement.findElement(By.xpath(xpathString + "/div/span/div/div/div/div/div[2]/div[1]/div/div/span/a/div/img")).getAttribute("src");
        }catch(Exception e){
            productImageLink = null;
        }

        try{
            productName = productElement.findElement(By.xpath(xpathString + "/div/span/div/div/div/div/div[2]/div[2]/div/div[1]/h2/a/span")).getText();
        }catch(Exception e){
            System.out.println("Product Name Error!");
            productName = null;
        }
        // /div/span/div/div/div/div/div[2]/div[2]/div/div/div[1]/h2/a/span/

        try{
            if(productName == null){
                productName = productElement.findElement(By.xpath(xpathString + "/div/span/div/div/div[2]/div[2]/div/div[1]/h2/a/span")).getText();
            }
        }catch(Exception e){
            System.out.println("Product Name Error! 2");
            productName = null;
        }

        try{
            productPriceRange = productElement.findElement(By.xpath(xpathString + "/div/span/div/div/div/div/div[2]/div[2]/div/div[3]/div[1]/div/div[1]/div/a/span/span[2]/span[2]")).getText();
            productPriceRange += "." + productElement.findElement(By.xpath(xpathString + "/div/span/div/div/div/div/div[2]/div[2]/div/div[3]/div[1]/div/div[1]/div/a/span/span[2]/span[3]")).getText();
        }catch(Exception e){
            System.out.println("Product Price Error!");
            productPriceRange = null;
        }
        
        newProduct.setProductLink(productLink);
        newProduct.setProductImageLink(productImageLink);
        newProduct.setProductName(productName);
        newProduct.setProductPrice(productPriceRange);

        return newProduct;

    }

    public int retrieveTotalResults(WebDriver driver){
        String tempString;
        String[] tokens;
        int totalResults;

        try{
            tempString = driver.findElement(By.xpath("//*[@id=\"search\"]/span/div/span/h1/div/div[1]/div/div/span[1]")).getText();
            tokens = tempString.split("\\s");
            int i = 0;
            for(String word: tokens){
                if(word.equalsIgnoreCase("over")){
                    break;
                }
                i++;
            }
            tempString = tokens[i+1];
            tempString = tempString.replace(",", "");
            totalResults = Integer.parseInt(tempString);
        }catch(Exception e){
            return 0;
        }

        return totalResults;
    }

    // public int retrievetotalPage(WebDriver driver){
    //     int totalPage;

    //     try{
    //         totalPage =  Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[3]/div/div[2]/div[2]/div[1]/div[2]/div/span[2]")).getText());
    //     }catch(Exception e){
    //         return 0;
    //     }
    //     return totalPage;
    // }

    public void takeScreenshot(String fileName) throws IOException,InterruptedException{
        // TAKE SCREENSHOT
        TakesScreenshot scrShot = ((TakesScreenshot) this.driver);
        File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File destFile = new File(String.format(String.format("C:\\Users\\Chris\\Desktop\\%s.png",fileName)));
        FileUtils.copyFile(srcFile, destFile);
    }

}
