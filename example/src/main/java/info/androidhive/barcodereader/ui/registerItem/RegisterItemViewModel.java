package info.androidhive.barcodereader.ui.registerItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterItemViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RegisterItemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Register Item fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}