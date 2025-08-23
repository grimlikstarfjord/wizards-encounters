package com.alderangaming.wizardsencounters;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ControllerMakeNewChar extends Activity
{

	private String newPlayerName = "";
	private int selectedClassId = 0;
//	private OwnedItems ownedItems;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());
		
		Bundle b = getIntent().getExtras();

		//ownedItems = (OwnedItems) b.getSerializable("ownedItems");
		
		setContentView(R.layout.makenewchar);
		setupViews();
	}

	private void setupViews()
	{
		makenewcharTextField = (EditText) findViewById(R.id.makenewcharTextField);
		makenewcharInfoLabel = (TextView) findViewById(R.id.makeNewCharInfoLabel);

		makenewcharTextField.setOnKeyListener(new OnKeyListener()
		{
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.ACTION_DOWN)
				{
					newPlayerName = makenewcharTextField.getText().toString();
					return true;
				}
				return false;
			}

		});

		Button makenewcharSelectClassButton = (Button) findViewById(R.id.makenewcharSelectClassButton);
		makenewcharSelectClassButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				Intent i = new Intent(getApplicationContext(), com.alderangaming.wizardsencounters.ControllerSelectClass.class);
				Bundle b = new Bundle();

			//	b.putSerializable("ownedItems", ownedItems);

				i.putExtras(b);

				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				startActivityForResult(i, 1001);

			}
		});

		makenewcharGoButton = (Button) findViewById(R.id.makenewcharGoButton);
		makenewcharGoButton.setEnabled(false);
		makenewcharGoButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				newPlayerName = makenewcharTextField.getText().toString();

				if (newPlayerName.length() < 1 || newPlayerName.matches("Player Name"))
				{
					makenewcharInfoLabel.setText("Player name is blank!");
					return;
				}

				if (newPlayerName.length() > 8)
				{
					makenewcharInfoLabel.setText("Player name is too long!");
					return;
				}

				// set newPlayerName to textField

				if (newPlayerName.indexOf('_') >= 0 || newPlayerName.indexOf("'") >= 0
					|| newPlayerName.indexOf('"') >= 0 || newPlayerName.indexOf('&') >= 0
					|| newPlayerName.indexOf('=') >= 0)
				{
					makenewcharInfoLabel.setText("Invalid Player Name");
					return;
				}

				// save new player to player table
				Log.d("selectclass","selected classId at desk go button was: "+selectedClassId);
				int playerID = DBHandler.insertNewPlayer(newPlayerName, selectedClassId);

				Player player = new Player(playerID, newPlayerName, selectedClassId, true);
				
				if(newPlayerName.equals("ttt"))
				{
					OwnedItems.addAllItems(getApplicationContext());
				}
				else
				{
					OwnedItems.addStartingRunesForClass(selectedClassId, getApplicationContext());
				}
				
				DBHandler.updateOwnedItems(OwnedItems.getOwnedItems());
				

				if (DBHandler.isOpen(getApplicationContext()))
					DBHandler.close();

				Intent resultIntent = new Intent();
				Bundle b = new Bundle();

			//	b.putSerializable("ownedItems", ownedItems);
				b.putSerializable("player", player);

				resultIntent.putExtras(b);

				setResult(Activity.RESULT_OK, resultIntent);
				finish();

			}

		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
			case (1001):
			{
				if (!DBHandler.isOpen(getApplicationContext()))
					DBHandler.open(getApplicationContext());

				selectedClassId = data.getIntExtra("selectedClassIdFromStore", 0);

				makenewcharInfoLabel.setText("Ah, I see you signed up for on of my favorite classes, the " + DefinitionClasses.CLASS_NAMES[selectedClassId]+". Good luck.");

				makenewcharGoButton.setEnabled(true);
			}
		}
	}

	private EditText makenewcharTextField;
	private TextView makenewcharInfoLabel;
	private Button makenewcharGoButton;
}
