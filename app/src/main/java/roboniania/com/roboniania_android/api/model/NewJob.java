package roboniania.com.roboniania_android.api.model;

/**
 * Created by Mateusz on 14.12.2016.
 */
public class NewJob {
    private String transaction_id;
    private String job;

    public NewJob(String transaction_id, String job) {
        this.transaction_id = transaction_id;
        this.job = job;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
