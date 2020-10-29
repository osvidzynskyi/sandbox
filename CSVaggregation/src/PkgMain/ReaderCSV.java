package PkgMain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.csvreader.CsvReader;

public class ReaderCSV {

	public static void main(String[] args) {
		try {
			
			CsvReader products = new CsvReader("test-task_dataset_summer_products.csv");
		
			products.readHeaders();

			List<Product> ProdList = new ArrayList<Product>();	        
					
			String ip_title = "";
			float ip_price = 0;
			float ip_rating_five_count = 0;
			float ip_rating_count = 0;
			String ip_origin_country = "";			
			
			while (products.readRecord())
			{			   
				ip_title = products.get("title");
				if ( products.get("price").isEmpty() ) {ip_price = 0;}
				else {ip_price = Float.parseFloat(products.get("price"));}
				if (products.get("rating_five_count").isEmpty()) {ip_rating_five_count = 0;}
				else {ip_rating_five_count = Float.parseFloat(products.get("rating_five_count"));}
				if (products.get("rating_count").isEmpty()) {ip_rating_count = 0;}
				else {ip_rating_count = Float.parseFloat(products.get("rating_count"));}
				ip_origin_country = products.get("origin_country");
				
				ProdList.add(new Product(ip_title, ip_price, ip_rating_five_count, ip_rating_count, ip_origin_country));
							
			}		
			
			Map<String, Double> AvgPricePerCountry = ProdList.stream()
					  .collect(Collectors.groupingBy(Product::getOrigin_country, 
							  						 Collectors.averagingDouble(Product::getPrice)));
			
			Map<String, Double> RatingfivePerCountry = ProdList.stream()
					  .collect(Collectors.groupingBy(Product::getOrigin_country, 
							  					     Collectors.summingDouble(Product::getRating_five_count)));
			
		
			Map<String, Double> RatingPerCountry = ProdList.stream()
					  .collect(Collectors.groupingBy(Product::getOrigin_country, 
							  						 Collectors.summingDouble(Product::getRating_count)));
			
			System.out.println("Country of Origins	Avg price of product	Share of five-star products");
			
    		for (Map.Entry<String, Double> entry : AvgPricePerCountry.entrySet())  
	            System.out.println(entry.getKey() + 
	                             "			" + (double)Math.round(entry.getValue()*100000)/100000 +
	                             "			" + (double)Math.round(RatingfivePerCountry.get(entry.getKey())/RatingPerCountry.get(entry.getKey())*100*100000)/100000); 
			
		
			products.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	

}

class Product {
    String title;
    float  price;
    float  rating_five_count;
    float  rating_count;
    String origin_country;
    
    Product(String title, float price, float rating_five_count, float rating_count, String origin_country) {
    	this.title = title;
 	    this.price = price;
 	    this.rating_five_count = rating_five_count;
 	    this.rating_count = rating_count;
 	    this.origin_country = origin_country;
    }
    
    public String getTitle() { return title; }
    public float getPrice() { return price; }
    public float getRating_five_count() { return rating_five_count; }
    public float getRating_count() { return rating_count; }
    public String getOrigin_country() { return origin_country; }
}
