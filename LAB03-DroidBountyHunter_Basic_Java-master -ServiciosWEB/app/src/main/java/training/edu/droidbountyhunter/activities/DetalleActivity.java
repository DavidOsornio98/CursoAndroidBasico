package training.edu.droidbountyhunter.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import training.edu.droidbountyhunter.R;
import training.edu.droidbountyhunter.data.DatabaseBountyHunter;
import training.edu.droidbountyhunter.interfaces.OnTaskListener;
import training.edu.droidbountyhunter.models.Fugitivo;
import training.edu.droidbountyhunter.network.NetServices;

public class DetalleActivity extends AppCompatActivity {
    private Fugitivo fugitivo;
    private DatabaseBountyHunter database;
    private Button capturarButton;
    private Button deleteButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se obtiene la información del Intent...
        Bundle bundleExtras = getIntent().getExtras();
        // Se pone el nombre del Fugitivo como título...
        fugitivo = bundleExtras.getParcelable("fugitivo");
        setTitle(fugitivo.getName() + " - Id: " + fugitivo.getId());

        //setTitle(bundleExtras.getString("title"));
        setContentView(R.layout.activity_detalle);
        database = new DatabaseBountyHunter(this);
        TextView label = findViewById(R.id.labelMessage);
        capturarButton = findViewById(R.id.buttonCapturar);
        deleteButton = findViewById(R.id.buttonEliminar);


        // Se identifica si es Fugitivo o Capturado para el mensaje...
        if(fugitivo.getStatus().equalsIgnoreCase("0")) {
            label.setText("El fugitivo sigue suelto...");
        }else {
            capturarButton.setVisibility(View.GONE);
            label.setText("Atrapado!!!");
        }
    }

    public void OnCaptureClick(View view) {
        fugitivo.setStatus("1");
        database.UpdateFugitivo(fugitivo);
        //Lab03
        NetServices netServices = new NetServices(new OnTaskListener() {
            @Override
            public void OnTaskCompleted(String response) {
                // después de traer los datos del web service se actualiza la interfaz...
                String message = "";
                try {
                    JSONObject object = new JSONObject(response);
                    message = object.optString("mensaje", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MessageClose(message);
            }

            @Override
            public void OnTaskError(int errorCode, String message) {
                Toast.makeText(getApplicationContext(),
                        "Ocurrio un problema en la comunicación con el WebService!!!",
                        Toast.LENGTH_LONG).show();
            }
        });
        netServices.execute("Atrapar", Home.UDID);
        capturarButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        setResult(RESULT_OK);

        //Lab02
    //setResult(0);
        finish();

    }

    public void OnDeleteClick(View view) {
        database.DeleteFugitivo(fugitivo.getId());
        setResult(0);
        finish();

    }

    //Lab03
    public void MessageClose(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Alerta!!!")
                .setMessage(message)
                .setOnDismissListener(dialogInterface -> {
                    setResult(RESULT_OK);
                    finish();
                }).show();
    }

}
