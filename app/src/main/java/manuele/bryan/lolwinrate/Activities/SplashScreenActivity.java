package manuele.bryan.lolwinrate.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;

import manuele.bryan.lolwinrate.Databases.DataBaseIO;
import manuele.bryan.lolwinrate.Databases.JsonIO;
import manuele.bryan.lolwinrate.Databases.PreferencesDataBase;
import manuele.bryan.lolwinrate.Helpers.LolStatsApplication;
import manuele.bryan.lolwinrate.LolStatistics.LeagueScrapper;
import manuele.bryan.lolwinrate.LolStatistics.StatisticsChampion;
import manuele.bryan.lolwinrate.LolStatistics.StatisticsChampionList;
import manuele.bryan.lolwinrate.R;


public class SplashScreenActivity extends ActionBarActivity {
    final String PREFS = "prefs";

    RelativeLayout splashLayout;
    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_splash_screen);

        splashLayout = (RelativeLayout) findViewById(R.id.splashLayout);
        splashImage = (ImageView) findViewById(R.id.splashImage);

        SharedPreferences settings = getSharedPreferences(PREFS, 0);
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time
            PreferencesDataBase preferences = new PreferencesDataBase(getBaseContext());
            preferences.createDefaultSettings();

            settings.edit().putBoolean("my_first_time", false).apply();
        }

        PreferencesDataBase preferences = new PreferencesDataBase(getBaseContext());
        if (preferences.getUsername().equals(PreferencesDataBase.DEFAULT_USER_NAME)) {
            Intent intent = new Intent(SplashScreenActivity.this, SetUpSummonerActivity.class);
            startActivity(intent);
        } else {
            startAnimations();
            new DownloadDataTask().execute("");
        }
    }

    private void startAnimations() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animation.reset();
        splashLayout.clearAnimation();
        splashLayout.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.translateup);
        animation.reset();
        splashImage.clearAnimation();
        splashImage.startAnimation(animation);
    }

    private class DownloadDataTask extends AsyncTask<String, String, Integer> {
        //0 means no connection, 1 means connection

        protected Integer leagueScrape() {
            LeagueScrapper leagueScrapper = new LeagueScrapper(getBaseContext());
            List<StatisticsChampion> statisticsChampions = null;
            try {
                statisticsChampions = leagueScrapper.createDataTable();

            } catch (IOException e) {
                //no internet connection!!

                System.out.println("no internet connection");
            }

            DataBaseIO dataBaseIO = new DataBaseIO(getBaseContext());

            if (statisticsChampions == null) {
                if (dataBaseIO.championsNull()) {
                    return 0;
                } else {
                    LolStatsApplication.statisticsChampionList = new StatisticsChampionList(dataBaseIO.getChampions());
                    return 1;
                }

            } else {
                dataBaseIO.addChampions(statisticsChampions);
                LolStatsApplication.statisticsChampionList = new StatisticsChampionList(dataBaseIO.getChampions());
                return 1;
            }
        }

        protected Integer updateUser() {
            PreferencesDataBase preferences = new PreferencesDataBase(getBaseContext());

            String username = preferences.getUsername();
            String region = preferences.getUserRegion();

            try {

                String infoURLString = "https://" + region + ".api.pvp.net/api/lol/" + region + "/v1.4/summoner/by-name/" + username + "?api_key=" + LolStatsApplication.riotApiKey;

                String infoJsonString = JsonIO.getJSONFromWeb(infoURLString);
                LolStatsApplication.userInfo = JsonIO.parseUserJson(username, infoJsonString);

                int id = LolStatsApplication.userInfo.id;

                String statsURLString = "https://" + region + ".api.pvp.net/api/lol/" + region + "/v1.3/stats/by-summoner/" + id +
                        "/ranked?season=SEASON2015&api_key=" + LolStatsApplication.riotApiKey;

                String statsJsonString = JsonIO.getJSONFromWeb(statsURLString);
                LolStatsApplication.rankedStatsInfo = JsonIO.parseRankedStatsJson(statsJsonString);

                String summaryURLString = "https://" + region + ".api.pvp.net/api/lol/" + region + "/v1.3/stats/by-summoner/" + id +
                        "/summary?season=SEASON2015&api_key=" + LolStatsApplication.riotApiKey;

                String summaryJsonString = JsonIO.getJSONFromWeb(summaryURLString);
                LolStatsApplication.userSummaryInfo = JsonIO.parseUserSummaryJson(summaryJsonString);

                String userLeagueURLString = "https://" + region + ".api.pvp.net/api/lol/" + region + "/v2.5/league/by-summoner/" + id +
                        "/entry?api_key=" + LolStatsApplication.riotApiKey;

                String userLeagueJsonString = JsonIO.getJSONFromWeb(userLeagueURLString);
                LolStatsApplication.usersLeagueInfo = JsonIO.parseUsersLeagueJson(userLeagueJsonString);

                String matchHistoryURLString = "https://" + region + ".api.pvp.net/api/lol/" + region + "/v2.2/matchhistory/" + id +
                        "?rankedQueues=RANKED_SOLO_5x5,RANKED_TEAM_3x3,RANKED_TEAM_5x5&beginIndex=0&endIndex=10&api_key=" + LolStatsApplication.riotApiKey;

                String matchHistoryJsonString = JsonIO.getJSONFromWeb(matchHistoryURLString);
                LolStatsApplication.matchHistory = JsonIO.parseMatchHistoryJson(matchHistoryJsonString);

                preferences.updateJSON(infoJsonString, statsJsonString, summaryJsonString, userLeagueJsonString, matchHistoryJsonString);

                return 1;

            } catch (Exception e) {
                System.out.println("Still no internet connection!");
            }

            //If you get here, there is no internet connection

            String infoJsonString = preferences.getSetting(PreferencesDataBase.KEY_JSON_USER_INFO);
            String statsJsonString = preferences.getSetting(PreferencesDataBase.KEY_JSON_USER_STATS);
            String summaryJsonString = preferences.getSetting(PreferencesDataBase.KEY_JSON_USER_SUMMARY);
            String userLeagueJsonString = preferences.getSetting(PreferencesDataBase.KEY_JSON_USER_LEAGUE);
            String matchHistoryJsonString = preferences.getSetting(PreferencesDataBase.KEY_JSON_MATCH_HISTORY);

            if (infoJsonString.equals("") || statsJsonString.equals("") || summaryJsonString.equals("") || userLeagueJsonString.equals("") || matchHistoryJsonString.equals("")) {
                return 0;
            } else {
                LolStatsApplication.userInfo = JsonIO.parseUserJson(username, infoJsonString);
                LolStatsApplication.rankedStatsInfo = JsonIO.parseRankedStatsJson(statsJsonString);
                LolStatsApplication.userSummaryInfo = JsonIO.parseUserSummaryJson(summaryJsonString);
                LolStatsApplication.usersLeagueInfo = JsonIO.parseUsersLeagueJson(userLeagueJsonString);
                LolStatsApplication.matchHistory = JsonIO.parseMatchHistoryJson(matchHistoryJsonString);

                return 1;
            }


        }

        @Override
        protected Integer doInBackground(String... params) {
            LolStatsApplication.staticLolItemsList = JsonIO.parseLolItemListJson(getAssets());

            if (leagueScrape() == 1) {
                return updateUser();
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);

            switch (code) {
                case 0:
                    Intent intent1 = new Intent(SplashScreenActivity.this, NoConnectivityActivity.class);
                    startActivity(intent1);
                    return;
                case 1:
                    Intent intent2 = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent2);
            }

            finish();
        }
    }

}
