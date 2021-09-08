package info.androidhive.barcodereader.ui.registerItem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.vision.barcode.Barcode;

import java.text.SimpleDateFormat;
import java.util.Date;

import info.androidhive.barcodereader.BarcodeScanRegisterItemActivity;
import info.androidhive.barcodereader.Product;
import info.androidhive.barcodereader.R;
import info.androidhive.barcodereader.SQLiteDatabaseFolder.DatabaseHandler;

public class RegisterItemFragment extends Fragment {

    public static EditText barcodeEdit;

    DatabaseHandler db = null;

    String TAG = this.getClass().getSimpleName();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_register_item, container, false);
        barcodeEdit = root.findViewById(R.id.barcode_edittext);
        final TextView name_edittext = root.findViewById(R.id.name_edittext);
        Button reg_btn = root.findViewById(R.id.reg_button);

        barcodeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BarcodeScanRegisterItemActivity.class);
                startActivity(intent);
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_edittext.getText().toString().isEmpty() || barcodeEdit.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }else{
                    if(db.getProduct(barcodeEdit.getText().toString()) == null){
                        if(db.addProduct(new Product(1, name_edittext.getText().toString(), barcodeEdit.getText().toString()))){
                            Toast.makeText(getContext(), "Product registered!", Toast.LENGTH_SHORT).show();
                            name_edittext.setText("");
                            barcodeEdit.setText("");
                        }else{
                            Toast.makeText(getContext(), "Product was not added!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Item with this barcode is already registered!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        db = new DatabaseHandler(getContext());
    }
}