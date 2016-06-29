package com.example.couchbase.simpletodo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;  //Get a handle to ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        //items = new ArrayList<>();  //this is called in readItems()
        readItems();  //load items during onCreate()
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //items.add("First Item");
        //items.add("Second Item");
        setupListViewListener();
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString(); //get the value from the 'etNewItem' field
        itemsAdapter.add(itemText);
        etNewItem.setText(""); //reset the field to be null
        writeItems(); //save items when a new list item is added


    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){  //Attach LongClickListener to each Item for ListView
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                        items.remove(pos);  //removes that item
                        itemsAdapter.notifyDataSetChanged();  //refreshes the adapter
                        writeItems(); //save items when a list item is removed
                        return true;
                    }
        });

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){  //Attach ClickListener to each Item
                    private final int REQUEST_CODE = 20;
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        String itemText =(String) adapter.getItemAtPosition(pos);
                        i.putExtra("itemText", itemText);
                        i.putExtra("position", pos);
                        //startActivity(i);
                        startActivityForResult(i,REQUEST_CODE);


                        //itemsAdapter.notifyDataSetChanged();  //refreshes the adapter
                        //writeItems();  //save the updated item
                    }
                }
        );

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above

        if (resultCode == RESULT_OK && requestCode == 20) {
            // Extract name value from result extras
            String name = data.getExtras().getString("itemText");
            //ArrayList<String> name = data.getExtras().getString("itemText");
            int position = data.getExtras().getInt("position",0);
            itemsAdapter.notifyDataSetChanged();
            items.set(position,name);
            itemsAdapter.notifyDataSetChanged();
            lvItems.setAdapter(itemsAdapter);

            writeItems();
            Toast.makeText(this, "Update Item Name: " + name, Toast.LENGTH_SHORT).show();

        }

    }


    private void readItems(){  //open a file and read a newline-delimited list of items
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }  catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems(){ //save a file and write a newline-delimited list of items
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
