/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthub;

import java.math.*;

/**
 *
 * @author Matthew
 */
public class SingleStock {
    //initiliaze all global variables
    
    String stockTicker;
    BigDecimal stockCost;
    BigDecimal stockPercentChange;
    public String brandonPath = "/users/bartonb/Documents/GUIpics/";
    public String mattPath = "/Users/Matthew/Documents/Senior Design/Financial Pictures/Weather Pics";
    public String testPath = brandonPath;
    
    public SingleStock(String ticker, BigDecimal cost, BigDecimal change){
        stockTicker = ticker;
        stockCost = cost;
        stockPercentChange = change;
    }
    
    public String getTicker(){
        return stockTicker;
    }
    
    public String getCost(){
        String costString = stockCost.toString();
        return costString;
    }
    
    public String getPercentChange(){
        String percentString = stockPercentChange.toString();
        return percentString;
    }
    
    public String getStockPic(){
        String stockPicString = null;
        
        if (stockPercentChange.compareTo(BigDecimal.ZERO) > 0){
                stockPicString = testPath+"/UpArrowGreen.png";
            }
        else{
                stockPicString = testPath+"/DownArrowRed.png";
            }
        
        return stockPicString;
    }
    
    public void printStock(){
        System.out.println("Ticker: " + stockTicker);
        System.out.println("Cost: " + stockCost);
        System.out.println("Percent Change: " + stockPercentChange);
    }
}
