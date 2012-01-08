package com.advancementbureau.BTDT2;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class QuizScoresActivity extends QuizActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);
        TabHost host = (TabHost) findViewById(R.id.TabHost1);
        host.setup();
        TabSpec allScoresTab = host.newTabSpec("allTab");
        allScoresTab.setIndicator(getResources().getString(R.string.all_scores));
        allScoresTab.setContent(R.id.ScrollViewAllScores);
        host.addTab(allScoresTab);
        TabSpec friendScoresTab = host.newTabSpec("friendsTab");
        friendScoresTab.setIndicator(getResources().getString(R.string.friends_scores));
        friendScoresTab.setContent(R.id.ScrollViewFriendScores);
        host.addTab(friendScoresTab);
        host.setCurrentTabByTag("allTab");
        
        TableLayout allScoresTable = (TableLayout) findViewById(R.id.TableLayout_AllScores);
        TableLayout friendScoresTable = (TableLayout) findViewById(R.id.TableLayout_FriendScores);
        initializeHeaderRow(allScoresTable);
        initializeHeaderRow(friendScoresTable);
        
        XmlResourceParser mockAllScores = getResources().getXml(R.xml.allscores);
        XmlResourceParser mockFriendScores = getResources().getXml(R.xml.friendscores);
        
        try {
            processScores(allScoresTable, mockAllScores);
            processScores(friendScoresTable, mockFriendScores);
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Failed to load scores", e);
        }
    }
    private void initializeHeaderRow(TableLayout scoreTable) {
    	TableRow headerRow = new TableRow(this);
    	int textColor = getResources().getColor(R.color.logo_color);
    	float textSize = getResources().getDimension(R.dimen.help_text_size);
    	addTextToRowWithValues(headerRow, getResources().getString(R.string.username), textColor, textSize);
    	addTextToRowWithValues(headerRow, getResources().getString(R.string.score), textColor, textSize);
    	addTextToRowWithValues(headerRow, getResources().getString(R.string.rank), textColor, textSize);
    	scoreTable.addView(headerRow);
    }

    private void processScores(final TableLayout scoreTable, XmlResourceParser scores) throws XmlPullParserException,
            IOException {
        int eventType = -1;
        boolean bFoundScores = false;
        // Find Score records from XML
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                // Get the name of the tag (eg scores or score)
                String strName = scores.getName();
                if (strName.equals("score")) {
                    bFoundScores = true;
                    String scoreValue = scores.getAttributeValue(null, "score");
                    String scoreRank = scores.getAttributeValue(null, "rank");
                    String scoreUserName = scores.getAttributeValue(null, "username");
                    insertScoreRow(scoreTable, scoreValue, scoreRank, scoreUserName);
                }
            }
            eventType = scores.next();
        }
        // Handle no scores available
        if (bFoundScores == false) {
            final TableRow newRow = new TableRow(this);
            TextView noResults = new TextView(this);
            noResults.setText(getResources().getString(R.string.no_scores));
            newRow.addView(noResults);
            scoreTable.addView(newRow);
        }
    }
    
    private void insertScoreRow(final TableLayout scoreTable, String scoreValue, String scoreRank, String scoreUserName) {
        final TableRow newRow = new TableRow(this);
        int textColor = getResources().getColor(R.color.title_color);
        float textSize = getResources().getDimension(R.dimen.help_text_size);
        addTextToRowWithValues(newRow, scoreUserName, textColor, textSize);
        addTextToRowWithValues(newRow, scoreValue, textColor, textSize);
        addTextToRowWithValues(newRow, scoreRank, textColor, textSize);
        scoreTable.addView(newRow);
    }
    
    private void addTextToRowWithValues(final TableRow tableRow, String text, int textColor, float textSize) {
    	TextView textView = new TextView(this);
    	textView.setTextSize(textSize);
    	textView.setTextColor(textColor);
    	textView.setText(text);
    	tableRow.addView(textView);
    }
}