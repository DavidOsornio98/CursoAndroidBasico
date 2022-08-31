package training.edu.droidbountyhunter.interfaces;

public interface OnTaskListener {
    void OnTaskCompleted(String response);
    void OnTaskError(int code, String message);

}
