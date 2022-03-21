

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BankImpl implements Bank {

    private Map<Long,Account> accountList = new HashMap<>();
    private long idNew = 0;

    private final Logger log = LogManager.getLogger(getClass());


    @Override
    public Long createAccount(String name, String address) {
        // Check if threse is existing account
        Long id = findAccount(name, address);
        if(id != null) {
            log.debug("Such account already exists");
            return id;
        }

        // Create new account
        Account account = new Account(++idNew,name,  address, BigDecimal.ZERO);
        accountList.put(idNew, account);

        log.debug("Account created");

        return account.id;
    }

    @Override
    public Long findAccount(String name, String address) {
        for (Account account : accountList.values()) {
            if(account.name.equals(name) && account.address.equals(address))
                log.debug("Account succesfully founded");
                return account.id;
        }

        log.error("Account not found");
        return null;
    }

    @Override
    public void deposit(Long id, BigDecimal amount) throws AccountIdException {
        Account account = accountList.get(id);
        if(account == null) throw new AccountIdException();

        log.debug("Balance got succesfully");
        account.setBalance(amount);
    }

    @Override
    public BigDecimal getBalance(Long id) throws AccountIdException {
        Account account = accountList.get(id);
        if(account == null) throw new AccountIdException();

        log.debug("Balance got succesfully");
        return account.getBalance();

    }

    @Override
    public void withdraw(Long id, BigDecimal amount) throws AccountIdException, InsufficientFundsException {
        Account account = accountList.get(id);
        if(account == null) throw new AccountIdException();

        BigDecimal balance = account.getBalance();

        if(amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(amount) >= 0){
            account.setBalance(account.balance.subtract(amount));
        }
        else throw new InsufficientFundsException();
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) throws AccountIdException, InsufficientFundsException {
        Account srcAccount = accountList.get(idSource);
        Account destAccount = accountList.get(idDestination);

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
