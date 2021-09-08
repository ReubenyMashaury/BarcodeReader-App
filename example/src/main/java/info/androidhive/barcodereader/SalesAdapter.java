package info.androidhive.barcodereader;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import info.androidhive.barcodereader.SQLiteDatabaseFolder.DatabaseHandler;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {

    Context context;
    List<Sale> sales;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DatabaseHandler db;
    SaleInterface saleInterface;

    public SalesAdapter(Context context, ArrayList<Sale> sales, SaleInterface saleInterface) {
        this.context = context;
        this.sales = sales;
        db = new DatabaseHandler(context);
        this.saleInterface = saleInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView quantity, product_name, date, amount;
        ConstraintLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.quantity);
            product_name = itemView.findViewById(R.id.product_name);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            itemLayout = itemView.findViewById(R.id.const_itemLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sale_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

//        holder.itemLayout.setBackgroundColor(Color.WHITE);
        holder.quantity.setText(String.valueOf(sales.get(position).getQuantity()));
        holder.quantity.setTextColor(Color.BLACK);
        holder.date.setText(sales.get(position).getDate());
        holder.date.setTextColor(Color.BLACK);
        holder.product_name.setText(sales.get(position).getProduct_name());
        holder.product_name.setTextColor(Color.BLACK);
        holder.amount.setText(String.valueOf(sales.get(position).getAmount()));
        holder.amount.setTextColor(Color.BLACK);
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
                                if (db.deleteSale(sales.get(position).getId())) {
                                    saleInterface.getPosition(sales.get(position));
                                    sales.remove(sales.get(position));
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
        return sales.size();
    }
}
