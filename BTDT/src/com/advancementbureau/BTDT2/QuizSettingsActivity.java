package com.advancementbureau.BTDT2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class QuizSettingsActivity extends QuizActivity {
	SharedPreferences mGameSettings;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		// Retrieve the shared preferences
		mGameSettings = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
		// Initialize the nickname entry
		initNicknameEntry();
		// Initialize the email entry
		initEmailEntry();
		// Initialize the Password chooser
		initPasswordChooser();
		// Initialize the Date picker
		initDatePicker();
		// Initialize the spinner
		initGenderSpinner();
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
		Log.d(DEBUG_TAG,
				"Nickname is: " + mGameSettings.getString(GAME_PREFERENCES_NICKNAME, "Not set"));
		Log.d(DEBUG_TAG,
				"Email is: " + mGameSettings.getString(GAME_PREFERENCES_EMAIL, "Not set"));
		Log.d(DEBUG_TAG, "Gender (M=1, F=2, U=0) is: " + mGameSettings.getInt(GAME_PREFERENCES_GENDER, 0));
		// We are not saving the password yet
		Log.d(DEBUG_TAG, "Password is: " + mGameSettings.getString(GAME_PREFERENCES_PASSWORD, "Not set"));
		// We are not saving the date of birth yet
		Log.d(DEBUG_TAG, "DOB is: " + DateFormat.format("MMMM dd, yyyy", mGameSettings.getLong(GAME_PREFERENCES_DOB, 0)));
		super.onDestroy();
	}

	/**
	 * Initialize the nickname entry
	 */
	private void initNicknameEntry() {
		EditText nicknameText = (EditText) findViewById(R.id.EditText_Nickname);
		if (mGameSettings.contains(GAME_PREFERENCES_NICKNAME)) {
			nicknameText.setText(mGameSettings.getString(GAME_PREFERENCES_NICKNAME, ""));
		}
	}

	/**
	 * Initialize the email entry
	 */
	private void initEmailEntry() {
		EditText emailText = (EditText) findViewById(R.id.EditText_Email);
		if (mGameSettings.contains(GAME_PREFERENCES_EMAIL)) {
			emailText.setText(mGameSettings.getString(GAME_PREFERENCES_EMAIL, ""));
		}
	}

	/**
	 * Initialize the Password chooser
	 */
	private void initPasswordChooser() {
		// Set password info
		TextView passwordInfo = (TextView) findViewById(R.id.TextView_Password_Info);
		if (mGameSettings.contains(GAME_PREFERENCES_PASSWORD)) {
			passwordInfo.setText(R.string.settings_pwd_set);
		} else {
			passwordInfo.setText(R.string.password_empty);
		}
	}

	/**
	 * Called when the user presses the Set Password button
	 * 
	 * @param view
	 *            the button
	 */
	public void onSetPasswordButtonClick(View view) {
		Toast.makeText(QuizSettingsActivity.this,
				"TODO: Launch Password Dialog", Toast.LENGTH_LONG).show();
	}

	/**
	 * Initialize the Date picker
	 */
	private void initDatePicker() {
		// Set password info
		TextView dobInfo = (TextView) findViewById(R.id.TextView_DOB_Info);
		if (mGameSettings.contains(GAME_PREFERENCES_DOB)) {
			dobInfo.setText(DateFormat.format("MMMM dd, yyyy",
					mGameSettings.getLong(GAME_PREFERENCES_DOB, 0)));
		} else {
			dobInfo.setText(R.string.dob_empty);
		}
	}

	/**
	 * Called when the user presses the Pick Date button
	 * 
	 * @param view
	 *            The button
	 */
	public void onPickDateButtonClick(View view) {
		Toast.makeText(QuizSettingsActivity.this,
				"TODO: Launch DatePickerDialog", Toast.LENGTH_LONG).show();
	}

	/**
	 * Initialize the spinner
	 */
	private void initGenderSpinner() {
		// Populate Spinner control with genders
		final Spinner spinner = (Spinner) findViewById(R.id.Spinner_Gender);
		ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this,
				R.array.genders, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		if (mGameSettings.contains(GAME_PREFERENCES_GENDER)) {
			spinner.setSelection(mGameSettings.getInt(GAME_PREFERENCES_GENDER, 0));
		}
		// Handle spinner selections
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