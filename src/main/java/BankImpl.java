import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankImpl implements Bank {

    private List<Account> accountList = new ArrayList<>();
    private long idNew = 0;

    @Override
    public Long createAccount(String name, String address) {
        // Check if threse is existing account
        Long id = findAccount(name, address);
        if(id != null) return id;

        // Create new account
        Account account = new Account(++idNew,name,  address, BigDecimal.ZERO);
        accountList.add(account);
        return account.id;
    }

    @Override
    public Long findAccount(String name, String address) {
        for (Account account : accountList) {
            if(account.name.equals(name) && account.address.equals(address))
                return account.id;
        }

        return null;
    }

    @Override
    public void deposit(Long id, BigDecimal amount) throws AccountIdException {
        Account account = accountList.get(Math.toIntExact(id));
        if(account == null) throw new AccountIdException();

        account.setBalance(amount);
    }

    @Override
    public BigDecimal getBalance(Long id) throws AccountIdException {
        Account account = accountList.get(Math.toIntExact(id));
        if(account == null) throw new AccountIdException();

        return account.getBalance();

    }

    @Override
    public void withdraw(Long id, BigDecimal amount) throws AccountIdException, InsufficientFundsException {
        Account account = accountList.get(Math.toIntExact(id));
        if(account == null) throw new AccountIdException();

        BigDecimal balance = account.getBalance();

        if(amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(amount) >= 0){
            account.setBalance(account.balance.subtract(amount));
        }
        else throw new InsufficientFundsException();
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) throws AccountIdException, InsufficientFundsException {
        Account srcAccount = accountList.get(Math.toIntExact(idSource));
        Account destAccount = accountList.get(Math.toIntExact(idDestination));

        if(srcAccount == null || destAccount == null) {
            throw new AccountIdException();
        }

        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientFundsException();
        }

        BigDecimal srcBalance = srcAccount.getBalance();
        BigDecimal destBalance = destAccount.getBalance();

        if(srcBalance.compareTo(amount) <= 0) {
            throw new InsufficientFundsException("Sender has insfficient funds");
        }

        srcAccount.setBalance(srcBalance.subtract(amount));
        destAccount.setBalance(destBalance.add(amount));
    }
}
