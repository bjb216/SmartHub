
package smarthub;

import java.io.IOException;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.util.ArrayList;
import java.math.*;




//http://financequotes-api.com

public class FinancialData {
    private final String[] stocks;
    User user;
    
    public FinancialData(User user){
        this.user=user;
        this.stocks=user.stocks;
    }
    
    public void populateStockArray(String[] stockList, ArrayList stockArray) throws IOException{
        String ticker;
        BigDecimal cost;
        BigDecimal percent;
        
        for (int i = 0; i <stocks.length; i++){
            Stock stk = YahooFinance.get(stocks[i]);
            ticker = stocks[i];
            cost = stk.getQuote().getPrice();
            percent = stk.getQuote().getChangeInPercent();
            
            SingleStock tempStock = new SingleStock(ticker, cost, percent);
            
            stockArray.add(tempStock);
        }
    }
    
    public void printInfo() throws IOException{
        for (int i=0;i<stocks.length;i++){
            Stock stk=YahooFinance.get(stocks[i]);
            System.out.println(stocks[i]+" "+stk.getQuote().getPrice()+" "+stk.getQuote().getChangeInPercent());
        }
    }
    
   /* public String getPrice(){
        return stk.getQuote().getPrice();
    }*/
    
    
    
}
