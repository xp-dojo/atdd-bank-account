package org.acmebank.bankaccount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.acmebank.bankaccount.Account.accountWithBalance;
import static org.acmebank.bankaccount.Account.emptyAccount;
import static org.acmebank.bankaccount.Money.ZERO;
import static org.acmebank.bankaccount.Money.amountOf;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("An account")
class AccountTest {

    @Test
    @DisplayName("should have zero balance when created")
    void newAccountShouldHaveZeroBalance() {
        Account account = emptyAccount();
        assertThat(account.balance()).isEqualTo(ZERO);
    }

    @Nested
    @DisplayName("deposit")
    class Deposit {
        @Test
        @DisplayName("should increase the balance of an empty account")
        void depositToEmptyAccountShouldIncreaseTheBalance() {
            Account account = emptyAccount();
            account.deposit(amountOf(10));
            assertThat(account.balance()).isEqualTo(amountOf(10));
        }

        @Test
        @DisplayName("should increase the balance of a non-empty account")
        void depositToNonEmptyAccountShouldIncreaseTheBalance() {
            Account account = accountWithBalance(amountOf(10));
            account.deposit(amountOf(20));
            assertThat(account.balance()).isEqualTo(amountOf(30));
        }
    }

    @Nested
    @DisplayName("withdrawal")
    class Withdrawal {
        @Test
        @DisplayName("should decrease the balance by the amount of the withdrawal")
        void withdrawalShouldDecreaseTheBalance() {
            Account account = accountWithBalance(amountOf(10));
            account.withdraw(amountOf(10));
            assertThat(account.balance()).isEqualTo(ZERO);
        }

        @Test
        @DisplayName("should not be applied when it would take the account overdrawn")
        void withdrawalShouldNotBeAppliedWhenItTakesAccountOverdrawn() {
            Account account = emptyAccount();
            account.withdraw(amountOf(1));
            assertThat(account.balance()).isEqualTo(ZERO);
        }
    }

    @Nested
    @DisplayName("transfer")
    class Transfer {
        @Test
        @DisplayName("should transfer the amount from one account to the other")
        void moneyCanBeTransferredBetweenAccounts() {
            Account sender = accountWithBalance(amountOf(10));
            Account receiver = emptyAccount();

            sender.transfer(amountOf(10), receiver);

            assertThat(sender.balance()).isEqualTo(ZERO);
            assertThat(receiver.balance()).isEqualTo(amountOf(10));
        }

        @Test
        @DisplayName("should not be applied when it would take the account overdrawn")
        void moneyShouldNotBeTransferredWhenItTakesSendingAccountOverdrawn() {
            Account sender = emptyAccount();
            Account receiver = emptyAccount();

            sender.transfer(amountOf(1), receiver);

            assertThat(sender.balance()).isEqualTo(ZERO);
            assertThat(receiver.balance()).isEqualTo(ZERO);
        }
    }
}
