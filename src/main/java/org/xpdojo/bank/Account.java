/*
 *
 * Copyright (c) 2019 xp-dojo organisation and committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xpdojo.bank;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.xpdojo.bank.Money.ZERO;
import static org.xpdojo.bank.Result.failure;
import static org.xpdojo.bank.Result.success;
import static org.xpdojo.bank.Transaction.Deposit.depositOf;
import static org.xpdojo.bank.Transaction.Withdraw.withdrawalOf;

public class Account {

    private final List<Transaction> transactions = new ArrayList<>();
    private final Clock clock;

    public static Account accountWithBalance(Money balance, Clock clock) {
        return new Account(balance, clock);
    }

    public static Account emptyAccount(Clock clock) {
        return accountWithBalance(ZERO, clock);
    }

    private Account(Money balance, Clock clock) {
        this.clock = clock;
        deposit(balance);
    }

    public Money balance() {
        return transactions().reduce(ZERO, (money, transaction) -> transaction.against(money), (a, b) -> a);
    }

	public void deposit(Money amount) {
        transactions.add(depositOf(amount, clock.now()));
    }

    public Result withdraw(Money amount) {
        if (balance().isLessThan(amount))
            return failure();

        transactions.add(withdrawalOf(amount, clock.now()));
        return success();
    }

    public void transfer(Money amount, Account receiver) {
        Result withdrawal = withdraw(amount);
        if (withdrawal.succeeded()) {
            receiver.deposit(amount);
        }
    }

    public String writeStatement(Statement statement, Writer writer) throws IOException {
        statement.write(this, writer);
        return writer.toString();
    }

    public Stream<Transaction> transactions() {
    	return transactions.stream();
	}

}
