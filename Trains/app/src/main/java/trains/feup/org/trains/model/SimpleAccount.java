package trains.feup.org.trains.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mzamith on 01/04/17.
 */

public class SimpleAccount {

    private String username;

    private String cardNumber;

    private Long cardDate;

    public SimpleAccount(){}

    public SimpleAccount(String username, String cardNumber, Long cardDate){
        this.username = username;
        this.cardNumber = cardNumber;
        this.cardDate = cardDate;
    }

    public SimpleAccount(String username, String cardNumber, String cardDate){

        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");

        this.username = username;
        this.cardNumber = cardNumber;

        try {
            this.cardDate = df2.parse(cardDate).getTime();
        }catch (ParseException pe){
            this.cardDate = null;
        }

    }

    public SimpleAccount(Account account){
        this.username = account.getUsername();
        this.cardNumber = account.getCardNumber();
        this.cardDate = account.getCardDate().getTime();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String creditCard) {
        this.cardNumber = creditCard;
    }

    public Long getCardDate() {
        return cardDate;
    }

    public void setCardDate(Long cardDate) {
        this.cardDate = cardDate;
    }

    public String getCardDateString(){

        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(cardDate);

        return df2.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleAccount that = (SimpleAccount) o;

        if (username != null ? !username.equals(that.username) : that.username != null)
            return false;
        if (cardNumber != null ? !cardNumber.equals(that.cardNumber) : that.cardNumber != null)
            return false;
        return cardDate != null ? cardDate.equals(that.cardDate) : that.cardDate == null;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
        result = 31 * result + (cardDate != null ? cardDate.hashCode() : 0);
        return result;
    }
}
