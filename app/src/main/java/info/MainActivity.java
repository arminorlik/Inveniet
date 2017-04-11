
package info;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.inveniet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends Activity implements KlasaDown.klasadownlistener {
    public String category;
    public String source; //nazwa lokalu (pobrana z wyboru mapy)
    public String[] placepic; //link do zdjęcia lokalu
    public String[] promopic; //link do zdjęcia promocji
    public String nazwa; // informacje o nazwie lokalizacji
    public String info; // informacje o lokalizacji z SQL'a
    public String infopromo; // informacje o lokalizacji z SQL'a
    public String kontakt; // kontakt do lokalizacji
    public String godziny; // informacje o godzinach pracy lokalizacji
    public double longitude; //dlugość geograficzna
    public double latitude; //szerokość geograficzna

    private ArrayList<HashMap<String, String>> mGodzinyPracy;

    String MarkerClicked;
    public boolean jestsiec = false;
    ConnectionDetector sprawdzsiec;
    int firstrun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);
        MarkerClicked = getIntent().getExtras().getString("selectedPlace");
        placepic = getIntent().getExtras().getStringArray("placepic");
        promopic = getIntent().getExtras().getStringArray("promopic");
        category = getIntent().getExtras().getString("selectedCategory");
        setPlacepic(placepic);
        setPromopic(promopic);
        setFirstrun(0);
        mGodzinyPracy = new ArrayList<HashMap<String, String>>();
        source = MarkerClicked;
        //sprawdzam czy jest połączenie z internetem
        sprawdzsiec = new ConnectionDetector(getApplicationContext());
        SprawdzInternet();//jest jest internet uruchom Asyntask
        ImageButton btnBack = (ImageButton) findViewById(R.id.ibBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (promopic[0] == null){
            zmienImEmpty();
        }
        else
        {
            zmienIm();
        }
    }

    @Override
    public void zmienIm() {
        KlasaCenter fragment = (KlasaCenter) getFragmentManager()
                .findFragmentById(R.id.fragmentcenter);
        if (fragment != null && fragment.isInLayout()) {
            fragment.UstawIm(placepic);
        }
    }

    public void zmienImEmpty() {
        String test = "";
        String[] testarray = new String[3];
        if (category.equals("1")) test = "android.resource://com.inveniet/drawable/restauracja";
        if (category.equals("2")) test = "android.resource://com.inveniet/drawable/kawiarnia";
        if (category.equals("3")) test = "android.resource://com.inveniet/drawable/bar";
        if (category.equals("4")) test = "android.resource://com.inveniet/drawable/fastfood";
        if (category.equals("5")) test = "android.resource://com.inveniet/drawable/club";
        if (category.equals("6")) test = "android.resource://com.inveniet/drawable/party";
        if (category.equals("7")) test = "android.resource://com.inveniet/drawable/music";
        if (category.equals("8")) test = "android.resource://com.inveniet/drawable/concert";
        testarray[0] = test;
        testarray[1] = test;
        testarray[2] = test;
        KlasaCenter fragment = (KlasaCenter) getFragmentManager()
                .findFragmentById(R.id.fragmentcenter);
        if (fragment != null && fragment.isInLayout()) {
            fragment.UstawIm(testarray);
        }
    }

    public void zmienIm2() {

        KlasaCenter fragment = (KlasaCenter) getFragmentManager()
                .findFragmentById(R.id.fragmentcenter);
        if (fragment != null && fragment.isInLayout()) {
            fragment.UstawIm(placepic);
        }
    }

    private void danezSQL(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);

                //szukam nazwy lokalu w bazie danych i porównuję z wybraną nazwą lokalu
                if (json.getString("name").equals(source)) {
                    nazwa = json.getString("name");
                    info = json.getString("description");
                    infopromo = json.getString("promotion");
                    godziny = json.getString("hours");
                    kontakt = json.getString("contact_data");
                    longitude = Double.parseDouble(json.getString("long"));
                    latitude = Double.parseDouble(json.getString("lat"));

                    setNazwa(nazwa);
                    setInfo(info);
                    setInfopromo(infopromo);

                    setGodziny(godziny);
                    setKontakt(kontakt);
                    setLatitude(latitude);
                    setLongitude(longitude);

                    JSONObject obj3 = new JSONObject(godziny);
                    JSONObject obj4 = null;
                    //Getting all the keys inside json object with keys- from and to
                    Iterator<String> keys = obj3.keys();
                    int help = 0;
                    while (keys.hasNext()) {
                        String keyValue = keys.next();
                        obj4 = obj3.getJSONObject(keyValue);
                        //getting string values with keys- from and to
                        String from = obj4.getString("from");
                        String to = obj4.getString("to");
                        int hourstart = Integer.parseInt(from);
                        int hoursclose = Integer.parseInt(to);

                        int hoursst = hourstart / 60;
                        int minutesst = hourstart % 60;
                        String minute = Integer.toString(minutesst);
                        if (minute.equals("0")) minute = "00";

                        int hourscl = hoursclose / 60;
                        int minutescl = hoursclose % 60;
                        String minute2 = Integer.toString(minutescl);
                        if (minute2.equals("0")) minute2 = "00";

                        HashMap<String, String> map = new HashMap<String, String>();
                        if (help == 0) map.put("day", "poniedziałek");
                        if (help == 1) map.put("day", "wtorek");
                        if (help == 2) map.put("day", "środa");
                        if (help == 3) map.put("day", "czwartek");
                        if (help == 4) map.put("day", "piątek");
                        if (help == 5) map.put("day", "sobota");
                        if (help == 6) map.put("day", "niedziela");
                        map.put("od", Integer.toString(hoursst) + ":" + minute);
                        map.put("do", Integer.toString(hourscl) + ":" + minute2);
                        mGodzinyPracy.add(map);
                        help++;
                    }
                    setmGodzinyPracy(mGodzinyPracy);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void SprawdzInternet() {
        jestsiec = sprawdzsiec.isConnectingToInternet();

        if (!jestsiec) {
            Toast.makeText(this, "Wymagane połączenie z internetem",
                    Toast.LENGTH_LONG).show();
        } else {//jesli połączenie z internetem jest aktywne

            new getPlacePic().execute(new ApiConnector());//uruchom Asynctask
        }
    }

    private class getPlacePic extends AsyncTask<ApiConnector, Long, JSONArray> {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            return params[0].getPlacePic();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            danezSQL(jsonArray);
            zmienIm2();
        }
    }

    /* GETTERY I SETTERY
    @
    @
    @
    @
    */
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getInfopromo() {
        return infopromo;
    }

    public void setInfopromo(String infopromo) {
        this.infopromo = infopromo;
    }

    public String[] getPlacepic() {
        return placepic;
    }

    public void setPlacepic(String[] placepic) {
        this.placepic = placepic;
    }

    public void setPromopic(String[] promopic) {
        this.promopic = promopic;
    }

    public String[] getPromopic() {
        return promopic;
    }

    public String getGodziny() {
        return godziny;
    }

    public void setGodziny(String godziny) {
        this.godziny = godziny;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getFirstrun() {
        return firstrun;
    }

    public void setFirstrun(int firstrun) {
        this.firstrun = firstrun;
    }

    public ArrayList<HashMap<String, String>> getmGodzinyPracy() {
        return mGodzinyPracy;
    }

    public void setmGodzinyPracy(ArrayList<HashMap<String, String>> mGodzinyPracy) {
        this.mGodzinyPracy = mGodzinyPracy;
    }
}


