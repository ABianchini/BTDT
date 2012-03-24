package com.advancementbureau.BTDT2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class QuizSplashActivity extends QuizActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        final String FIRST_BOOT = "boot";
        SharedPreferences bootPref = getSharedPreferences(FIRST_BOOT, MODE_PRIVATE);
        SharedPreferences.Editor editor = bootPref.edit();
        if (bootPref.getBoolean(FIRST_BOOT, true)) {
        	startAnimating();
        	editor.putBoolean("boot", false);
            editor.commit();
        } else {
        	startActivity(new Intent(QuizSplashActivity.this, QuizMenuActivity.class));
    		QuizSplashActivity.this.finish();
        }
        startAnimating();
    }
    private void startAnimating() {
        TextView logo1 = (TextView) findViewById(R.id.TextViewTopTitle);
        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo1.startAnimation(fade1);
        TextView logo2 = (TextView) findViewById(R.id.TextViewBottomTitle);
        Animation fade2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2);
        logo2.startAnimation(fade2);
        Animation spinin = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        LayoutAnimationController controller = new LayoutAnimationController(spinin);
        TableLayout table = (TableLayout) findViewById(R.id.TableLayout01);
        for (int i = 0; i < table.getChildCount(); i++) {
        	TableRow row = (TableRow) table.getChildAt(i);
        	row.setLayoutAnimation(controller);
        }
        fade2.setAnimationListener(new AnimationListener() {
        	public void onAnimationEnd(Animation animation) {
        		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		startActivity(new Intent(QuizSplashActivity.this, QuizMenuActivity.class));
        		QuizSplashActivity.this.finish();
        	}
        	public void onAnimationRepeat(Animation animation) {
        	}
        	public void onAnimationStart(Animation animation) {
        	}
        });
    }
    @Override
    protected void onPause() {
    	super.onPause();
    	//Stop Animation
    	TextView logo1 = (TextView) findViewById(R.id.TextViewTopTitle);
    	logo1.clearAnimation();
    	TextView logo2 = (TextView) findViewById(R.id.TextViewBottomTitle);
    	logo2.clearAnimation();
    	TableLayout table = (TableLayout) findViewById(R.id.TableLayout01);
    	for(int i = 0; i < table.getChildCount(); i++) {
    		TableRow row = (TableRow) table.getChildAt(i);
    		row.clearAnimation();
    	}
    }
    @Override
    protected void onResume() {
    	super.onResume();
    	startAnimating();
    }
}