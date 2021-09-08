package info.androidhive.barcodereader;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.barcodereader.SQLiteDatabaseFolder.DatabaseHandler;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    Context context;
    List<Product> products;
    DatabaseHandler db;
    ProductInterface productInterface;

    public ProductsAdapter(Context context, ArrayList<Product> products, ProductInterface productInterface){
        this.context = context;
        this.products = products;
        this.productInterface = productInterface;
        db = new DatabaseHandler(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView id, product_name, quantity, barcode;
        ConstraintLayout itemLayout;

        public ViewHolder(View itemView){
            super(itemView);

            id = itemView.findViewById(R.id.sn);
            product_name = itemView.findViewById(R.id.product_name);
            quantity = itemView.findViewById(R.id.quantity);
            barcode = itemView.findViewById(R.id.barcode);
            itemLayout = itemView.findViewById(R.id.const_itemLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.id.setText(String.valueOf(products.get(position).getId()));
        holder.product_name.setText(products.get(position).getProduct_name());
        holder.quantity.setText(String.valueOf(db.getQuantity(products.get(position).getBarcode())));
        holder.barcode.setText(products.get(position).getBarcode());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(
                        context);

// Setting Dialog Title
                alertdialog.setTitle("Confirm Delete...");

// Setting Dialog Message
                alertdialog.setMessage("Do you want to delete this item?");

// Setting Icon to Dialog
                alertdialog.setIcon(R.drawable.ic_baseline_delete_forever_24);

// Setting Positive "Yes" Btn
                alertdialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                if (db.deleteProduct(products.get(position).getId())) {
                                    productInterface.getPosition(products.get(position));
                                    products.remove(products.get(position));
                                    notifyItemRemoved(position);
                                    Toast.makeText(context,
                                            "Item successfully deleted!", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    Toast.makeText(context,
                                            "Delete action was unsuccessful!", Toast.LENGTH_SHORT)
                                            .show();
                                }

                            }
                        });
// Setting Negative "NO" Btn
                alertdialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

// Showing Alert Dialog
                alertdialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
