package com.example.notapad1;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class Notepad1 extends ListActivity {
	private int mNoteNumber = 1;
	private NotesDbAdapter mDbHelper;
	private Cursor mNotesCursor;
	public static final int INSERT_ID = Menu.FIRST;
	public static final int DELETE_ID = Menu.FIRST+ 1 ;

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notepad_list);
		Log.d("Notepadv3", "onCreate()");
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		// getMenuInflater().inflate(R.menu.notepad1, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case INSERT_ID:
			createNote();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			mDbHelper.deleteNote(info.id);
			fillData();
			return true;

		}
		return super.onContextItemSelected(item);
	}

	private void createNote() {
		// String noteName = "Note" + mNoteNumber++;
		// mDbHelper.createNote(noteName, "");
		// fillData();

		Intent i = new Intent(this, NoteEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent(this, NoteEdit.class);
		i.putExtra(NotesDbAdapter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
		
		/*Cursor c = mNotesCursor;
		c.moveToPosition(position);
		Intent i = new Intent(this, NoteEdit.class);
		i.putExtra(NotesDbAdapter.KEY_ROWID, id);
		i.putExtra(NotesDbAdapter.KEY_TITLE,
				c.getString(c.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
		i.putExtra(NotesDbAdapter.KEY_BODY,
				c.getString(c.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
		startActivityForResult(i, ACTIVITY_EDIT);****** version 2 *******/
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		/*Bundle extras = data.getExtras();

		switch (requestCode) {
		case ACTIVITY_CREATE:
			String title = extras.getString(NotesDbAdapter.KEY_TITLE);
			String body = extras.getString(NotesDbAdapter.KEY_BODY);
			mDbHelper.createNote(title, body);
			fillData();
			break;
		case ACTIVITY_EDIT:
			Long mRowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
			if (mRowId != null) {
				String editTitle = extras.getString(NotesDbAdapter.KEY_TITLE);
				String editBody = extras.getString(NotesDbAdapter.KEY_BODY);
				mDbHelper.updateNote(mRowId, editTitle, editBody);
			}                  ****** version 2 ******
*/
			fillData();

		
	}

	private void fillData() {
		
		Cursor c = mDbHelper.fetchAllNotes();
		mNotesCursor = mDbHelper.fetchAllNotes();

		String[] from = new String[] { NotesDbAdapter.KEY_TITLE };

		int[] to = new int[] { R.id.text1 };

		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.notes_row, c, from, to, 0);

		setListAdapter(notes);
	}

}
