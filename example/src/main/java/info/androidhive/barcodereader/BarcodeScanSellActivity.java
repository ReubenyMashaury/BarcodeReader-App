package info.androidhive.barcodereader;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.barcode.Barcode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import info.androidhive.barcode.BarcodeReader;
import info.androidhive.barcodereader.SQLiteDatabaseFolder.DatabaseHandler;

/***
 * Created by Mikey Pixels
 * year 2020
 */

public class BarcodeScanSellActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    private static final String TAG = BarcodeScanSellActivity.class.getSimpleName();

    private BarcodeReader barcodeReader;

    private DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);

        // getting barcode instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Scanning...");

        /***
         * Providing beep sound. The sound file has to be placed in
         * `assets` folder
         */
        // barcodeReader.setBeepSoundFile("shutter.mp3");

        /**
         * Pausing / resuming barcode reader. This will be useful when you want to
         * do some foreground user interaction while leaving the barcode
         * reader in background
         * */
        // barcodeReader.pauseScanning();
        // barcodeReader.resumeScanning();
    }

    EditText quantityEditText;
    EditText amountEditText;
    TextView productName;
    TextView itemQuantity;
    Button submit;
    Button cancel;
    int height;
    int width;
    DisplayMetrics dm;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onScanned(final Barcode barcode) {
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();

        dm = this.getResources().getDisplayMetrics();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                width = dm.widthPixels;
                height = dm.heightPixels;
                final Dialog dialog = new Dialog(BarcodeScanSellActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.quantity_dialog);

                quantityEditText = dialog.findViewById(R.id.quantityEdittext);
                productName = dialog.findViewById(R.id.productName);
                itemQuantity = dialog.findViewById(R.id.itemQuantity);
                amountEditText = dialog.findViewById(R.id.amountEditText);
                cancel = dialog.findViewById(R.id.cancel);
                submit = dialog.findViewById(R.id.submit);
//                Toast.makeText(getApplicationContext(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();
//                String name = db.getProduct(barcode.displayValue).getProduct_name();

                if(db.getProduct(barcode.displayValue) != null){
                    int quantity = db.getQuantity(barcode.displayValue);
                    productName.setText(db.getProduct(barcode.displayValue).getProduct_name());
                    itemQuantity.setText(String.valueOf(quantity));
                }

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!quantityEditText.getText().toString().isEmpty() && !amountEditText.getText().toString().isEmpty()){
                            if(db.getQuantity(barcode.displayValue) >= Integer.parseInt(quantityEditText.getText().toString())){
                                if(db.addSale(barcode.displayValue, Double.parseDouble(amountEditText.getText().toString()), Integer.parseInt(quantityEditText.getText().toString()))){
                                    Toast.makeText(getApplicationContext(), "Sale added with Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Sale was not added, product is either not registered or there is a another problem!", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "There is no enough quantity in stock!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Please fill the quantity field!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                Objects.requireNonNull(dialog.getWindow()).setLayout((int) (0.9*width), (int) (0.75*height));
                dialog.setCancelable(true);

//            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation

                if(db.getProduct(barcode.displayValue)!=null){
                    dialog.show();
                }else{
                    Toast.makeText(getApplicationContext(), "The product is not registered yet!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.e(TAG, "onScannedMultiple: " + barcodes.size());

        String codes = "";
        for (Barcode barcode : barcodes) {
            codes += barcode.displayValue + ", ";
        }

        final String finalCodes = codes;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(), "Barcodes: " + finalCodes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}