package Controller;

public class UserConfigs {
    private String username;
    private String password;
    private String[] files;
    private String[][] requests;
    private String[][] acceptedRequests;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String[][] getRequests() {
        return requests;
    }

    public void setRequests(String[][] requests) {
        this.requests = requests;
    }

    public String[][] getAcceptedRequests() {
        return acceptedRequests;
    }

    public void setAcceptedRequests(String[][] acceptedRequests) {
        this.acceptedRequests = acceptedRequests;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }
}
