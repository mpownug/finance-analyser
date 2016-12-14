package pl.pownug.marek.financeAnalyzer.domain;

import pl.pownug.marek.financeAnalyzer.charts.AccountBalance;
import pl.pownug.marek.financeAnalyzer.charts.AccountBalanceBar;

import java.util.List;

/**
 * Created by Comarch-Karol Kalaga <karol.kalaga@comarch.com>
 */
public class Overview {
    List<AccountBalanceBar> accountBalanceBars;
    List<AccountBalance> accountBalances;
    List<Account> accounts;


    public List<AccountBalanceBar> getAccountBalanceBars() {
        return accountBalanceBars;
    }

    public void setAccountBalanceBars(List<AccountBalanceBar> accountBalanceBars) {
        this.accountBalanceBars = accountBalanceBars;
    }

    public List<AccountBalance> getAccountBalances() {
        return accountBalances;
    }

    public void setAccountBalances(List<AccountBalance> accountBalances) {
        this.accountBalances = accountBalances;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
