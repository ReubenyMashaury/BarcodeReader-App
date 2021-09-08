package info.androidhive.barcodereader.ui.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import info.androidhive.barcodereader.MonthlyReportAdapter;
import info.androidhive.barcodereader.R;
import info.androidhive.barcodereader.SQLiteDatabaseFolder.DatabaseHandler;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseHandler db;

    DatePickerDialog picker;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        db = new DatabaseHandler(getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        final EditText datepicker = root.findViewById(R.id.datepicker);
        final TextView sales = root.findViewById(R.id.sales);
        final TextView purchases = root.findViewById(R.id.purchases);
        final TextView profit = root.findViewById(R.id.profit);

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month = "", day = "";
                                if (String.valueOf(monthOfYear + 1).length() == 1) {
                                    month = "0" + String.valueOf(monthOfYear + 1);
                                }else{
                                    month = String.valueOf(monthOfYear + 1);
                                }

                                if (String.valueOf(dayOfMonth).length() == 1) {
                                    day = "0" + String.valueOf(dayOfMonth);
                                }else{
                                    day = String.valueOf(dayOfMonth);
                                }

                                datepicker.setText(year + "-" + month + "-" + day);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        datepicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sales.setText(String.valueOf(db.getDailyReport(s.toString()).getSales()));
                purchases.setText(String.valueOf(db.getDailyReport(s.toString()).getPurchases()));
                profit.setText(String.valueOf(db.getDailyReport(s.toString()).getProfit()));

            }
        });

        datepicker.setText(simpleDateFormat.format(new Date().getTime()));

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        MonthlyReportAdapter adapter = new MonthlyReportAdapter(getContext(), db.getMonthlyReportItems());
        recyclerView.setAdapter(adapter);

        return root;
    }
}