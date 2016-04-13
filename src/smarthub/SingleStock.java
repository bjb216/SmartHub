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
    BigDecimal divYield;
    BigDecimal ytdChange;
    String name;
    
    public String brandonPath = "/users/bartonb/Documents/GUIpics/";
    public String mattPath = "/Users/Matthew/Documents/Senior Design/Financial Pictures/Weather Pics";
    public String piPath = "/home/pi/pictures";
    public String testPath = piPath;
    
    public SingleStock(String ticker, BigDecimal cost, BigDecimal change, String name, BigDecimal divYield, BigDecimal ytdChange){
        stockTicker = ticker;
        stockCost = cost;
        stockPercentChange = change;
        this.name=name;
        this.divYield=divYield;
        this.ytdChange=ytdChange;
        
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
    
    public String getStockPic(BigDecimal bd){
        String stockPicString = null;
        
        if (bd.compareTo(BigDecimal.ZERO) > 0){
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
