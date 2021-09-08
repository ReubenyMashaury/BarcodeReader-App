package info.androidhive.barcodereader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import info.androidhive.barcodereader.SQLiteDatabaseFolder.DatabaseHandler;

public class AddPurchaseActivity extends AppCompatActivity {

    ArrayList<String> productNames = new ArrayList<>();
    DatabaseHandler db = new DatabaseHandler(this);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);
        final Spinner spinner = findViewById(R.id.product_spinner);
        final TextView quantityEditText = findViewById(R.id.quantity_edittext);
        final TextView amountEditText = findViewById(R.id.amount_edittext);
        final TextView dateEditText = findViewById(R.id.date);
        Button submitBtn = findViewById(R.id.submit_btn);

        for(Product product: db.getAllProducts()){
            productNames.add(product.product_name);
        }

        dateEditText.setText(simpleDateFormat.format(new Date().getTime()));

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, productNames);
        spinner.setAdapter(arrayAdapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!quantityEditText.getText().toString().isEmpty() || !amountEditText.getText().toString().isEmpty() || !dateEditText.getText().toString().isEmpty()){
                    if(db.addPurchase(new Purchase("", dateEditText.getText().toString(), spinner.getSelectedItem().toString(), Integer.parseInt(quantityEditText.getText().toString()),Double.parseDouble(amountEditText.getText().toString())))){
                        Toast.makeText(AddPurchaseActivity.this, "Purchase added successfully!", Toast.LENGTH_SHORT).show();
                        quantityEditText.setText("");
                        amountEditText.setText("");
                    }else{
                        Toast.makeText(AddPurchaseActivity.this, "Purchase was not added successfully!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddPurchaseActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}