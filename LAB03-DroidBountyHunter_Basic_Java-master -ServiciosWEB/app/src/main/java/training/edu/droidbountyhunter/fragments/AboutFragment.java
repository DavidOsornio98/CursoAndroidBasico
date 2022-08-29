package training.edu.droidbountyhunter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.fragment.app.Fragment;

import training.edu.droidbountyhunter.R;

/**
 * Fragment con datos acerca de la App.
 */
public class AboutFragment extends Fragment {
    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se hace referencia al Fragment generado por XML en los Layouts y
        // se instancia en una View...
        View view = inflater.inflate(R.layout.fragment_acerca_de, container,
                false);
        // Se accede a los elementos ajustables del Fragment...
        RatingBar ratingBar = view.findViewById(R.id.ratingApp);

        String sRating = "0.0"; // Variable para lectura del Rating guardado
        // en el property.
        try {
            if (System.getProperty("rating") != null) {
                sRating = System.getProperty("rating");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assert sRating != null;
        if (sRating.isEmpty()) {
            sRating = "0.0";
        }
        ratingBar.setRating(Float.parseFloat(sRating));
        // Listener al Rating para la actualización de la property...
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            System.setProperty("rating", String.valueOf(rating));
            ratingBar1.setRating(rating);
        });

        return view;
    }
}
