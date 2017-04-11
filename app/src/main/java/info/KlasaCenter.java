
package info;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inveniet.Methods;
import com.inveniet.R;

public class KlasaCenter extends Fragment {
    public String url;
    Methods met;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentcenter, container, false);
        return view;

    }

    public void UstawIm(String[] picurl) {
        TextView tvinfo = (TextView) getView().findViewById(R.id.tvInfoMore);
        TextView tvnazwa = (TextView) getView().findViewById(R.id.tvNazwa);
        MainActivity main = (MainActivity) getActivity();
        SlideShowView img = (SlideShowView) getView().findViewById(R.id.IvCenter);
        if (main.getFirstrun()==0){
            img.job();
            main.setFirstrun(1);
        }
        if (picurl != null) { //jeśli adres placepic jest pusty wczytaj domyślną grafikę
            img.start(picurl);

            tvinfo.setText(main.getInfo());
            tvnazwa.setText(main.getNazwa());
        } else {

            tvinfo.setText(met.getInfo());
            tvnazwa.setText(met.getNazwa());
        }
    }

    public void zmienOpis(String op) {
        TextView tvInfo = (TextView) getView().findViewById(R.id.tvInfoMore);
        tvInfo.setText(op);
    }

    public void zmienInfo(String info) {
        TextView tvOpis = (TextView) getView().findViewById(R.id.tvInfo);
        tvOpis.setText(info);
    }
}

