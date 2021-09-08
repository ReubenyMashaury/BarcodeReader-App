package info.androidhive.barcodereader.ui.sales;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import info.androidhive.barcodereader.R;
import info.androidhive.barcodereader.SQLiteDatabaseFolder.DatabaseHandler;
import info.androidhive.barcodereader.Sale;
import info.androidhive.barcodereader.SaleInterface;
import info.androidhive.barcodereader.SalesAdapter;

public class SalesFragment extends Fragment implements SaleInterface {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHandler db;
    ArrayList<Sale> saleArrayList;
    SalesAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        slideshowViewModel =
//                ViewModelProviders.of(this).get(SalesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sales, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        saleArrayList = db.getAllSales();

        adapter = new SalesAdapter(getContext(), db.getAllSales(), this);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        db = new DatabaseHandler(getContext());
    }

    @Override
    public void getPosition(Sale sale) {
        int position = 0;
        for (int i = 0; i < saleArrayList.size(); i++) {
            if (saleArrayList.get(i).equals(sale)) {
                saleArrayList.remove(sale);
                position = i;
            }
        }
        adapter.notifyItemRemoved(position + 1);
        adapter.notifyItemRangeChanged(position + 1, saleArrayList.size());

    }

}