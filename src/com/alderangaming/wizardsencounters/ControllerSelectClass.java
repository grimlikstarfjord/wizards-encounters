package com.alderangaming.wizardsencounters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ControllerSelectClass extends Activity
{

	private ArrayList<ItemClass> possibleClasses;
	private int curIndex = 1;
	private boolean showingRuneInfo = false;
	private int runeIndex = 0;
	private int[] runeIds =
	{ 0 };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		possibleClasses = OwnedItems.getAllClasses();;

		setContentView(R.layout.selectclass);
		setupViews();
		setupClassLabels();
		updateRuneList();
	}

	private void setupViews()
	{
		selectclassTopLabel = (TextView) findViewById(R.id.selectclassTopLabel);
		selectclassMiddleLabel = (TextView) findViewById(R.id.selectclassMiddleLabel);
		selectclassBottomLabel = (TextView) findViewById(R.id.selectclassBottomLabel);
		selectclassTitleLabel = (TextView) findViewById(R.id.selectclassTitleLabel);
		selectclassSelectedInfoLabel = (TextView) findViewById(R.id.selectclassSelectedInfoLabel);
		selectclassSelectedInfoDescription = (TextView) findViewById(R.id.selectclassSelectedInfoDescription);

		selectclassTypeSelectedInfoLabel = (TextView) findViewById(R.id.selectclassTypeSelectedInfoLabel);
		selectclassTypeSelectedInfoDescription = (TextView) findViewById(R.id.selectclassTypeSelectedInfoDescription);

		selectclassSelectedAbilityLayout = (LinearLayout) findViewById(R.id.selectclassSelectedAbilityLayout);
		selectclassSelectedAbilityLayout.setVisibility(View.INVISIBLE);

		selectclassSelectedAbilityName = (TextView) findViewById(R.id.selectclassSelectedAbilityName);
		selectclassSelectedAbilityName.setText("");

		selectclassSelectedAbilityInfo = (TextView) findViewById(R.id.selectclassSelectedAbilityInfo);
		selectclassSelectedAbilityInfo.setText("");

		selectclassUpButton = (Button) findViewById(R.id.selectclassUpButton);
		selectclassUpButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				moveUp();
				Log.d("selectclass","classId = "+possibleClasses.get(curIndex).id());
			}

		});
		selectclassDownButton = (Button) findViewById(R.id.selectclassDownButton);
		selectclassDownButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				moveDown();
				Log.d("selectclass","classId = "+possibleClasses.get(curIndex).id());
			}

		});
		selectclassGoButton = (Button) findViewById(R.id.selectclassGoButton);
		selectclassGoButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent resultIntent = new Intent();
				Log.d("selectclass","classId when pressed go was "+possibleClasses.get(curIndex).id()+" ("+DefinitionClasses.CLASS_NAMES[possibleClasses.get(curIndex).id()]);
				resultIntent.putExtra("selectedClassIdFromStore", possibleClasses.get(curIndex).id());

				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});

		selectclassRuneList = (ListView) findViewById(R.id.selectclassRuneList);
		selectclassRuneList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3)
			{
				runeIndex = index;
			}

		});
		selectclassRuneList.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
					{
						if (!showingRuneInfo)
						{
							showingRuneInfo = true;

							selectclassSelectedAbilityName
								.setText((String) DefinitionRunes.runeData[runeIds[runeIndex]][DefinitionRunes.RUNE_NAMES][0]);
							selectclassSelectedAbilityInfo
								.setText((String) DefinitionRunes.runeData[runeIds[runeIndex]][DefinitionRunes.RUNE_DESCRIPTION][0]);
							selectclassSelectedAbilityLayout.setVisibility(View.VISIBLE);
						}
					}
						break;
					case MotionEvent.ACTION_UP:
					{
						showingRuneInfo = false;
						selectclassSelectedAbilityLayout.setVisibility(View.INVISIBLE);
					}
						break;
				}
				return true;
			}

		});

	}

	private void updateRuneList()
	{

		runeIds = Helper.getRuneIdsForClassAndRank(possibleClasses.get(curIndex).id(), 1);
		String[] runeNames = new String[runeIds.length];
		for (int a = 0; a < runeNames.length; a++)
			runeNames[a] = (String) DefinitionRunes.runeData[runeIds[a]][DefinitionRunes.RUNE_NAMES][0];

		runeListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, runeNames);
		selectclassRuneList.setAdapter(runeListAdapter);
	}

	private void moveUp()
	{
		curIndex--;

		if (curIndex < 0)
		{
			curIndex = possibleClasses.size() - 1;
		}

		Log.d("selectclass", "current index is " + curIndex);

		if (curIndex == 0)
		{
			int topInd = possibleClasses.size() - 1;

			selectclassTopLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(topInd).classType()]
				+ ": " + possibleClasses.get(topInd).name());
		}
		else
		{
			selectclassTopLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex - 1).classType()]
					+ ": " + possibleClasses.get(curIndex - 1).name());
		}

		if (curIndex == possibleClasses.size() - 1)
		{
			selectclassBottomLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(0).classType()]
				+ ": " + possibleClasses.get(0).name());
		}
		else
		{
			selectclassBottomLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex + 1)
				.classType()] + ": " + possibleClasses.get(curIndex + 1).name());
		}

		selectclassMiddleLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex).classType()]
			+ ": " + possibleClasses.get(curIndex).name());

		updateCurrentInfo();

		updateRuneList();
	}

	private void moveDown()
	{
		curIndex++;
		if (curIndex > possibleClasses.size() - 1)
		{
			curIndex = 0;
		}
		
		Log.d("selectclass", "current index is " + curIndex);

		if (curIndex == possibleClasses.size() - 1)
		{
			selectclassBottomLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(0).classType()]
				+ ": " + possibleClasses.get(0).name());
		}
		else
		{
			selectclassBottomLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex + 1)
				.classType()] + ": " + possibleClasses.get(curIndex + 1).name());
		}
		
		if(curIndex == 0)
		{
			selectclassTopLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(possibleClasses.size() - 1).classType()]
					+ ": " + possibleClasses.get(possibleClasses.size() - 1).name());
		}
		else
		{
			selectclassTopLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex - 1).classType()]
					+ ": " + possibleClasses.get(curIndex - 1).name());
		}

		selectclassMiddleLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex).classType()]
			+ ": " + possibleClasses.get(curIndex).name());

		updateCurrentInfo();

		updateRuneList();
	}

	private void setupClassLabels()
	{
		selectclassTitleLabel.setText("Unlocked Classes (" + possibleClasses.size() + ")");

		selectclassTopLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex - 1).classType()]
			+ ": " + possibleClasses.get(curIndex - 1).name());
		selectclassMiddleLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex).classType()]
			+ ": " + possibleClasses.get(curIndex).name());
		selectclassBottomLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex + 1)
			.classType()] + ": " + possibleClasses.get(curIndex + 1).name());

		updateCurrentInfo();
	}

	private void updateCurrentInfo()
	{
		selectclassSelectedInfoLabel.setText("The " + possibleClasses.get(curIndex).name());
		selectclassSelectedInfoDescription.setText(possibleClasses.get(curIndex).description());
		
		selectclassTypeSelectedInfoLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex)
			.classType()] + "s");
		selectclassTypeSelectedInfoDescription.setText(DefinitionClassType.CLASS_TYPE_DESCRIPTION[possibleClasses.get(
			curIndex).classType()]);
	}

	LinearLayout selectclassSelectedAbilityLayout;
	TextView selectclassSelectedAbilityName;
	TextView selectclassSelectedAbilityInfo;

	TextView selectclassTitleLabel;
	Button selectclassUpButton;
	Button selectclassDownButton;
	Button selectclassGoButton;
	TextView selectclassTopLabel;
	TextView selectclassBottomLabel;
	TextView selectclassMiddleLabel;

	TextView selectclassSelectedInfoLabel;
	TextView selectclassSelectedInfoDescription;
	TextView selectclassTypeSelectedInfoLabel;
	TextView selectclassTypeSelectedInfoDescription;

	ListView selectclassRuneList;
	ListAdapter runeListAdapter;
}
