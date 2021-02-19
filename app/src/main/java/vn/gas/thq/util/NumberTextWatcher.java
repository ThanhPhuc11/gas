package vn.gas.thq.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import vn.gas.thq.customview.CustomArrayAdapter;

public class NumberTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;
    private CallBackChange callBackChange;

    private EditText et;
    private CustomArrayAdapter suggestAdapter;

    public NumberTextWatcher(EditText et, CustomArrayAdapter adapter, CallBackChange callBackChange) {
        df = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMAN));
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.GERMAN));
        this.et = et;
        hasFractionalPart = false;
        this.callBackChange = callBackChange;
        this.suggestAdapter = adapter;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            String vSuggest = v + "000";
            Number n = df.parse(v);
            Number nSuggest = df.parse(vSuggest);
            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                et.setText(df.format(n));
                suggestAdapter.add(df.format(nSuggest));
            } else {
                et.setText(dfnd.format(n));
                suggestAdapter.add(dfnd.format(nSuggest));
            }
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        }

        et.addTextChangedListener(this);
        callBackChange.afterEditTextChange(s);
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        suggestAdapter.clear();
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }

}
