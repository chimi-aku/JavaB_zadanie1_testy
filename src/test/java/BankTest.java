import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankTest {
    private Bank bank = new BankImpl();

    @Test
    public void createAccount_createdCorrectly() {
        // given
        String name = "x";
        String address = "d";

        // when
        Long id = bank.createAccount(name, address);

        // then
        assert id != null;
    } // Account created

    @Test
    public void findAccount_accountFound() {
        // given
        String name = "x";
        String address = "d";
        Long id = bank.createAccount(name, address);

        // when
        Long foundedId = bank.findAccount(name, address);

        // then
        assert id == foundedId;
    } // Account not found


    @Test
    public void findAccount_accountNotFound() {
        // given
        String name = "x";
        String address = "d";

        // when
        Long id = bank.findAccount(name, address);

        // then
        assert id == null;
    } // Account not found

    @Test
    public void deposit_AccountFound() {
        // given
        Long id = bank.createAccount("x", "d");
        BigDecimal amount = BigDecimal.valueOf(200f);

        // When
        bank.deposit(id, amount);
        BigDecimal balance = bank.getBalance(id);

        // Then
        assert balance == amount;
    }

    @Test
    public void deposit_AccountNotFound() {
        // given
        Long id = 3L;
        BigDecimal amount = BigDecimal.valueOf(2f);

        //When, Then
        assertThrows(Bank.AccountIdException.class, () -> {
            bank.deposit(id, amount);
        });
    }

    @Test
    public void getBalance_AccountFound() {
        // given
        Long id = bank.createAccount("x", "d");
        BigDecimal amount = BigDecimal.valueOf(2f);
        bank.deposit(id, amount);

        // When
        BigDecimal balance = bank.getBalance(id);

        // Then
        assert balance == amount;
    }

    @Test
    public void getBalance_AccountNotFound() {
        // given
        Long id = 3L;

        //When, Then
        assertThrows(Bank.AccountIdException.class, () -> {
            bank.getBalance(id);
        });
    }

    @Test
    public void withdraw_AccountNotFound() {
        // given
        Long id = 3L;
        BigDecimal amount = BigDecimal.valueOf(2f);

        // When, Then
        assertThrows(Bank.AccountIdException.class, () -> {
            bank.withdraw(id, amount);
        });
    }

    @Test
    public void withdraw_InsufficientFunds() {
        // given
        Long id = bank.createAccount("x", "d");
        BigDecimal deposit = BigDecimal.valueOf(20f);
        BigDecimal withdrawAmount = BigDecimal.valueOf(100f);

        bank.deposit(id, deposit);

        // When, Then
        assertThrows(Bank.InsufficientFundsException.class, () -> {
            bank.withdraw(id, withdrawAmount);
        });
    }

    @Test
    public void withdraw_sufficientFunds() {
        // given
        Long id = bank.createAccount("x", "d");
        BigDecimal deposit = BigDecimal.valueOf(300f);
        BigDecimal withdrawAmount = BigDecimal.valueOf(100f);

        bank.deposit(id, deposit);

        // When
        bank.withdraw(id, withdrawAmount);
        BigDecimal amountAfterWithdraw = bank.getBalance(id);

        // Then
        assert amountAfterWithdraw.floatValue() == 200;
    }

    @Test
    public void transfer_sourceAccountNotFound() {
        // given
        Long srcId = 3L;
        Long destId = bank.createAccount("x", "d");
        BigDecimal amount = BigDecimal.valueOf(2f);

        // When, Then
        assertThrows(Bank.AccountIdException.class, () -> {
            bank.transfer(srcId, destId, amount);
        });
    }

    @Test
    public void transfer_destinationAccountNotFound() {
        // given
        Long srcId = bank.createAccount("x", "d");
        Long destId = 3L;
        BigDecimal amount = BigDecimal.valueOf(2f);

        // When, Then
        assertThrows(Bank.AccountIdException.class, () -> {
            bank.transfer(srcId, destId, amount);
        });
    }

    @Test
    public void transfer_transferAmountIsLessThanZero() {
        // given
        Long srcId = bank.createAccount("x", "d");
        Long destId = bank.createAccount("a", "b");;
        BigDecimal amount = BigDecimal.valueOf(-10f);

        bank.deposit(srcId, BigDecimal.valueOf(2f));

        // When, Then
        assertThrows(Bank.InsufficientFundsException.class, () -> {
            bank.transfer(srcId, destId, amount);
        });
    }

    @Test
    public void transfer_sourceInsufficientFunds() {
        // given
        Long srcId = bank.createAccount("x", "d");
        Long destId = bank.createAccount("a", "b");;
        BigDecimal amount = BigDecimal.valueOf(10f);

        bank.deposit(srcId, BigDecimal.valueOf(2f));

        // When, Then
        assertThrows(Bank.InsufficientFundsException.class, () -> {
            bank.transfer(srcId, destId, amount);
        });
    }

    @Test
    public void transfer_successTransfer() {
        // given
        Long srcId = bank.createAccount("x", "d");
        Long destId = bank.createAccount("a", "b");;
        BigDecimal amount = BigDecimal.valueOf(10f);

        bank.deposit(srcId, BigDecimal.valueOf(100f));

        // When
        bank.transfer(srcId, destId, amount);

        BigDecimal srcBalanceAfterTransfer = bank.getBalance(srcId);
        BigDecimal destBalanceAfterTransfer = bank.getBalance(destId);

        boolean success = srcBalanceAfterTransfer.floatValue() == 90
                && destBalanceAfterTransfer.floatValue() == 10;

        // Then
        assert success == true;
    }


}
