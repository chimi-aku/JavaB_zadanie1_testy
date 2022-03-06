import java.math.BigDecimal;


public class Account {
    public Long id;
    public String name;
    public String address;
    public BigDecimal balance;

    public Account(Long id, String name, String address, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.balance = balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
