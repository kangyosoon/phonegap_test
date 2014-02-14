/**
 * 
 */
package com.example.notapad1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author user
 *
 */
public class NoteEdit extends Activity {
	EditText mTitleText;
	EditText mBodyText;
	Long mRowId;
	private NotesDbAdapter mDbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d("NoteEdit", "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_edit);
		
		mTitleText = (EditText)findViewById(R.id.title);
		mBodyText = (EditText)findViewById(R.id.body);
		Button confirmButton = (Button)findViewById(R.id.confirm);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		mRowId = null;
		
		/*Bundle extras = getIntent().getExtras();
		if(extras !=null)	{
			String title = extras.getString(NotesDbAdapter.KEY_TITLE);
			String body = extras.getString(NotesDbAdapter.KEY_BODY);
			mRowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
			if(title != null)	{
				mTitleText.setText(title);
			}
			if(body != null)	{
				mBodyText.setText(body);
			}
		}*/
		
		mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
		if(mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID) : null;
		}
		populateFields();
		
		confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Bundle bundle = new Bundle();
				bundle.putString(NotesDbAdapter.KEY_TITLE, mTitleText.getText().toString());
				bundle.putString(NotesDbAdapter.KEY_BODY, mBodyText.getText().toString());
				
				if(mRowId != null){
					bundle.putLong(NotesDbAdapter.KEY_ROWID, mRowId);
				}
				
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent); ****** version 2 ****** */  
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	
	private void populateFields() {
		Log.d("NoteEdit", "populateFields()");
		// TODO Auto-generated method stub
		if(mRowId != null) {
			Cursor note = mDbHelper.fetchNote(mRowId);
			startManagingCursor(note);
			mTitleText.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
			
			mBodyText.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("NoteEdit", "onPause()");
		saveState();
	}
		
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("NoteEdit", "onResume()");
		populateFields();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
	}

	private void saveState() {
		// TODO Auto-generated method stub
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();
		Log.d("Activity", "saveState()");
		if(mRowId == null) {
			long id = mDbHelper.createNote(title, body);
			
			if(id > 0) 
				mRowId = id;
			else
				mDbHelper.updateNote(mRowId, title, body);
		}
		else mDbHelper.updateNote(mRowId, title, body);
	}
}
