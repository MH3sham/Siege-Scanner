package app.com.example.vip.enemyrank;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.com.example.vip.enemyrank.TextVision.OcrCaptureActivity;

public class MainActivity extends AppCompatActivity{

    ArrayList<String> playersNames = new ArrayList<String>();
    //strings for method stringManipulation(res)
    String imageProfile = null;
    String playerName = null;
    String playerlvl = null;
    String playerKD = null;
    String imageRank = null;
    String rankName = null;

    //PlayersAsynTask playersAsynTask;
    ListAdapter listAdapter;

    ArrayList<PlayerClass> playerClassArrayList = new ArrayList<PlayerClass>();

    ListView listView;
    ProgressBar progressBar;
    String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , OcrCaptureActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //initialize AndroidNetworking
        AndroidNetworking.initialize(getApplicationContext());

        listView = (ListView) findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        try{
            playersNames = (ArrayList<String>) bundle.getSerializable("ARRAYLISTkey");

        }catch (Exception e) {
            e.printStackTrace();
        }

        /*
        playersNames.add("Yooda.UNKWN");
        playersNames.add("Time2Toxic");
        playersNames.add("GunHawk.");
        playersNames.add("POTATOSENPAI111");
        playersNames.add("Al-Idrisi.");
        */
        Log.i("Names:", String.valueOf(playersNames));

        if (checkIfNoInternetDisplayDialogElseLoadMyData()){  //if true then there's internet
            for (int i=0 ; i< playersNames.size() ; i++){

                //playersAsynTask = new PlayersAsynTask();
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    //String res = playersAsynTask.execute("https://r6.tracker.network/profile/pc/" + playersNames.get(i)).get();
                    AndroidNetworking.get("https://r6.tracker.network/profile/pc/" + playersNames.get(i))
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("Response", response);
                                    stringManipulation(response); //method String Manipulation to get specific parts from it.
                                    String imgProfile = imageProfile;
                                    String plaName = playerName;
                                    String plalvl = playerlvl;
                                    String plaKD = playerKD;
                                    String imgRank = imageRank;
                                    String rankkName = rankName;
                                    PlayerClass player = new PlayerClass(imgProfile,plaName,plalvl,plaKD,imgRank,rankkName);
                                    playerClassArrayList.add(player);
                                    listAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            listAdapter = new ListAdapter(playerClassArrayList, MainActivity.this, this);
            listView.setAdapter(listAdapter);
        }
    }

    private boolean checkIfNoInternetDisplayDialogElseLoadMyData() {
        //method that check if no internet then display AlertDialg says "No internet connection!" else run the thread and get my data.
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.internet_error))
                    .setIcon(R.drawable.scanblack)
                    .setPositiveButton("Ok", null).show();
        }else{
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void stringManipulation(String res) {
        //String Manipulation to get specific parts from it.
        //Get Imgs Links.

        //String[] splitString = res.split("<div id=\"footer\" class=\"ft\">");
        //String mResultImgs = splitString[0];
        Pattern p = Pattern.compile("<meta property=\"og:image\" content=\"(.*?)\" />");
        Matcher m = p.matcher(res);
        if (m.find()){
            imageProfile = m.group(1);  //get ImageProfile
        }


        p = Pattern.compile("<meta name=\"description\" content=\"View (.*?)'s Rainbow Six Siege stats profile overview and leaderboard rankings.\">");
        m = p.matcher(res);
        if (m.find()){
            playerName = m.group(1);  //get playerName
        }
        else {
            playerName = "Not Found";
        }

        p = Pattern.compile("<div class=\"trn-defstat__name\">Level</div>\n<div class=\"trn-defstat__value\">\n(.*?)\n</div>");
        m = p.matcher(res);
        if (m.find()){
            playerlvl = m.group(1);  //get playerlvl
        }
        else {
            playerlvl = "Not Found";
        }

        p = Pattern.compile("<div class=\"trn-defstat__value\" data-stat=\"PVPKDRatio\">\n(.*?)\n</div>");
        m = p.matcher(res);
        if (m.find()){
            playerKD = m.group(1);  //get playerKD
        }
        else {
            playerKD = "Not Found";
        }


        p = Pattern.compile("<div style=\"width: 50px; margin-right: 14px;\">\n<img style=\"width: 100%\" src=\"(.*?)\" title=");  //13
        m = p.matcher(res);
        if (m.find()){
            imageRank = m.group(1);
        }

        p = Pattern.compile("<div style=\"flex-grow: 1; display: flex; justify-content: space-between; align-items: center;\">\n<div>\n<div>(.*?)</div>");
        m = p.matcher(res);
        if (m.find()){
            rankName = m.group(1);  //get rankName
        }
        else {
            rankName = "Not Found";
        }


    }


    /*
    @SuppressLint("StaticFieldLeak")
    public class PlayersAsynTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                URL mUrl = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) mUrl.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); //faster than InputStream reader
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n"); // "/n" this makes new line in the buffer so that it finds a free space to put the incoming line into it instead override the previous line.
                }
                result = buffer.toString();
                Log.i("Result: ", result);
                return result; //then return res in on post method

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    */




    public String getRankName(String num){
        String rnk = "";
        if (num == "0"){
            rnk = "Unranked";
        }
        if (num == "1"){
            rnk = "Copper IV";
        }
        if (num == "2"){
            rnk = "Copper III";
        }
        if (num == "3"){
            rnk = "Copper II";
        }
        if (num == "4"){
            rnk = "Copper I";
        }
        if (num == "5"){
            rnk = "Bronze IV";
        }
        if (num == "6"){
            rnk = "Bronze III";
        }
        if (num == "7"){
            rnk = "Bronze II";
        }
        if (num == "8"){
            rnk = "Bronze I";
        }
        if (num == "9"){
            rnk = "Silver IV";
        }
        if (num == "10"){
            rnk = "Silver III";
        }
        if (num == "11"){
            rnk = "Silver II";
        }
        if (num == "12"){
            rnk = "Silver I";
        }
        if (num == "13"){
            rnk = "Gold IV";
        }
        if (num == "14"){
            rnk = "Gold III";
        }
        if (num == "15"){
            rnk = "Gold II";
        }
        if (num == "16"){
            rnk = "Gold I";
        }
        if (num == "17"){
            rnk = "Platinum III";
        }
        if (num == "18"){
            rnk = "Platinum II";
        }
        if (num == "19"){
            rnk = "Platinum I";
        }
        if (num == "20"){
            rnk = "Diamond";
        }

        return rnk;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
