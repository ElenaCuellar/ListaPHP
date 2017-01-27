package com.example.caxidy.listaphp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String IP_Server;
    private String url_consulta;
    private JSONArray jSONArrayGeneros, jSONArrayPelis;
    private DevuelveJSON devuelveJSON;

    //Variables de la lista expandable
    ExpandableListView listaExpandable;
    ExpandableListAdapter adaptadorExpandable;
    List<String> listaCabeceras;
    HashMap<String,List<Pelicula>> listaHijos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Recuperar la IP de las preferencias
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        IP_Server = sharedPref.getString("ipServer","iesayala.ddns.net");

        url_consulta = "http://" + IP_Server + "/ElenaCuellar/consultaPelis.php";

        devuelveJSON = new DevuelveJSON();

        //Obtener el ExpandableListView
        listaExpandable = (ExpandableListView) findViewById(R.id.listExpandable);

        //Crear la lista de cabeceras y la de los hijos
        listaCabeceras = new ArrayList<>();
        listaHijos = new HashMap<>();

        //Listener de la lista Expandable (al pulsar en un hijo):
        listaExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //Sacar en un dialog la informacion de la pelicula seleccionada
                Pelicula peli;
                if(listaHijos.size()>0 && adaptadorExpandable!=null){
                    peli = (Pelicula) adaptadorExpandable.getChild(groupPosition,childPosition);
                    mostrarDialog(peli);
                }
                return false;
            }
        });
        //Listener para cuando se expande un grupo:
        listaExpandable.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {}
        });
        //Listener al contraer un grupo:
        listaExpandable.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {}
        });

        comprobarConexion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.preferencias)
            abrirPreferencias();
        return super.onOptionsItemSelected(item);
    }

    public void abrirPreferencias(){
        Intent i = new Intent(this,ActivityPref.class);
        startActivity(i);
    }

    public void mostrarDialog(Pelicula peli){

        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(this);
        alertDialogBu.setTitle(peli.getNombre());
        alertDialogBu.setMessage(getString(R.string.categ)+peli.getCategoria()+"\n\n"+getString(R.string.val)+peli.getValoracion()+"\n\n"+
                getString(R.string.sinops)+ peli.getSinopsis());
        alertDialogBu.setIcon(R.mipmap.ic_launcher);

        alertDialogBu.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });
        AlertDialog alertDialog = alertDialogBu.create();
        alertDialog.show();
    }

    public void comprobarConexion() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //recuperar los datos requeridos
            new ListaPeliculas().execute();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.errCon), Toast.LENGTH_LONG).show();
        }
    }

    class ListaPeliculas extends AsyncTask<String, String, JSONArray[]> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.carg));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONArray[] doInBackground(String... args) {
            try {
                JSONArray[] arrayJSONs = new JSONArray[2];
                //JSON de las cabeceras: generos de las peliculas
                HashMap<String, String> parametrosPostG = new HashMap<>();
                parametrosPostG.put("ins_sql", "Select g.idGenero, g.Nombre from Generos g");
                jSONArrayGeneros = devuelveJSON.sendRequest(url_consulta,
                        parametrosPostG);
                if (jSONArrayGeneros != null) {
                    arrayJSONs[0] = jSONArrayGeneros;

                    //JSON de los hijos: peliculas
                    HashMap<String, String> parametrosPostP = new HashMap<>();
                    parametrosPostP.put("ins_sql",
                            "Select p.idPelicula, p.Nombre, p.Valoracion, p.Sinopsis, (SELECT Nombre from Generos WHERE idGenero=p.Genero) as Categoria from Peliculas p");
                    jSONArrayPelis = devuelveJSON.sendRequest(url_consulta,
                            parametrosPostP);
                    if (jSONArrayPelis != null) {
                        arrayJSONs[1] = jSONArrayPelis;
                        return arrayJSONs;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONArray[] json) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if(json!=null) {
                if (json[0] != null) {

                    Toast.makeText(MainActivity.this, getString(R.string.generosok), Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < json[0].length(); i++) {
                        try {
                            JSONObject jsonObject = json[0].getJSONObject(i);
                            Genero genero = new Genero();
                            genero.setIdGenero(jsonObject.getInt("idGenero"));
                            genero.setNombre(jsonObject.getString("Nombre"));
                            listaCabeceras.add(genero.getNombre());

                            ArrayList<Pelicula> grupoPelis = new ArrayList<>();

                            if (json[1] != null) {
                                for (int j = 0; j < json[1].length(); j++) {
                                    JSONObject jsonObject2 = json[1].getJSONObject(j);

                                    if(jsonObject2.getString("Categoria").equals(genero.getNombre())) {
                                        Pelicula pelicula = new Pelicula();
                                        pelicula.setIdPelicula(jsonObject2.getInt("idPelicula"));
                                        pelicula.setValoracion(jsonObject2.getInt("Valoracion"));
                                        pelicula.setNombre(jsonObject2.getString("Nombre"));
                                        pelicula.setSinopsis(jsonObject2.getString("Sinopsis"));
                                        pelicula.setCategoria(jsonObject2.getString("Categoria"));
                                        grupoPelis.add(pelicula);
                                    }
                                }
                                listaHijos.put(listaCabeceras.get(i), grupoPelis);
                                //Poner el adaptador
                                adaptadorExpandable = new ExpandableListAdapter(getApplicationContext(),listaCabeceras,listaHijos);
                                listaExpandable.setAdapter(adaptadorExpandable);
                            } else {
                                Toast.makeText(MainActivity.this, getString(R.string.jsonnulo), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.pelisok), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.jsonnulo), Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(MainActivity.this, getString(R.string.arraynulo), Toast.LENGTH_LONG).show();
            }
        }
    }
}
