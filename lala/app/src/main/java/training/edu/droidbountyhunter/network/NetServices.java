package training.edu.droidbountyhunter.network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import training.edu.droidbountyhunter.interfaces.OnTaskListener;

public class NetServices extends AsyncTask<String, Void, Boolean> {
    private static final String LOG_TAG = NetServices.class.getSimpleName();
    private static final String endpoint_fugitivos =
            "http://3.13.226.218/droidBHServices.svc/fugitivos";
    private static final String endpoint_atrapados =
            "http://3.13.226.218/droidBHServices.svc/atrapados";
    private OnTaskListener listener;
    private String JSONString;
    private int code = 0;
    private String message;
    private String error;

    public NetServices(OnTaskListener listener) {
        this.listener = listener;
    }

    enum TYPE {
        FUGITIVOS, ATRAPADOS
    }

    @Override
    protected Boolean doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        JSONString = null;
        try {
            boolean isFugitivos = params[0].matches("Fugitivos");
            urlConnection = getStructuredRequest(
                    isFugitivos ? TYPE.FUGITIVOS : TYPE.ATRAPADOS,
                    isFugitivos ? endpoint_fugitivos : endpoint_atrapados,
                    isFugitivos ? "" : params[1]);
            InputStream is = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (is == null) {
                //No hay nada que hacer
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0)
                return true;
            JSONString = buffer.toString();
            Log.d(LOG_TAG, "Respuesta del Servidor: " + JSONString);
            return true;
        } catch (Exception e) {
            manageError(urlConnection);
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error Closing Stream", e);
                }
            }
        }
    }

    private HttpURLConnection getStructuredRequest(TYPE type, String endpoint, String id) throws IOException, JSONException {
        int TIME_OUT = 500;
        URL url = new URL(endpoint);
        HttpURLConnection urlConnection  = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(TIME_OUT);
        if (type == TYPE.FUGITIVOS) { //------------------- GET Fugitivos---------------
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();
        } else { //------------------ POST Atrapados----------------------------------
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            JSONObject object = new JSONObject();
            object.put("UDIDString", id);
            DataOutputStream dataOutputStream = new
                    DataOutputStream(urlConnection.getOutputStream());
            dataOutputStream.write(object.toString().getBytes());
            dataOutputStream.flush();
            dataOutputStream.close();
        }
        Log.d(LOG_TAG, url.toString());
        return urlConnection;
    }

    private void manageError(HttpURLConnection urlConnection) {
        if (urlConnection != null) {
            try {
                code = urlConnection.getResponseCode();
                if (urlConnection.getErrorStream() != null) {
                    InputStream is = urlConnection.getErrorStream();
                    StringBuilder buffer = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }
                    message = buffer.toString();
                } else {
                    message = urlConnection.getResponseMessage();
                }
                error = urlConnection.getErrorStream().toString();
                Log.e(LOG_TAG, "Error: " + error + ", code: " + code);
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.e(LOG_TAG, "Error");
            }
        } else {
            code = 105;
            message = "Error: No internet connection";
            Log.e(LOG_TAG, "code: " + code + ", " + message);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            listener.OnTaskCompleted(JSONString);
        } else {
            listener.OnTaskError(code, message);
        }
    }
}
