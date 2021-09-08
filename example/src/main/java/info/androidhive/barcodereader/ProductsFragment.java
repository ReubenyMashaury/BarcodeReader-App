package info.androidhive.barcodereader;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import info.androidhive.barcodereader.SQLiteDatabaseFolder.DatabaseHandler;

public class ProductsFragment extends Fragment implements ProductInterface {

    RecyclerView.LayoutManager layoutManager;
    DatabaseHandler db;
    ArrayList<Product> productArrayList;
    ProductsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        productArrayList = db.getAllProducts();

        adapter = new ProductsAdapter(getContext(), db.getAllProducts(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        db = new DatabaseHandler(getContext());
    }

    @Override
    public void getPosition(Product product) {
        int position = 0;
        for (int i = 0; i < productArrayList.size(); i++) {
            if (productArrayList.get(i).equals(product)) {
                productArrayList.remove(product);
                position = i;
            }
        }
        adapter.notifyItemRemoved(position + 1);
        adapter.notifyItemRangeChanged(position + 1, productArrayList.size());

    }
}