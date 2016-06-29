package com.example.couchbase.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    //int originalPosition = getIntent().getIntExtra("position").toInt();

    int originalPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String originalText = getIntent().getStringExtra("itemText").toString();
        originalPosition = getIntent().getIntExtra("position",0);
        //String test = "william";
        //System.out.print(originalText);
        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText("");
        etEditItem.append(originalText);


    }
        public void onEditItem(View v) {
            //String originalPosition = getIntent().getStringExtra("position");

            Intent data = new Intent();
            //pass relevant data back as a result
            EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
            String itemEditText = etEditItem.getText().toString(); //get the value from the 'etNewItem' field
            //data.putExtra("position",)
            //itemsAdapter.add(itemText);
            //pass the data back to previous screen
            data.putExtra("itemText", itemEditText);
            data.putExtra("position", originalPosition);
            setResult(RESULT_OK, data);
            etEditItem.setText(""); //reset the field to be null
            //writeItems(); //save items when a new list item is added
            finish();
        }


}
