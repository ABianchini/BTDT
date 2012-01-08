package com.advancementbureau.BTDT2;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class QuizSettingsActivity extends QuizActivity {
	SharedPreferences mGameSettings;
	static final int DATE_DIALOG_ID = 0;
	static final int PASSWORD_DIALOG_ID = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mGameSettings = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
        initNicknameEntry();
        initEmailEntry();
        initPasswordChooser();
        initDatePicker();
        initGenderSpinner();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
    	case DATE_DIALOG_ID:
    		final TextView dob = (TextView) findViewById(R.id.TextView_DOB_Info);
    		Calendar now = Calendar.getInstance();
    		DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
    			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    				
    				Time dateOfBirth = new Time();
    				dateOfBirth.set(dayOfMonth, monthOfYear, year);
    				long dtDob = dateOfBirth.toMillis(true);
    				dob.setText(DateFormat.format("MMMM dd, yyyy", dtDob));
    				Editor editor = mGameSettings.edit();
    				editor.putLong(GAME_PREFERENCES_DOB, dtDob);
    				editor.commit();
    			}
    		}, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
    	return dateDialog;
    	case PASSWORD_DIALOG_ID:
    		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.password_dialog, (ViewGroup) findViewById(R.id.root));
            final EditText p1 = (EditText) layout.findViewById(R.id.EditText_Pwd1);
            final EditText p2 = (EditText) layout.findViewById(R.id.EditText_Pwd2);
            final TextView error = (TextView) layout.findViewById(R.id.TextView_PwdProblem);
            p2.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    String strPass1 = p1.getText().toString();
                    String strPass2 = p2.getText().toString();
                    if (strPass1.equals(strPass2)) {
                        error.setText(R.string.settings_pwd_equal);
                    } else {
                        error.setText(R.string.settings_pwd_not_equal);
                    }
                }

                // ... other required overrides need not be implemented 
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);
            // Now configure the AlertDialog
            builder.setTitle(R.string.settings_button_pwd);
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // We forcefully dismiss and remove the Dialog, so it
                    // cannot be used again (no cached info)
                    QuizSettingsActivity.this.removeDialog(PASSWORD_DIALOG_ID);
                }
            });
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    TextView passwordInfo = (TextView) findViewById(R.id.TextView_Password_Info);
                    String strPassword1 = p1.getText().toString();
                    String strPassword2 = p2.getText().toString();
                    if (strPassword1.equals(strPassword2)) {
                        Editor editor = mGameSettings.edit();
                        editor.putString(GAME_PREFERENCES_PASSWORD, strPassword1);
                        editor.commit();
                        passwordInfo.setText(R.string.settings_pwd_set);
                    } else {
                        Log.d(DEBUG_TAG, "Passwords do not match. Not saving. Keeping old password (if set).");
                    }
                    // We forcefully dismiss and remove the Dialog, so it
                    // cannot be used again
                    QuizSettingsActivity.this.removeDialog(PASSWORD_DIALOG_ID);
                }
            });
            // Create the AlertDialog and return it
            AlertDialog passwordDialog = builder.create();
            return passwordDialog;
    	}
    	return null;
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    	super.onPrepareDialog(id, dialog);
    	switch (id) {
    	case DATE_DIALOG_ID:
    		//Handle any DatePickerDialog initialization here
    		DatePickerDialog dateDialog = (DatePickerDialog) dialog;
    		int iDay, iMonth, iYear;
    		//Check for date of birth preference
    		if (mGameSettings.contains(GAME_PREFERENCES_DOB)) {
    			//retrieve Birth date setting from preferences
    			long msBirthDate = mGameSettings.getLong(GAME_PREFERENCES_DOB, 0);
    			Time dateOfBirth = new Time();
    			dateOfBirth.set(msBirthDate);
    			iDay = dateOfBirth.monthDay;
    			iMonth = dateOfBirth.month;
    			iYear = dateOfBirth.year;
    		} else {
    			Calendar cal = Calendar.getInstance();
    			//Today's date fields
    			iDay = cal.get(Calendar.DAY_OF_MONTH);
    			iMonth = cal.get(Calendar.MONTH);
    			iYear = cal.get(Calendar.YEAR);
    		}
    		dateDialog.updateDate(iYear, iMonth, iDay);
    		return;
    	case PASSWORD_DIALOG_ID:
    		return;
    	}
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	EditText nicknameText = (EditText) findViewById(R.id.EditText_Nickname);
    	EditText emailText = (EditText) findViewById(R.id.EditText_Email);
    	String strNickname = nicknameText.getText().toString();
    	String strEmail = emailText.getText().toString();
    	Editor editor = mGameSettings.edit();
    	editor.putString(GAME_PREFERENCES_NICKNAME, strNickname);
    	editor.putString(GAME_PREFERENCES_EMAIL, strEmail);
    	editor.commit();
    }
    
    @Override
    protected void onDestroy() {
    	Log.d(DEBUG_TAG, "SHARED PREFERENCES");
    	Log.d(DEBUG_TAG, "Nickname is: " + mGameSettings.getString(GAME_PREFERENCES_NICKNAME, "Not set"));
    	Log.d(DEBUG_TAG, "Email is: " + mGameSettings.getString(GAME_PREFERENCES_EMAIL, "Not set"));
    	Log.d(DEBUG_TAG, "Gender (M=1, F=2, N=0) is: " + mGameSettings.getInt(GAME_PREFERENCES_GENDER, 0));
    	Log.d(DEBUG_TAG, "Password is: " + mGameSettings.getString(GAME_PREFERENCES_PASSWORD, "Not set"));
    	Log.d(DEBUG_TAG, "DoB is: " + DateFormat.format("MMMM dd, yyyy", mGameSettings.getLong(GAME_PREFERENCES_DOB, 0)));
    	super.onDestroy();
    }
    
    public void onSetPasswordButtonClick(View view) {
    	showDialog(PASSWORD_DIALOG_ID);
    }
    
    public void onPickDateButtonClick(View view) {
    	showDialog(DATE_DIALOG_ID);
    }
    
    public void onChangeLogButtonClick(View view) {
    	startActivity(new Intent(QuizSettingsActivity.this, QuizChangeActivity.class));
    }
    
    private void initNicknameEntry() {
    	EditText nicknameText = (EditText) findViewById(R.id.EditText_Nickname);
    	if (mGameSettings.contains(GAME_PREFERENCES_NICKNAME)) {
    		nicknameText.setText(mGameSettings.getString(GAME_PREFERENCES_NICKNAME, ""));
    	}
    }
    
    private void initEmailEntry() {
    	EditText emailText = (EditText) findViewById(R.id.EditText_Email);
    	if (mGameSettings.contains(GAME_PREFERENCES_EMAIL)) {
    		emailText.setText(mGameSettings.getString(GAME_PREFERENCES_EMAIL, ""));
    	}
    }
    
    private void initPasswordChooser() {
    	TextView  passwordInfo = (TextView) findViewById(R.id.TextView_Password_Info);
    	if (mGameSettings.contains(GAME_PREFERENCES_PASSWORD)) {
    		passwordInfo.setText(R.string.password_set);
    	} else {
    		passwordInfo.setText(R.string.password_empty);
    	}
    }
    
    private void initDatePicker() {
    	TextView dobInfo = (TextView) findViewById(R.id.TextView_DOB_Info);
    	if (mGameSettings.contains(GAME_PREFERENCES_DOB)) {
    		dobInfo.setText(DateFormat.format("MMMM dd, yyyy", mGameSettings.getLong(GAME_PREFERENCES_DOB, 0)));
    	} else {
    		dobInfo.setText(R.string.dob_empty);
    	}
    }
    
    private void initGenderSpinner() {
    	final Spinner spinner = (Spinner) findViewById(R.id.Spinner_Gender);
    	ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this,
				R.array.genders, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		if (mGameSettings.contains(GAME_PREFERENCES_GENDER)) {
			spinner.setSelection(mGameSettings.getInt(GAME_PREFERENCES_GENDER,
					0));
		}
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent,
					View itemSelected, int selectedItemPosition, long selectedId) {
				Editor editor = mGameSettings.edit();
				editor.putInt(GAME_PREFERENCES_GENDER, selectedItemPosition);
				editor.commit();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
    }
}