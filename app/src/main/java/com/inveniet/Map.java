package com.inveniet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import info.ApiConnectorImg;

public class Map extends FragmentActivity implements
        GoogleMap.OnMarkerClickListener {
    private static final String TAG_OBIEKT = "name";
    private static final String TAG_ID = "place_type_id";
    private static final String TAG_LONG = "long";
    private static final String TAG_LAT = "lat";
    private static final String TAG_HOURS = "hours";
    private static final String TAG_POSTS = "posts";
    private static final String CZYTAJ_LISTE_OBIEKTOW = "http://www.bakusek.zz.mu/webservice/map.php";
    private static final String CZYTAJ_LISTE_BAR = "http://www.bakusek.zz.mu/webservice/map_bar.php";
    private static final String CZYTAJ_LISTE_KAW = "http://www.bakusek.zz.mu/webservice/map_kaw.php";
    private static final String CZYTAJ_LISTE_CLUB = "http://www.bakusek.zz.mu/webservice/map_club.php";
    private static final String CZYTAJ_LISTE_FAST = "http://www.bakusek.zz.mu/webservice/map_fast.php";
    private static final String CZYTAJ_LISTE_MUSIC = "http://www.bakusek.zz.mu/webservice/map_music.php";
    private static final String CZYTAJ_LISTE_PARTY = "http://www.bakusek.zz.mu/webservice/map_party.php";
    private static final String CZYTAJ_LISTE_CONCERT = "http://www.bakusek.zz.mu/webservice/map_concert.php";

    public String id;
    public String lat;
    public String longi;
    public String name;
    public String category;
    public String godz;
    public String dzien;

    public String nazwa; // informacje o nazwie lokalizacji
    public String info; // informacje o lokalizacji z SQL'a
    public String infopromo; // informacje o lokalizacji z SQL'a
    public String kontakt; // kontakt do lokalizacji
    public String godziny; // informacje o godzinach pracy lokalizacji

    public String[] placepic; //link do zdjęcia lokalu
    public String[] promopic; //link do zdjęcia promocji

    public String markerTitle;
    LocationManager locationManager;
    double szer, dlug;
    private GoogleMap mMap;
    private JSONArray Obiekty = null;
    private ProgressDialog pDialog;
    private ArrayList<HashMap<String, String>> mListaObiektow;
    private ArrayList<HashMap<String, String>> mGodzinyPracy;

    RelativeLayout mapinfo;
    ImageView small_pic;
    TextView tvNazwa, tvTyp, tvAdres;
    Methods met;
    info.MainActivity main;
    Calendar c;
    int dayOfWeek;
    String open, close, today;
    SimpleDateFormat sdf;
    public String weekDay = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        small_pic = (ImageView) findViewById(R.id.ivMini);
        tvNazwa = (TextView) findViewById(R.id.tvNazwaMap);
        tvTyp = (TextView) findViewById(R.id.tvTypMap);
        main = new info.MainActivity();
        c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        today = (String) android.text.format.DateFormat.format(
                "kk:mm", new java.util.Date());
        sdf = new SimpleDateFormat("kk:mm");
        mGodzinyPracy = new ArrayList<HashMap<String, String>>();

        category = getIntent().getExtras().getString("selectedItems");
        mapinfo = (RelativeLayout) findViewById(R.id.layMapInfo);
        mapinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getMarkerTitle() == null) {
                    Toast.makeText(getApplicationContext(), "Wybierz marker na mapie", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(getApplicationContext(),
                            info.MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString("selectedPlace", getMarkerTitle());
                    b.putString("selectedCategory", category);
                    b.putStringArray("placepic", placepic);
                    b.putStringArray("promopic", promopic);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

        ustawMape();

        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();
            return;
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // Check if first run
        if (savedInstanceState == null) {
            // Prevent map from resetting when screen rotated
            supportMapFragment.setRetainInstance(true);
        }
        new WczytajObiektyAsyncT().execute();
        met = new Methods();

        //ustawienie domyślnej miniaturki dla odpowiedniej kategorii
        if (category.equals("1")) small_pic.setImageResource(R.drawable.restauracja);
        if (category.equals("2")) small_pic.setImageResource(R.drawable.kawiarnia);
        if (category.equals("3")) small_pic.setImageResource(R.drawable.bar);
        if (category.equals("4")) small_pic.setImageResource(R.drawable.fastfood);
        if (category.equals("5")) small_pic.setImageResource(R.drawable.club);
        if (category.equals("6")) small_pic.setImageResource(R.drawable.party);
        if (category.equals("7")) small_pic.setImageResource(R.drawable.music);
        if (category.equals("8")) small_pic.setImageResource(R.drawable.concert);

        //ustawienie kalendarza z aktualnym dniem dla markera
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "poniedziałek";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "wtorek";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "środa";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "czwartek";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "piątek";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "sobota";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "niedziela";
        }
    }

    private void ustawMape() {

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                // Create a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Get the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                // Get Current Location
                Location myLocation = locationManager.getLastKnownLocation(provider);
                if (myLocation != null) {
                    LatLng ll = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(ll)
                            .zoom(14).build();
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                } else {
                    LatLng ll = new LatLng(52.230625, 21.013129);//WAWA
                    //LatLng ll = new LatLng(53.363631, 17.040005);//Złotów
                    CameraPosition cp = new CameraPosition.Builder()
                            .target(ll)
                            .zoom(6).build();
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cp));
                }
                mMap.setOnMarkerClickListener(this);
            }
        }
    }

    public void updateJSONdata() {

        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..

        mListaObiektow = new ArrayList<HashMap<String, String>>();

        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        JSONObject json = null;

        // sprawdzam która kategoria została wybrana i wczytuję odpowiednie obiekty do mapy
        if (category.equals("1")) json = jParser.getJSONFromUrl(CZYTAJ_LISTE_OBIEKTOW);
        if (category.equals("2")) json = jParser.getJSONFromUrl(CZYTAJ_LISTE_KAW);
        if (category.equals("3")) json = jParser.getJSONFromUrl(CZYTAJ_LISTE_BAR);
        if (category.equals("4")) json = jParser.getJSONFromUrl(CZYTAJ_LISTE_FAST);
        if (category.equals("5")) json = jParser.getJSONFromUrl(CZYTAJ_LISTE_CLUB);
        if (category.equals("6")) json = jParser.getJSONFromUrl(CZYTAJ_LISTE_PARTY);
        if (category.equals("7")) json = jParser.getJSONFromUrl(CZYTAJ_LISTE_MUSIC);
        if (category.equals("8")) json = jParser.getJSONFromUrl(CZYTAJ_LISTE_CONCERT);

        // when parsing JSON stuff, we should probably
        // try to catch any exceptions:
        try {

            // I know I said we would check if "Posts were Avail." (success==1)
            // before we tried to read the individual posts, but I lied...
            // mComments will tell us how many "posts" or comments are
            // available
            Obiekty = json.getJSONArray(TAG_POSTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < Obiekty.length(); i++) {
                JSONObject c = Obiekty.getJSONObject(i);

                // gets the content of each tag
                String id = c.getString(TAG_ID);
                String obiekt = String.valueOf(c.getString(TAG_OBIEKT));
                String dlug = String.valueOf(c.getDouble((TAG_LONG)));
                String szer = String.valueOf(c.getDouble((TAG_LAT)));
                String godziny = String.valueOf(c.getString(TAG_HOURS));

                // Toast.makeText(getApplicationContext(), "asdasda" ,
                // Toast.LENGTH_SHORT).show();
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_ID, id);
                map.put(TAG_OBIEKT, obiekt);
                map.put(TAG_LONG, dlug);
                map.put(TAG_LAT, szer);
                map.put(TAG_HOURS, godziny);

                // adding HashList to ArrayList
                mListaObiektow.add(map);

                // annndddd, our JSON data is up to date same with our array
                // list
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateList() {
        String resultArr = tablica();

        for (java.util.Map<String, String> mListaMar : mListaObiektow) {
            id = mListaMar.get(TAG_ID);
            lat = mListaMar.get(TAG_LAT);
            longi = mListaMar.get(TAG_LONG);
            name = mListaMar.get(TAG_OBIEKT);
            godz = mListaMar.get(TAG_HOURS);

            //if (id.equals(resultArr)) {
            ustawMape2();
            //}
            id = lat = longi = name = godz = "";
        }
    }

    public String getMarkerTitle() {
        return markerTitle;
    }

    public void setMarkerTitle(String markerTitle) {
        this.markerTitle = markerTitle;
    }

    public double zwroc_szer() {
        double szer = Double.parseDouble(lat);
        return szer;
    }

    public double zwroc_dlug() {
        double dlug = Double.parseDouble(longi);
        return dlug;
    }

    public String zwroc_nazwe() {
        String nazwa = name;
        return nazwa;
    }

    public String tablica() {
        Bundle b = getIntent().getExtras();
        String resultArr = b.getString("selectedItems");
        return resultArr;
    }

    public void Marker() {

        JSONObject obj3 = null;
        try {
            obj3 = new JSONObject(godz);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject obj4 = null;
        //Getting all the keys inside json object with keys- from and to
        Iterator<String> keys = obj3.keys();
        int help = 0;
        while (keys.hasNext()) {
            String keyValue = keys.next();
            try {
                obj4 = obj3.getJSONObject(keyValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //getting string values with keys- from and to
            String from = null;
            try {
                from = obj4.getString("from");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String to = null;
            try {
                to = obj4.getString("to");
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            if (help == 0) map.put("dayOfWeek", "poniedziałek");
            if (help == 1) map.put("dayOfWeek", "wtorek");
            if (help == 2) map.put("dayOfWeek", "środa");
            if (help == 3) map.put("dayOfWeek", "czwartek");
            if (help == 4) map.put("dayOfWeek", "piątek");
            if (help == 5) map.put("dayOfWeek", "sobota");
            if (help == 6) map.put("dayOfWeek", "niedziela");
            map.put("od", Integer.toString(hoursst) + ":" + minute);
            map.put("do", Integer.toString(hourscl) + ":" + minute2);
            mGodzinyPracy.add(map);
            help++;
        }

        for (java.util.Map<String, String> mListaMar : mGodzinyPracy) {
            open = mListaMar.get("od");
            close = mListaMar.get("do");
            if (close.equals("24:00")) close = "23:59";
            dzien = mListaMar.get("dayOfWeek");

            try {
                Date timeOpen = sdf.parse(open);
                Date timeClose = sdf.parse(close);
                Date timeActual = sdf.parse(today);

                if (dzien.equals(weekDay)) {
                    if (timeActual.after(timeOpen) && timeActual.before(timeClose)) {
                        MarkerOptions opcje = new MarkerOptions().title(name)
                                .position(new LatLng(szer, dlug))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markeropen));
                        mMap.addMarker(opcje);
                    } else {
                        MarkerOptions opcje = new MarkerOptions().title(name)
                                .position(new LatLng(szer, dlug))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerclose));
                        mMap.addMarker(opcje);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
        setMarkerTitle(marker.getTitle());
        met.setPlacepic(null);

        if (category.equals("1")) tvTyp.setText("Restauracja");
        if (category.equals("2")) tvTyp.setText("Kawiarnia");
        if (category.equals("3")) tvTyp.setText("Bar");
        if (category.equals("4")) tvTyp.setText("FastFood");
        if (category.equals("5")) tvTyp.setText("Klub");
        if (category.equals("6")) tvTyp.setText("Impreza");
        if (category.equals("7")) tvTyp.setText("Muzyka");
        if (category.equals("8")) tvTyp.setText("Koncert");
        tvNazwa.setText(marker.getTitle());//ustaw nazwę lokalu w polu nazwy wyciągając ją z nazwy markera
        new getPlacePic().execute(new ApiConnectorImg()); //uruchom Asynctask z pobraniem linku do zdjęć
        return false;
    }

    public void ustawMape2() {

        dlug = zwroc_dlug();
        szer = zwroc_szer();

        Marker();
    }

    private void ImagezSQL(JSONArray jsonArray) {
        String[] tab_place = new String[jsonArray.length()];
        String[] tab_promo = new String[jsonArray.length()];
        int a = 0;
        int b = 0;
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);

                //szukam nazwy lokalu w bazie danych i porównuję z wybraną nazwą lokalu
                if (json.getString("place_name").equals(getMarkerTitle())) {
                    if (json.getString("place_picture").equals("1")) {

                        tab_place[a] = "http://www.bakusek.zz.mu/uploads/" + json.getString("source");
                        met.setPlacepic(new String[]{"http://www.bakusek.zz.mu/uploads/" + json.getString("source")});
                        a++;
                    }

                    if (json.getString("promo_picture").equals("1")) {

                        tab_promo[b] = "http://www.bakusek.zz.mu/uploads/" + json.getString("source");
                        met.setPromopic(new String[]{"http://www.bakusek.zz.mu/uploads/" + json.getString("source")});
                        b++;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        placepic = tab_place;
        promopic = tab_promo;

        a = 0;
        b = 0;
    }

    public class WczytajObiektyAsyncT extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Map.this);
            pDialog.setMessage("Wczytuję dane...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            ustawMape();
            //idzDoPolozenia(52.249665, 21.012511, 10);
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            // we will develop this method in version 2

            updateJSONdata();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            try {
                pDialog.dismiss();
                pDialog = null;
            } catch (Exception e) {

            }
            updateList();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private class getPlacePic extends AsyncTask<ApiConnectorImg, Long, JSONArray> {
        @Override
        protected JSONArray doInBackground(ApiConnectorImg... params) {
            return params[0].getPlacePic();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            ImagezSQL(jsonArray);
            UstawIm(met.getPlacepic());
        }
    }

    public void UstawIm(String[] picurl) {

        String test = "";
        if (category.equals("1")) test = "android.resource://com.inveniet/drawable/restauracja";
        if (category.equals("2")) test = "android.resource://com.inveniet/drawable/kawiarnia";
        if (category.equals("3")) test = "android.resource://com.inveniet/drawable/bar";
        if (category.equals("4")) test = "android.resource://com.inveniet/drawable/fastfood";
        if (category.equals("5")) test = "android.resource://com.inveniet/drawable/club";
        if (category.equals("6")) test = "android.resource://com.inveniet/drawable/party";
        if (category.equals("7")) test = "android.resource://com.inveniet/drawable/music";
        if (category.equals("8")) test = "android.resource://com.inveniet/drawable/concert";

        if (picurl != null) { //jeśli adres placepic jest pusty wczytaj domyślną grafikę
            Picasso.with(this)
                    .load(picurl[0])
                    .fit()
                    .error(R.drawable.def)
                    .into(small_pic);
        } else {
            Picasso.with(this)
                    .load(test)
                    .fit()
                    .into(small_pic);
        }
    }
}