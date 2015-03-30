package snorreedwin.no.myofirebase.models;

public class FirebaseCreateResponse {
    public boolean success;
    public String error;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
