package gbrusa.cse331_17au_campus_paths;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * <b>CampusPathsMainActivity</b> represents the Controller of the CampusPaths application.
 * It is concerned with maintaining listeners to various widgets that allow user interaction.
 * It relays user input to the Model and View according to whether data is needed or the View
 * must be updated.
 * It can also pull data from the Model to initialize various widgets.
 */

public class CampusPathsMainActivity extends AppCompatActivity {

    private DrawView view;
    private ListView startBuildingsList;
    private ListView endBuildingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_paths_main);

        // initializes the Model so that it can information can be pulled later on
        CampusPathsModel.createModel(getApplicationContext());

        // Sets the getPath button listener which prompts the View to display a path
        Button getPathButton = (Button) findViewById(R.id.GetPathButton);
        getPathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.drawPath();
            }
        });

        // sets the clear button listener that resets the view when pressed
        Button clearButton = (Button) findViewById(R.id.ClearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.clear();
                startBuildingsList.clearChoices();
                startBuildingsList.requestLayout();
                endBuildingsList.clearChoices();
                endBuildingsList.requestLayout();
            }
        });

        view = (DrawView) findViewById(R.id.CampusMap);

        // creates the lists that store the building names
        startBuildingsList = (ListView) findViewById(R.id.StartBuildings);
        endBuildingsList = (ListView) findViewById(R.id.EndBuildings);

        startBuildingsList.setAdapter(createAdapter());
        endBuildingsList.setAdapter(createAdapter());

        // sets the starting buildings list listener so that when an option is selected it can
        // be displayed on the map view
        startBuildingsList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                String buildingShortName = (String) startBuildingsList.getItemAtPosition(position);
                view.drawStart(buildingShortName);
            }
        });

        // sets the destination buildings list listened so that when an option is selected it can
        // be displayed on the map view
        endBuildingsList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                String buildingShortName = (String) endBuildingsList.getItemAtPosition(position);
                view.drawDest(buildingShortName);
            }
        });
    }

    /**
     * @return an ArrayAdapter that stores all the building names in sorted order
     */
    private ArrayAdapter<String> createAdapter() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_single_choice, new ArrayList<String>());
        for (String str : CampusPathsModel.getBuildingNames()) {
            adapter.add(str);
        }
        return adapter;
    }
}
