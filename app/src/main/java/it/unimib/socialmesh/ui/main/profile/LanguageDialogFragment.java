package it.unimib.socialmesh.ui.main.profile;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.util.LocaleManager;

public class LanguageDialogFragment extends DialogFragment {

    private Context context;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        context = requireContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Language");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        adapter.add("Italiano");
        adapter.add("English");
        adapter.add("Español");
        adapter.add("中文");

        builder.setAdapter(adapter, (dialog, which) -> {
            String selectedLanguage = adapter.getItem(which);
            setLocale(selectedLanguage);
        });

        return builder.create();
    }

    private void setLocale(String selectedLanguage) {
        LocaleManager lang = new LocaleManager(getContext());
        switch (selectedLanguage) {
            case "Italiano":
                lang.updateResource("it");
                break;
            case "English":
                lang.updateResource("en");
                break;
            case "Español":
                lang.updateResource("es");
                break;
            case "中文":
                lang.updateResource("zh");
                break;
            default:
             break;
        }


        dismiss();
        getActivity().recreate();
    }

}
