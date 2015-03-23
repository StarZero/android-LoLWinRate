package manuele.bryan.lolwinrate.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import manuele.bryan.lolwinrate.Databases.JsonIO;
import manuele.bryan.lolwinrate.Helpers.LolStatsApplication;
import manuele.bryan.lolwinrate.Helpers.StringHelper;
import manuele.bryan.lolwinrate.Items.StaticChampion;
import manuele.bryan.lolwinrate.R;
import manuele.bryan.lolwinrate.UserStatistics.RankedStatsInfo;

public class ChampionInfoFragment extends Fragment {
    private Context context;

    public static final String KEY_CHAMPNAME = "champname";
    public static final String KEY_WINRATE = "winrate";
    public static final String KEY_POPULARITY = "popularity";
    String champName = "";
    String champDisplayName = "";
    String winrate = "";
    String popularity = "";

    ImageView championBanner;

    TextView championNameTextView;
    TextView championTitleTextView;

    LinearLayout rankedStatsLayout;
    TextView noRankedStats;

    TextView kdaStaticTextView;
    TextView kdaTextView;
    TextView winLossStaticTextView;
    TextView winsTextView;
    TextView winLossStaticDashTextView;
    TextView lossesTextView;

    TextView aggregateStatisticsStaticTextView;
    TextView winrateTextView;
    TextView popularityTextView;

    TextView bioTextView;
    TextView allyTipsTextView;
    TextView enemyTipsTextView;

    TextView spellTitleTextView;
    TextView spellDescriptionTextView;

    ImageView spell1Icon;
    ImageView spell2Icon;
    ImageView spell3Icon;
    ImageView spell4Icon;

    TextView bioTitleStaticTextView;
    TextView allyTitleStaticTextView;
    TextView enemyTitleStaticTextView;
    TextView winrateStaticTextview;
    TextView popularityStaticTextView;


    public static ChampionInfoFragment newInstance(String champName, String winrate, String popularity) {
        ChampionInfoFragment fragment = new ChampionInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CHAMPNAME, champName);
        bundle.putString(KEY_WINRATE, winrate);
        bundle.putString(KEY_POPULARITY, popularity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            champName = getArguments().getString(KEY_CHAMPNAME);
            champDisplayName = StringHelper.capitalizeFirstLetter(champName);
            winrate = getArguments().getString(KEY_WINRATE);
            popularity = getArguments().getString(KEY_POPULARITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_champion_info, container, false);

        //___________________________IDENTIFYING_VIEWS________________________________

        championBanner = (ImageView) view.findViewById(R.id.champBanner);

        championNameTextView = (TextView) view.findViewById(R.id.championName);
        championTitleTextView = (TextView) view.findViewById(R.id.championTitle);

        rankedStatsLayout = (LinearLayout) view.findViewById(R.id.championInfoRankedView);
        noRankedStats = (TextView) view.findViewById(R.id.championInfoStaticNoRanked);

        kdaStaticTextView = (TextView) view.findViewById(R.id.championInfoStaticKDA);
        kdaTextView = (TextView) view.findViewById(R.id.championInfoKDA);
        winLossStaticTextView = (TextView) view.findViewById(R.id.championInfoStaticWinLoss);
        winsTextView = (TextView) view.findViewById(R.id.championInfoWins);
        winLossStaticDashTextView = (TextView) view.findViewById(R.id.championInfoWinLossStaticDash);
        lossesTextView = (TextView) view.findViewById(R.id.championInfoLosses);

        spell1Icon = (ImageView) view.findViewById(R.id.championInfoSpellIcon1);
        spell2Icon = (ImageView) view.findViewById(R.id.championInfoSpellIcon2);
        spell3Icon = (ImageView) view.findViewById(R.id.championInfoSpellIcon3);
        spell4Icon = (ImageView) view.findViewById(R.id.championInfoSpellIcon4);

        spellTitleTextView = (TextView) view.findViewById(R.id.spellName);
        spellDescriptionTextView = (TextView) view.findViewById(R.id.spellDescription);

        aggregateStatisticsStaticTextView = (TextView) view.findViewById(R.id.championInfoAggregateStatisticsStatic);
        winrateStaticTextview = (TextView) view.findViewById(R.id.championInfoWinrateStatic);
        winrateTextView = (TextView) view.findViewById(R.id.winrate);
        popularityStaticTextView = (TextView) view.findViewById(R.id.championInfoMatchesStatic);
        popularityTextView = (TextView) view.findViewById(R.id.popularity);

        bioTitleStaticTextView = (TextView) view.findViewById(R.id.championInfoStaticLoreTitle);
        bioTextView = (TextView) view.findViewById(R.id.bioText);

        allyTitleStaticTextView = (TextView) view.findViewById(R.id.championInfoStaticAllyTips);
        allyTipsTextView = (TextView) view.findViewById(R.id.allytips);
        enemyTitleStaticTextView = (TextView) view.findViewById(R.id.championInfoStaticEnemyTips);
        enemyTipsTextView = (TextView) view.findViewById(R.id.enemytips);

        //________________________________TYPE_FACE_SETTING________________________________

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/robotolight.ttf");
        championNameTextView.setTypeface(typeface);
        championTitleTextView.setTypeface(typeface);

        kdaStaticTextView.setTypeface(typeface);
        kdaTextView.setTypeface(typeface);
        winLossStaticTextView.setTypeface(typeface);
        winsTextView.setTypeface(typeface);
        winLossStaticDashTextView.setTypeface(typeface);
        lossesTextView.setTypeface(typeface);

        spellTitleTextView.setTypeface(typeface);
        spellDescriptionTextView.setTypeface(typeface);

        aggregateStatisticsStaticTextView.setTypeface(typeface);
        winrateStaticTextview.setTypeface(typeface);
        winrateTextView.setTypeface(typeface);
        popularityStaticTextView.setTypeface(typeface);
        popularityTextView.setTypeface(typeface);

        bioTitleStaticTextView.setTypeface(typeface);
        bioTextView.setTypeface(typeface);

        allyTitleStaticTextView.setTypeface(typeface);
        allyTipsTextView.setTypeface(typeface);
        enemyTitleStaticTextView.setTypeface(typeface);
        enemyTipsTextView.setTypeface(typeface);

        //_____________________________INFLATING_VIEWS_____________________________________

        StaticChampion championInfo = JsonIO.loadChampionInfo(getActivity(), champName);
        int championId = championInfo.key;

        RankedStatsInfo rankedStatsInfo = LolStatsApplication.rankedStatsInfo;

        championNameTextView.setText(championInfo.name);
        championTitleTextView.setText(championInfo.title);

        if (rankedStatsInfo != null) {
            //ranked
            RankedStatsInfo.ChampionStats championStats = rankedStatsInfo.championList.get(championId);
            if (championStats != null) {
                noRankedStats.setVisibility(View.INVISIBLE);

                if (championStats.totalDeathsPerSession != 0) {
                    double kda = 1.0 * (championStats.totalChampionKills + championStats.totalAssists) / championStats.totalDeathsPerSession;
                    kdaTextView.setText("" + ((int) Math.round(kda * 100.0)) / 100.0);
                } else {
                    kdaTextView.setText("Flawless");
                }

                winsTextView.setText("" + championStats.totalSessionsWon);
                lossesTextView.setText("" + championStats.totalSessionsLost);
            } else {
                rankedStatsLayout.setVisibility(View.GONE);

                noRankedStats.setText("No ranked " + championInfo.name + " matches played");
            }
        } else {
            //unranked
            rankedStatsLayout.setVisibility(View.GONE);

        }

        winrateTextView.setText(winrate);
        popularityTextView.setText(popularity);

        bioTextView.setText(championInfo.lore);

        String allytips = StringHelper.createBulletPointText(championInfo.allytips);
        String enemytips = StringHelper.createBulletPointText(championInfo.enemytips);

        allyTipsTextView.setText(allytips);
        enemyTipsTextView.setText(enemytips);




        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }

}
