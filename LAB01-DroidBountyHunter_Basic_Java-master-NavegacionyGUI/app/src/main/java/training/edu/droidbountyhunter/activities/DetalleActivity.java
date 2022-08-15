package training.edu.droidbountyhunter.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import training.edu.droidbountyhunter.R;

public class DetalleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se obtiene la información del Intent...
        Bundle bundleExtras = getIntent().getExtras();
        // Se pone el nombre del Fugitivo como título...
        setTitle(bundleExtras.getString("title"));
        setContentView(R.layout.activity_detalle);
        TextView label = findViewById(R.id.labelMessage);
        // Se identifica si es Fugitivo o Capturado para el mensaje...
        if(bundleExtras.getInt("status") == 0)
            label.setText("El fugitivo sigue suelto...");
        else
            label.setText("Atrapado!!!");

    }

}
