package training.edu.droidbountyhunter.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import training.edu.droidbountyhunter.R;
import training.edu.droidbountyhunter.data.DatabaseBountyHunter;
import training.edu.droidbountyhunter.models.Fugitivo;


public class AgregarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Nuevo Fugitivo");
        setContentView(R.layout.activity_agregar);
    }

    public void OnSaveClick(View view) {
        TextView name = findViewById(R.id.nameFugitiveEditText);
        if (name.getText().toString().length() > 0) {
            DatabaseBountyHunter database = new DatabaseBountyHunter(this);
            database.InsertFugitivo(new Fugitivo(0, name.getText().toString(), "0"));
            setResult(RESULT_OK);
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Alerta")
                    .setMessage("Favor de capturar el nombre del fugitivo.")
                    .show();
        }

    }

}
