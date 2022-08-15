package training.edu.droidbountyhunter.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import training.edu.droidbountyhunter.R;
import training.edu.droidbountyhunter.data.DatabaseBountyHunter;
import training.edu.droidbountyhunter.models.Fugitivo;

public class DetalleActivity extends AppCompatActivity {
    private Fugitivo fugitivo;
    private DatabaseBountyHunter database;
    private Button capturarButton;

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
        setResult(0);
        finish();

    }

    public void OnDeleteClick(View view) {
        database.DeleteFugitivo(fugitivo.getId());
        setResult(0);
        finish();

    }
}
