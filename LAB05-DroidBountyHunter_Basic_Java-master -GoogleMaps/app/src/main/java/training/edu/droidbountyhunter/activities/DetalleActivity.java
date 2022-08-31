package training.edu.droidbountyhunter.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import training.edu.droidbountyhunter.utils.PictureTools;

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
        Button takePhotoButton = findViewById(R.id.buttonPhoto);


        // Se identifica si es Fugitivo o Capturado para el mensaje...
        if(fugitivo.getStatus().equalsIgnoreCase("0")) {
            label.setText("El fugitivo sigue suelto...");
        }else {
            capturarButton.setVisibility(View.GONE);
            takePhotoButton.setVisibility(View.GONE);
            label.setText("Atrapado!!!");
            ImageView photoImageView = findViewById(R.id.pictureFugitive);
            String pathPhoto = fugitivo.getPhoto();
            if (pathPhoto != null && pathPhoto.length() > 0) {
                Bitmap bitmap = PictureTools.decodeSampledBitmapFromUri(pathPhoto, 200, 200);
                photoImageView.setImageBitmap(bitmap);
            }

        }
    }

    public void OnCaptureClick(View view) {
        fugitivo.setStatus("1");
        String pathPhoto = fugitivo.getPhoto();
        if (pathPhoto == null || pathPhoto.length() == 0) {
            Toast.makeText(this, "Es necesario tomar la foto antes de capturar al fugitivo",
                    Toast.LENGTH_LONG).show();
            return;
        }

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
        //finish();

    }

    public void OnDeleteClick(View view) {
        database.DeleteFugitivo(fugitivo.getId());
        //setResult(0);
        setResult(RESULT_OK);
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

    public ActivityResultLauncher<Intent> resultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode()==RESULT_OK){
                    fugitivo.setPhoto(PictureTools.currentPhotoPath);
                    ImageView imageFugitive = findViewById(R.id.pictureFugitive);
                    Bitmap bitmap = PictureTools.decodeSampledBitmapFromUri(PictureTools.currentPhotoPath, 200, 200);
                    imageFugitive.setImageBitmap(bitmap);

                }
            });

    public void OnFotoClick(View view) {
        if (PictureTools.permissionReadMemmory(this)) {
            dispatchPicture();
        }
    }

    private void dispatchPicture() {
        Uri pathImage = PictureTools.with(this).getOutputMediaFileUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pathImage);
        resultPicture.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGrantedAllPermissions = true;
        for (int grantResult : grantResults) {
            isGrantedAllPermissions = isGrantedAllPermissions && (grantResult == PackageManager.PERMISSION_GRANTED);
        }
        if (isGrantedAllPermissions && requestCode == PictureTools.REQUEST_CODE) {
            dispatchPicture();
        }
    }



}
