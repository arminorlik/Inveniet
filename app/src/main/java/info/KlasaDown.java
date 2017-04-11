package info;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.inveniet.R;

/**
 * Created by Administrator on 2015-07-07.
 */
public class KlasaDown extends Fragment {

    private klasadownlistener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentdown, container, false);
        ImageButton btnOpis = (ImageButton) view.findViewById(R.id.btnOpis);
        ImageButton btnPromo = (ImageButton) view.findViewById(R.id.btnPromo);
        ImageButton btnCzynne = (ImageButton) view.findViewById(R.id.btnCzynne);
        ImageButton btnKontakt = (ImageButton) view.findViewById(R.id.btnKontakt);

        final KlasaCenter klasacenter = (KlasaCenter) getFragmentManager().findFragmentById(R.id.fragmentcenter);
        final MainActivity main = (MainActivity) getActivity();

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btnOpis:
                        klasacenter.zmienOpis(main.getInfo());
                        klasacenter.zmienInfo("Opis");
                        changeIm();
                        break;
                    case R.id.btnPromo:
                        klasacenter.UstawIm(main.getPromopic());
                        klasacenter.zmienInfo("Promocje");
                        klasacenter.zmienOpis(main.getInfopromo());

                        break;
                    case R.id.btnCzynne:
                        String display = "";
                        klasacenter.UstawIm(main.getPlacepic());
                        klasacenter.zmienInfo("Godziny otwarcia");

                        for (java.util.Map<String, String> mListaMar : main.getmGodzinyPracy()) {
                            display = display + "\n" + mListaMar.get("day") + " " + mListaMar.get("od") + " " + mListaMar.get("do");

                        }
                        klasacenter.zmienOpis(display);
                        break;
                    case R.id.btnKontakt:
                        klasacenter.UstawIm(main.getPlacepic());
                        klasacenter.zmienInfo("Kontakt");
                        klasacenter.zmienOpis(main.getKontakt());
                        break;
                    default:
                        break;
                }
            }
        };

        btnOpis.setOnClickListener(clickListener);
        btnKontakt.setOnClickListener(clickListener);
        btnPromo.setOnClickListener(clickListener);
        btnCzynne.setOnClickListener(clickListener);

        return view;
    }

    public void changeIm() {
        listener.zmienIm();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof klasadownlistener) {
            listener = (klasadownlistener) activity;
        } else {
            throw new ClassCastException(activity.toString() + "Błąd onAttach w klasie KlasaDown");
        }
    }

    public interface klasadownlistener {
        void zmienIm();
    }
}




