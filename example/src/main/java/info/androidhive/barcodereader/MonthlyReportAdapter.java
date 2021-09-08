package info.androidhive.barcodereader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class MonthlyReportAdapter extends RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder> {

    Context context;
    List<MonthReportItem> monthReportItems;

    public MonthlyReportAdapter(Context context, ArrayList<MonthReportItem> monthReportItems){
        this.context = context;
        this.monthReportItems = monthReportItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView monthyear, sales, purchases, profit;

        public ViewHolder(View itemView){
            super(itemView);

            monthyear = itemView.findViewById(R.id.monthYear);
            sales = itemView.findViewById(R.id.sales);
            purchases = itemView.findViewById(R.id.purchases);
            profit = itemView.findViewById(R.id.profit);
        }
    }

    @NonNull
    @Override
    public MonthlyReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.monthly_report_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyReportAdapter.ViewHolder holder, int position) {
        holder.monthyear.setText(String.valueOf(monthReportItems.get(position).getMonthYear()));
        holder.sales.setText(String.valueOf(monthReportItems.get(position).getSales()));
        holder.purchases.setText(String.valueOf(monthReportItems.get(position).getPurchases()));
        if(monthReportItems.get(position).getProfit() < 0){
            holder.profit.setTextColor(Color.RED);
        }else
            holder.profit.setTextColor(Color.BLACK);
        holder.profit.setText(String.valueOf(monthReportItems.get(position).getProfit()));
    }

    @Override
    public int getItemCount() {
        return monthReportItems.size();
    }
}
