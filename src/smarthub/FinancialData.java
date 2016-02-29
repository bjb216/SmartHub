
package smarthub;

import java.io.IOException;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;


//http://financequotes-api.com

public class FinancialData {
    private final String[] stocks;
    User user;
    
    public FinancialData(User user){
        this.user=user;
        this.stocks=user.stocks;
    }
    
    public void printInfo() throws IOException{
        for (int i=0;i<stocks.length;i++){
            Stock stk=YahooFinance.get(stocks[i]);
            System.out.println(stocks[i]+" "+stk.getQuote().getPrice()+" "+stk.getQuote().getChangeInPercent());
        }
    }
    
    
    
}
