package training.edu.droidbountyhunter.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import training.edu.droidbountyhunter.R;
import training.edu.droidbountyhunter.models.Fugitivo;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Fugitivo fugitivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fugitivo = getIntent().getParcelableExtra("fugitivo");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        setTitle(fugitivo.getName());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        LatLng position;
        if (fugitivo.getLatitude() == 0d && fugitivo.getLongitude() == 0d) {
            position = new LatLng(-34, 151);
        }else {
            position = new LatLng(fugitivo.getLatitude(),fugitivo.getLongitude());
        }
        googleMap.addMarker(new MarkerOptions().position(position)
                .title(fugitivo.getName()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position,9));
    }
}
