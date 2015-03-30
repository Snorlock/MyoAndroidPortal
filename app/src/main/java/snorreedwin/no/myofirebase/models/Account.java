package snorreedwin.no.myofirebase.models;


import java.util.Map;

public class Account {
    public String account_welcome;
    public String email;
    public Map<String, Firebase> firebases;

    public Map<String, Firebase> getFirebases() {
        return firebases;
    }

    public void setFirebases(Map<String, Firebase> firebases) {
        this.firebases = firebases;
    }

    public String getAccount_welcome() {
        return account_welcome;
    }

    public void setAccount_welcome(String account_welcome) {
        this.account_welcome = account_welcome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
