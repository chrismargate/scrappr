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

public class ShopeeSearcher implements Runnable{

    private JavascriptExecutor js;
    private String userSearch;
    private WebDriver driver;
    private ChromeOptions options;
    private ArrayList<Product> productsList;
    private int productsListCount = 0;
    private int startProductNumberSearch = 1;
    private int numberOfResults = 10;
    private int instanceFail = 0;

    public ShopeeSearcher(){
        this(1,10);
    }

    public ShopeeSearcher(int startProductNumberSearch, int numberOfResults){
        this.options = new ChromeOptions();
        this.configureChromeOptions();
        this.driver = new ChromeDriver(this.options);
        this.setDriverDimensions(1920, 1080);
        this.js = ((JavascriptExecutor) this.driver);
        this.productsList = new ArrayList<>();
        this.startProductNumberSearch = startProductNumberSearch;
        this.numberOfResults = numberOfResults;
    }

    private void configureChromeOptions(){
        this.options.addArguments("disable-notifications");
        this.options.addArguments("--disable-infobars");
        this.options.addArguments("start-maximized");
        this.options.addArguments("disable-infobars");
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

    public int getInstanceFail(){
        return this.instanceFail;
    }

    public void setInstanceFail(int instanceFail){
        this.instanceFail = instanceFail;
    }
  
    public String getSearch(){
        return this.userSearch;
    }
    
    public void setSearch(String userSearch){
        this.userSearch = userSearch;
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
        String xpathString, xpathSecondPrice;
        String productName, productLink, productImageLink, productPriceRange;

        int resultsCounter = 0;
        int initialScroll;
        int currentPage = 0;
        int totalPage = 0;
        int errorCounter = 0;
        int errorAfterReload = 0;
        boolean hasProductLink;
        boolean hasProductName;
        boolean hasProductPrice;
        boolean hasProductRating;
        boolean hasCompleteDetails;

        this.productsList.clear();
        
        this.driver.get("https://shopee.ph");

        Thread.sleep(1000);

        // Closes the pop up when first visiting shopee site
        WebElement closePopUpButton = driver.findElement(By.xpath("//*[@id=\"modal\"]/div/div/div[2]/div"));
        closePopUpButton.click();

        Thread.sleep(1000);

        // Enters the users search on shopee's search
        WebElement searchBar = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[1]/div[2]/div/div[1]/div[1]/div/form/input"));
        searchBar.sendKeys(this.userSearch);
        searchBar.sendKeys("\n");

        Thread.sleep(1000);

        currentPage = this.retrieveCurrentPage(driver);
        totalPage = this.retrieveTotalPage(driver);

        int productDiv = this.startProductNumberSearch;

        if(this.startProductNumberSearch>10){
            initialScroll = (this.startProductNumberSearch / 10) * 2;
            int i = 0 ;
            while(i < initialScroll){
                this.js.executeScript("window.scrollBy(0,1000)", "");
                System.out.println("Initial Scroll executed!");
                i++;
            }
            
        }

        while(resultsCounter < this.numberOfResults){
            Product newProduct = new Product();
            hasProductLink = false;
            hasProductName = false;
            hasProductPrice = false;
            hasProductRating = false;
            hasCompleteDetails = false;
            productElement = null;

            if(productDiv >= 60){
                if(currentPage >= totalPage){
                    break;
                }
                try{
                    WebElement nextButton = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[3]/div/div[2]/div[2]/div[1]/div[2]/button[2]"));
                    nextButton.click();
                }catch(Exception e){
                    System.out.println("Error in next button");
                }
                
                currentPage ++;
                Thread.sleep(1000);

                productDiv = 1;
                this.startProductNumberSearch = 1;
                System.out.println(String.format("\nPage %d of %d\n",currentPage,totalPage));
            }

            xpathString = String.format("//*[@id=\"main\"]/div/div[3]/div/div[2]/div[2]/div[2]/div[%d]",productDiv);

            try{
                if(this.productExists(xpathString) == false){
                    System.out.println(String.format("Product div[%d] does not exist",productDiv));
                    errorCounter++;
                    if(errorCounter >= 5){
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

            // if(newProduct.getProductLink() != null){
            //     hasProductLink = true;
            //     if(newProduct.getProductName() != null){
            //         hasProductName = true;
            //         if(newProduct.getProductPrice() != null){
            //             hasProductPrice = true;
            //             hasCompleteDetails = true;
            //             if(newProduct.getProductRating() != null){
            //                 hasProductRating = true;
            //             }
            //         }
            //     }
            // }
            
            // if(hasCompleteDetails)

            this.productsList.add(newProduct);

            System.out.println("\n");
            if(resultsCounter==5){
                try{
                    this.js.executeScript("window.scrollBy(0,1000)", "");
                    System.out.println("JScript executed");
                }catch(Exception e){
                    System.out.println("JScript execution error");
                }
                
            }//if statement 2

            productDiv++;
            resultsCounter++;

        }// while loop

        this.takeScreenshot("screenShot2");

        for(Product productItem: this.productsList){
            System.out.println(String.format("%s\n%s\n%s\n%d",productItem.getProductLink(),productItem.getProductImageLink(),productItem.getProductName(),productItem.getProductPrice()));
        }

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

    private boolean secondPriceExists(String xpathSecondPrice,WebElement product){
        try{
            product.findElement(By.xpath(xpathSecondPrice)).isDisplayed();
        }catch(Exception e){
            System.out.println("Second price does not exist!");
            return false;
        }
        return true;
    }

    //RETTRIEVAL OF PRODUCT ELEMENTS

    private Product retrieveProductInfo(String xpathString,WebElement product) throws InterruptedException{
        String productLink;
        String xpathSecondPrice;
        String productName;
        String productImageLink;
        String productPriceRange;

        xpathSecondPrice = xpathString + "/a/div/div/div[2]/div[2]/div/span[4]";

        Product newProduct = new Product();

        try{
            productLink = product.findElement(By.xpath(xpathString + "/a")).getAttribute("href");
        }catch(Exception e){
            productLink = null;
        }

        try{
            productImageLink = product.findElement(By.xpath(xpathString + "/a/div/div/div[1]/img")).getAttribute("src");
        }catch(Exception e){
            productImageLink = null;
        }

        try{
            productName = product.findElement(By.xpath(xpathString + "/a/div/div/div[2]/div[1]/div/div")).getText();
        }catch(Exception e){
            productName = null;
        }

        try{
            productPriceRange = product.findElement(By.xpath(xpathString + "/a/div/div/div[2]/div[2]/div/span[2]")).getText();
        }catch(Exception e){
            productPriceRange = null;
        }

        if(this.secondPriceExists(xpathSecondPrice,product))
            productPriceRange += String.format("-%s",product.findElement(By.xpath(xpathString + "/a/div/div/div[2]/div[2]/div/span[4]")).getText());
        
        newProduct.setProductLink(productLink);
        newProduct.setProductImageLink(productImageLink);
        newProduct.setProductName(productName);
        newProduct.setProductPrice(productPriceRange);

        return newProduct;

    }

    public int retrieveCurrentPage(WebDriver driver){
        int currentPage;

        try{
            currentPage = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[3]/div/div[2]/div[2]/div[1]/div[2]/div/span[1]")).getText());
        }catch(Exception e){
            return 0;
        }
        return currentPage;
    }

    public int retrieveTotalPage(WebDriver driver){
        int totalPage;

        try{
            totalPage =  Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[3]/div/div[2]/div[2]/div[1]/div[2]/div/span[2]")).getText());
        }catch(Exception e){
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