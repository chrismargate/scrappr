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

public class LazadaSearcher implements Runnable{
    
    private JavascriptExecutor js;
    private String userSearch;
    private WebDriver driver;
    private ChromeOptions options;
    private ArrayList<Product> productsList;
    private int productsListCount = 0;
    private int startProductNumberSearch = 1;
    private int numberOfResults = 10;
    private int instanceFail = 0;
    private static int maxErrorChecking = 4;

    public LazadaSearcher(){
        this(1,10);
    }

    public LazadaSearcher(int startProductNumberSearch, int numberOfResults){
        this.options = new ChromeOptions();
        this.configureChromeOptions();
        this.driver = new ChromeDriver(this.options);
        this.setDriverDimensions(1920, 1080);
        this.productsList = new ArrayList<>();
        this.js = ((JavascriptExecutor) driver);
        this.startProductNumberSearch = startProductNumberSearch;
        this.numberOfResults = numberOfResults;
    }

    private void configureChromeOptions(){
        this.options.addArguments("disable-notifications");
        this.options.addArguments("--disable-infobars");
        this.options.addArguments("start-maximized");
        this.options.addArguments("disable-infobars");
        this.options.addArguments("--no-startup-window");
        this.options.setHeadless(true);
    }

    public ArrayList<Product> getProductsList(){
        return this.productsList;
    }

    public void setProductsList(ArrayList<Product> productsList){
        this.productsList = productsList;
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

    public String getSearch(){
        return this.userSearch;
    }
    
    public void setSearch(String userSearch){
        this.userSearch = userSearch;
    }
    
    public int getInstanceFail(){
        return this.instanceFail;
    }

    public void setInstanceFail(int instanceFail){
        this.instanceFail = instanceFail;
    }
  

    @Override
    public void run(){
        System.out.println("Thread has started");
        try{
            this.search();
        }
        catch(Exception e){
            
        }
        System.out.println("Thread has ended");
    }

    public void search() throws IOException,InterruptedException{
        this.driver = new ChromeDriver(this.options);

        WebElement productElement;
        String xpathString;
        String productName, productLink, productImageLink, productPriceRange;

        int resultsCounter = 0;
        int initialScroll;
        int currentPage = 0;
        int totalPage = 0;
        int errorCounter = 0;
        int errorAfterReload = 0;
        
        this.productsList.clear();

        this.driver.get("https://www.lazada.com.ph/");

        Thread.sleep(1000);

        // Enters the users search on shopee's search
        WebElement searchBar = driver.findElement(By.xpath("//*[@id=\"q\"]"));
        searchBar.sendKeys(this.userSearch);
        searchBar.sendKeys("\n");

        Thread.sleep(1000);

        currentPage = this.retrieveCurrentPage(driver);
        totalPage = this.retrieveTotalPage(driver);
        System.out.println(String.format("Page %d of %d",currentPage,totalPage));

        this.takeScreenshot("lazadaScreenShot1");

        int productDiv = this.startProductNumberSearch;

        if(this.startProductNumberSearch>10){
            initialScroll = (this.startProductNumberSearch / 10) * 2;
            int i = 0 ;
            while(i < initialScroll){
                this.js.executeScript("window.scrollBy(0,750)", "");
                System.out.println("Initial Scroll executed!");
                i++;
            }
            this.takeScreenshot("lazadaScreenShot1");
        }// starts scrolling based on which product number to start

        while(resultsCounter < this.numberOfResults){
            Product newProduct = new Product();
            productElement = null;
            
            if(productDiv >= 40){
                if(currentPage >= totalPage){
                    break;
                }// inner if
                
                try{
                    WebElement nextButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div/div[1]/div[3]/div/ul/li[9]/button"));
                    nextButton.click();
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Error in next button");
                }
                
                currentPage ++;
                Thread.sleep(1000);
                productDiv = 1;
                this.startProductNumberSearch = 1;
                System.out.println(String.format("\nPage %d of %d\n",currentPage,totalPage));
            }//outer if
            xpathString = String.format("//*[@id=\"root\"]/div/div[2]/div[1]/div/div[1]/div[2]/div[%d]",productDiv);
            
            try{
                if(this.productExists(xpathString) == false){
                System.out.println(String.format("Product div[%d] does not exist",productDiv));
                errorCounter++;
                if(errorCounter>=5){
                    break;
                }
                }// product exists checker
            }catch(Exception e){
                System.out.println("Error in product exists checker");
            }
            
            try{
                productElement = this.driver.findElement(By.xpath(xpathString));
            }catch(Exception e){
                System.out.println("Error in productElement");
            }
            
            newProduct = this.retrieveProductInfo(xpathString, productElement);

            System.out.println(String.format("Product div [%d]",productDiv));
            System.out.println("Product Link: " + newProduct.getProductLink());
            System.out.println("Product Image Link: " + newProduct.getProductImageLink());
            System.out.println("Product Name: " + newProduct.getProductName());
            System.out.println("Product Price: " + newProduct.getProductPrice());
            System.out.println("\n");
            
            this.productsList.add(newProduct);
            
            if(productDiv>=5){
                this.js.executeScript("window.scrollBy(0,1000)", "");
                System.out.println("JScript executed");
            }//if statement 2
            productDiv++;
            resultsCounter++;
        }// while loop

        this.takeScreenshot("screenShot2");

        this.driver.quit();
    }// search function

    private boolean productExists(String xpathString){
        boolean productExists;
        try{
            productExists = this.driver.findElement(By.xpath(xpathString)).isDisplayed() ? true : false;
        }catch(Exception e){
            System.out.println("Product does not exist!");
            this.js.executeScript("window.scrollBy(0,500)", "");
            System.out.println("Javascript Executed!");
            return false;
        }

        return true;
    }

    //RETTRIEVAL OF PRODUCT ELEMENTS

    private Product retrieveProductInfo(String xpathString,WebElement product) throws InterruptedException{
        String productLink;
        String productName;
        String productImageLink;
        String productPriceRange;

        Product newProduct = new Product();

        try{
            productLink = product.findElement(By.xpath(xpathString + "/div/div/div[2]/div[2]/a")).getAttribute("href");
        }catch(Exception e){
            System.out.println("Product link could not be found!");
            productLink = null;
        }

        
        try{
            // //*[@id="root"]/div/div[2]/div[1]/div/div[1]/div[2]/div[1]/div/div/div[1]/div[1]/a/img
            // //*[@id="root"]/div/div[2]/div[1]/div/div[1]/div[2]/div[2]/div/div/div[1]/div[1]/a/img

            productImageLink = product.findElement(By.xpath(xpathString + "/div/div/div[1]/div[1]/a/img")).getAttribute("src");
        }catch(Exception e){
            System.out.println("Product image link could not be found!");
            productImageLink = null;
        }

        try{
            // //*[@id="root"]/div/div[2]/div[1]/div/div[1]/div[2]/div[1]/div/div/div[2]/div[2]/a
            productName = product.findElement(By.xpath(xpathString + "/div/div/div[2]/div[2]/a")).getAttribute("title");
        }catch(Exception e){
            System.out.println("Product name could not be found!");
            productName = null;
        }

        try{
            // //*[@id="root"]/div/div[2]/div[1]/div/div[1]/div[2]/div[1]/div/div/div[2]/div[3]/span
            productPriceRange = product.findElement(By.xpath(xpathString + "/div/div/div[2]/div[3]/span")).getText();
        }catch(Exception e){
            System.out.println("Product price range could not be found!");
            productPriceRange = null;
        }

        newProduct.setProductLink(productLink);
        newProduct.setProductImageLink(productImageLink);
        newProduct.setProductName(productName);
        newProduct.setProductPrice(productPriceRange);

        return newProduct;

    }

    public int retrieveCurrentPage(WebDriver driver){
        int currentPage;

        try{
            currentPage = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div/div[1]/div[3]/div/ul/li[2]/a")).getText());
        }catch(Exception e){
            System.out.println("Could not retrieve current page");
            return 0;
        }
        return currentPage;
    }

    public int retrieveTotalPage(WebDriver driver){
        int totalPage;

        try{
            totalPage =  Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div/div[1]/div[3]/div/ul/li[8]/a")).getText());
        }catch(Exception e){
            System.out.println("Could not retrieve total page");
            return 0;
        }
        return totalPage;
    }

    public void takeScreenshot(String fileName) throws IOException,InterruptedException{
        // TAKE SCREENSHOT
        TakesScreenshot scrShot = ((TakesScreenshot) this.driver);
        File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File destFile = new File(String.format(String.format("C:\\Users\\Chris\\Desktop\\%s.png",fileName)));
        FileUtils.copyFile(srcFile, destFile);
    }

}
