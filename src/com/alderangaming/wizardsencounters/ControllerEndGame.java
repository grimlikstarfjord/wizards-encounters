package com.alderangaming.wizardsencounters;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ControllerEndGame extends Activity
{

	Player player;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		if (!DBHandler.isOpen(getApplicationContext()))
			DBHandler.open(getApplicationContext());

		Bundle b = getIntent().getExtras();

		// OwnedItems = (OwnedItems) b.getSerializable("OwnedItems");
		player = (Player) b.getSerializable("player");
		
		setContentView(R.layout.endgame);
		updatePlayer();
		setupViews();
	}
	
	private void updatePlayer()
	{
		player.setCurrentRound(1);
		player.setCurrentFight(1);
		player.setRank(player.rank() + 1);
		DBHandler.updatePlayer(player);
	}

	private void setupViews()
	{
		Button doneButton = (Button) findViewById(R.id.endGameDoneButton);
		doneButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();			
			}

		});
	}

}
