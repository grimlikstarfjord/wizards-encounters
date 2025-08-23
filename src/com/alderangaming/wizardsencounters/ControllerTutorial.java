package com.alderangaming.wizardsencounters;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ControllerTutorial extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tutorial);
		Button exit = (Button) findViewById(R.id.tutorialExitButton);

		exit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				finish();
			}

		});
	}
}
