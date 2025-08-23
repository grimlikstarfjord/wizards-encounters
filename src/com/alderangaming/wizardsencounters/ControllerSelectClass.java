package com.alderangaming.wizardsencounters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ControllerSelectClass extends Activity
{

	private ArrayList<ItemClass> possibleClasses;
	private Dialog hDialog;
	private int curIndex = 0;
	private boolean showingRuneInfo = false;
	private int runeIndex = 0;
	private int[] runeIds =
	{ 0 };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		possibleClasses = OwnedItems.getAllClasses();

		setContentView(R.layout.selectclass);
		setupHelpDialog();
		setupViews();
		setupClassLabels();
		updateRuneList();
	}

	private void showHelp()
	{
		hDialog.show();
	}

	private void setupHelpDialog()
	{
		AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.help, (ViewGroup) findViewById(R.id.helpRoot));
		TextView helpTextView = (TextView) layout.findViewById(R.id.helpText);
		Button doneButton = (Button) layout.findViewById(R.id.helpDoneButton);
		doneButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					hDialog.dismiss();
				}
				catch (Exception e)
				{
					// who cares I have more important crap in my life to deal
					// with
				}
			}

		});
		helpTextView.setText(R.string.selectclassHelpText);

		aboutDialog.setView(layout);

		hDialog = aboutDialog.create();
	}

	private void setupViews()
	{
		Button helpButton = (Button) findViewById(R.id.selectclassHelpButton);
		helpButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showHelp();
			}
		});

		selectclassTopLabel = (TextView) findViewById(R.id.selectclassTopLabel);
		selectclassMiddleLabel = (TextView) findViewById(R.id.selectclassMiddleLabel);
		selectclassMiddleLabel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent resultIntent = new Intent();
				Log.d("selectclass", "classId when pressed go was " + possibleClasses.get(curIndex).id() + " ("
					+ DefinitionClasses.CLASS_NAMES[possibleClasses.get(curIndex).id()]);
				resultIntent.putExtra("selectedClassIdFromStore", possibleClasses.get(curIndex).id());

				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}

		});
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
				Log.d("selectclass", "classId = " + possibleClasses.get(curIndex).id());
			}

		});
		selectclassDownButton = (Button) findViewById(R.id.selectclassDownButton);
		selectclassDownButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				moveDown();
				Log.d("selectclass", "classId = " + possibleClasses.get(curIndex).id());
			}

		});
		selectclassGoButton = (Button) findViewById(R.id.selectclassGoButton);
		selectclassGoButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent resultIntent = new Intent();
				Log.d("selectclass", "classId when pressed go was " + possibleClasses.get(curIndex).id() + " ("
					+ DefinitionClasses.CLASS_NAMES[possibleClasses.get(curIndex).id()]);
				resultIntent.putExtra("selectedClassIdFromStore", possibleClasses.get(curIndex).id());

				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});

		selectclassRuneList = (ListView) findViewById(R.id.selectclassRuneList);
	}

	public void updateRuneList()
	{
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		// runeIds =
		// Helper.getRuneIdsForClassAndRank(possibleClasses.get(curIndex).id(),
		// 1);
		runeIds = DefinitionClasses.CLASS_STARTING_RUNES[possibleClasses.get(curIndex).id()];

		for (int i = 0; i < runeIds.length; i++)
		{
			String name = (String) DefinitionRunes.runeData[runeIds[i]][DefinitionRunes.RUNE_NAMES][0];

			String apcost = "" + DefinitionRunes.runeData[runeIds[i]][DefinitionRunes.RUNE_AP_COST][0];

			String description = (String) DefinitionRunes.runeData[runeIds[i]][DefinitionRunes.RUNE_DESCRIPTION][0];

			rows.add(Helper.createMap(name, apcost, description));
		}

		String[] fromKeys = new String[]
		{ "Name", "APCost", "Description" };
		int[] toIds = new int[]
		{ R.id.abilityListName2, R.id.abilityListCost2, R.id.abilityListDescription2 };

		selectclassRuneList.setAdapter(new SimpleAdapter(this, rows, R.layout.abilitylistitem2, fromKeys, toIds));

		selectclassRuneList.invalidate();
	}

	private void moveUp()
	{
		if (possibleClasses.size() == 1)
			return;

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

			selectclassTopLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex - 1)
				.classType()] + ": " + possibleClasses.get(curIndex - 1).name());
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
		if (possibleClasses.size() == 1)
			return;

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

		if (curIndex == 0)
		{
			selectclassTopLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(
				possibleClasses.size() - 1).classType()]
				+ ": " + possibleClasses.get(possibleClasses.size() - 1).name());
		}
		else
		{

			selectclassTopLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex - 1)
				.classType()] + ": " + possibleClasses.get(curIndex - 1).name());

		}

		selectclassMiddleLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(curIndex).classType()]
			+ ": " + possibleClasses.get(curIndex).name());

		updateCurrentInfo();

		updateRuneList();
	}

	private void setupClassLabels()
	{
		selectclassTitleLabel.setText("Unlocked Classes (" + possibleClasses.size() + "/"
			+ DefinitionClasses.CLASS_NAMES.length + ")");

		if (possibleClasses.size() > 2)
		{
			selectclassTopLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(
				possibleClasses.size() - 1).classType()]
				+ ": " + possibleClasses.get(possibleClasses.size() - 1).name());
			selectclassMiddleLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(0).classType()]
				+ ": " + possibleClasses.get(0).name());
			selectclassBottomLabel.setText(DefinitionClassType.CLASS_TYPE_NAME[possibleClasses.get(1).classType()]
				+ ": " + possibleClasses.get(1).name());
		}

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
	private int position = 0;
}
