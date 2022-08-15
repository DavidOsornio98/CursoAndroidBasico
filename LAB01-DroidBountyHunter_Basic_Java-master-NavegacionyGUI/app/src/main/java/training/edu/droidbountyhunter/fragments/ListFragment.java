package training.edu.droidbountyhunter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import training.edu.droidbountyhunter.R;
import training.edu.droidbountyhunter.activities.DetalleActivity;

/**
 * Fragment multiuso para mostrar la lista de Fugitivos o Capturados acorde
 * al argumento indicado.
 */
public class ListFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";

    public ListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Se hace referencia al Fragment generado por XML en los Layouts y
        // se instancia en una View...
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Bundle arguments = getArguments();
        final int status = arguments != null ?
                arguments.getInt(ARG_SECTION_NUMBER) : 0;

        ListView lista = view.findViewById(R.id.listFugitivosAtrapados);
        String[] dummyData = new String[6];
        // Datos en HardCode...
        dummyData[0] = "Armando Olmos";
        dummyData[1] = "Guillermo Ortega";
        dummyData[2] = "Carlos Martinez";
        dummyData[3] = "Moises Rivas";
        dummyData[4] = "Adrián Rubiera";
        dummyData[5] = "Victor Medina";

        ArrayAdapter<String> aList = new ArrayAdapter<>(
                requireContext(), R.layout.item_fugitivo_list, dummyData);
        lista.setAdapter(aList);
        // Se genera el Listener para el detalle de cada elemento...
        lista.setOnItemClickListener((adapterView, view1, position, id) -> {
            Intent intent = new Intent(getActivity(), DetalleActivity.class);
            intent.putExtra("title", ((TextView) view1).getText());
            intent.putExtra("status", status);
            startActivity(intent);
        });

        return view;
    }
}





