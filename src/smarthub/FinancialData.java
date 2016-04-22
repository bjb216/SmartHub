
package smarthub;

import java.io.IOException;
import static java.lang.Boolean.FALSE;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.util.ArrayList;
import java.math.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import yahoofinance.quotes.stock.StockDividend;




//http://financequotes-api.com

public class FinancialData {
    private final ArrayList<String> stocks;
    User user;
    
    public FinancialData(User user){
        this.user=user;
        this.stocks=user.stocks;
    }
    
    public void populateStockArray(ArrayList stockArray) throws IOException{
        String ticker;
        String name;
        BigDecimal cost;
        BigDecimal percent;
        BigDecimal divYield;
        BigDecimal ytdChange;
        YahooFinance.logger.setUseParentHandlers(false);
        
        for (int i = 0; i <stocks.size(); i++){
            Stock stk = YahooFinance.get(stocks.get(i));
            ticker = stocks.get(i);
            cost = stk.getQuote().getPrice();
            percent = stk.getQuote().getChangeInPercent();
            name = stk.getName();
            StockDividend d = stk.getDividend();
            divYield = d.getAnnualYieldPercent();
            Calendar c = new GregorianCalendar(2016,1,1);
            System.out.println("num of data points"+stk.getHistory(c).size());
            System.out.println("stock price jan 1: "+ stk.getHistory(c).get(2).getClose());
            ytdChange = cost.divide(stk.getHistory(c).get(0).getClose(),2,BigDecimal.ROUND_HALF_UP);
            System.out.println(name +": "+ytdChange);
            BigDecimal one = new BigDecimal(1);
            ytdChange = ytdChange.subtract(one);
            
            SingleStock tempStock = new SingleStock(ticker, cost, percent, name, divYield,ytdChange);
            
            stockArray.add(tempStock);
        }
    }
    
    public void printInfo() throws IOException{
        for (int i=0;i<stocks.size();i++){
            Stock stk=YahooFinance.get(stocks.get(i));
            //System.out.println(stocks[i]+" "+stk.getQuote().getPrice()+" "+stk.getQuote().getChangeInPercent());
        }
    }
    
    public static boolean isValid(String ticker){
        try {
            Stock stock = YahooFinance.get(ticker);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
   /* public String getPrice(){
        return stk.getQuote().getPrice();
    }*/
    
    
    
}
