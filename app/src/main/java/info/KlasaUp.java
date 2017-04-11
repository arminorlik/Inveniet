package info;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.inveniet.R;

/**
 * Created by Administrator on 2015-07-07.
 */
public class KlasaUp extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentup,container,false);
        ImageButton btnback = (ImageButton) view.findViewById(R.id.ibBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Po wciśnięciu przycisku powrócę do okna z mapą", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
