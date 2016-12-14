package roboniania.com.roboniania_android.api.model;

import java.io.Serializable;

/**
 * Created by Mateusz on 14.12.2016.
 */
public class Transaction implements Serializable {
        private String transaction_id;
        private String status;
        private String account_id;
        private String game_id;
        private String created_at;

        public Transaction() {
        }

        public Transaction(String transaction_id, String status, String account_id, String game_id, String created_at) {
            this.transaction_id = transaction_id;
            this.status = status;
            this.account_id = account_id;
            this.game_id = game_id;
            this.created_at = created_at;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAccount_id() {
            return account_id;
        }

        public void setAccount_id(String account_id) {
            this.account_id = account_id;
        }

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
}

