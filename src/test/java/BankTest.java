import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankTest {
    private Bank bank = new BankImpl();

    @Test
    public void createAccountTest() {
        Long id = bank.createAccount("a", "b");
        assert id != null;
    } // Account created

    @Test
    public void accountNotFoundTest() {
        Long id = bank.findAccount("X", "D");
        assert id == null;
    } // Account not found

    @Test
    public void depositTest() {
        Long id = bank.findAccount("X", "D");

        bank.deposit(id, BigDecimal.valueOf(11f));
        BigDecimal amount = bank.getBalance(id);
        assert amount.floatValue() > 0;
    }

    @Test
    public void depositAccountNotFoundTest() {
        assertThrows(Bank.AccountIdException.class, () -> {
            bank.getBalance(3L);
        });
    }






}
