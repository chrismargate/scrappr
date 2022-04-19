package expressoapp;

public class Product {
    private String productName;
    private String productPrice;
    private String productRating;
    private String productLink;
    private String productImageLink;

    public Product(){
        this("","","");
    }

    public Product(String productName,String productPrice){
        this(productName,productPrice,"");
    }

    public Product(String productName,String productPrice,String productRating){
        this.productName = productName;
        this.productPrice = productPrice;
        this.productRating = productRating;
        this.productLink = "";
        this.productImageLink = "";
    }

    public String getProductName(){
        return this.productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public String getProductPrice(){
        return this.productPrice;
    }

    public void setProductPrice(String productPrice){
        this.productPrice = productPrice;
    }

    public String getProductLink(){
        return this.productLink;
    }

    public void setProductLink(String productLink){
        this.productLink = productLink;
    }

    public String getProductImageLink(){
        return this.productImageLink;
    }

    public void setProductImageLink(String productImageLink){
        this.productImageLink = productImageLink;
    }

    public String getProductRating(){
        return this.productRating;
    }

    public void setProductRating(String productRating){
        this.productRating = productRating;
    }

}
