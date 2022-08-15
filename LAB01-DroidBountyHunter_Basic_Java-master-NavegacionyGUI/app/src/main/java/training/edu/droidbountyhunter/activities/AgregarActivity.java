package training.edu.droidbountyhunter.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import training.edu.droidbountyhunter.R;


public class AgregarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Nuevo Fugitivo");
        setContentView(R.layout.activity_agregar);
    }

}
