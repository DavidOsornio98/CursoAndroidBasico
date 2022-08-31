package training.edu.droidbountyhunter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import training.edu.droidbountyhunter.R;
import training.edu.droidbountyhunter.activities.DetalleActivity;
import training.edu.droidbountyhunter.activities.Home;
import training.edu.droidbountyhunter.data.DatabaseBountyHunter;
import training.edu.droidbountyhunter.interfaces.OnTaskListener;
import training.edu.droidbountyhunter.models.Fugitivo;
import training.edu.droidbountyhunter.network.JSONUtils;
import training.edu.droidbountyhunter.network.NetServices;

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

        /*ListView lista = view.findViewById(R.id.listFugitivosAtrapados);
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
        });*/
        final ListView lista = (ListView) view.findViewById(R.id.listFugitivosAtrapados);
        // Se actualiza los valores de la lista con la base de datos
        UpdateList(lista, status);

        // Se genera el Listener para el detalle de cada elemento...
        //lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                lista.setOnItemClickListener((adapterView, view1, position, id) -> {
                ArrayList<Fugitivo> fugitivos = (ArrayList<Fugitivo>) lista.getTag();
                Fugitivo fugitivo = fugitivos.get(position);
                Intent intent = new Intent(getActivity(), DetalleActivity.class);
                intent.putExtra("fugitivo", fugitivo);
                ((Home) requireActivity()).result.launch(intent);
            //}
        });
        return view;
    }
    private void UpdateList(ListView list, int status) {
        DatabaseBountyHunter database = new DatabaseBountyHunter(getContext());
        ArrayList<Fugitivo> fugitivos = database.GetFugitivos(status == 1);
        if (fugitivos.size() > 0) {
            String[] data = new String[fugitivos.size()];
            for (int i = 0; i < fugitivos.size(); i++) {
                data[i] = fugitivos.get(i).getName();

                //Lab03
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_list_item_1, data);
                list.setAdapter(adapter);
                list.setTag(fugitivos);
            }
            //Lab03
        } else if (status == 0) { // La base de datos se encuentra vacía entonces
                //hara la llamada al servicio web
                NetServices apiCall = new NetServices(new OnTaskListener() {
                @Override
                public void OnTaskCompleted(String response) {
                        JSONUtils.parseFugitivos(response, getContext());
                        UpdateList(list, status);
                }

                @Override
                public void OnTaskError(int code, String message) {
                    Toast.makeText(getContext(),"Ocurrió un problema con el WebService!!!" +
                                    " --- Código de error: " + code + "\nMensaje: " + message, Toast.LENGTH_LONG).show();
                }
                });
                apiCall.execute("Fugitivos");
            }
        }

            /*ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_list_item_1, data);
            list.setAdapter(adapter);
            list.setTag(fugitivos);*/
}






