package com.maximeg.my_french_certificate.fragments;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.maximeg.my_french_certificate.R;
import com.maximeg.my_french_certificate.constants.Constants;
import com.maximeg.my_french_certificate.sqlite.QRContract;
import com.maximeg.my_french_certificate.sqlite.QRDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;

public class FormFragment extends Fragment {

    private TextInputEditText firstNameEdit;
    private String firstName;

    private TextInputEditText lastNameEdit;
    private String lastName;

    private TextInputEditText birthDateEdit;
    private String birthDate;

    private TextInputEditText birthPlaceEdit;
    private String birthPlace;

    private TextInputEditText addressEdit;
    private String address;

    private TextInputEditText cityEdit;
    private String city;

    private TextInputEditText postalCodeEdit;
    private String postalCode;

    private RadioGroup radioGroup;
    private String activity;

    private TextInputEditText dateEdit;
    private TextInputEditText timeEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        long today = new Date().getTime();

        CalendarConstraints birthDateConstraints = new CalendarConstraints.Builder().setOpenAt(1L).build();
        final MaterialDatePicker birthDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText(R.string.birth_date).setSelection(1L).setCalendarConstraints(birthDateConstraints).build();

        CalendarConstraints dateConstraints = new CalendarConstraints.Builder().setOpenAt(today).build();
        final MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(R.string.leaving_date).setSelection(today).setCalendarConstraints(dateConstraints).build();

        firstNameEdit = view.findViewById(R.id.first_name);
        lastNameEdit = view.findViewById(R.id.last_name);
        birthDateEdit = view.findViewById(R.id.birth_date);
        birthPlaceEdit = view.findViewById(R.id.birth_place);
        addressEdit = view.findViewById(R.id.address);
        cityEdit = view.findViewById(R.id.city);
        postalCodeEdit = view.findViewById(R.id.postal_code);

        radioGroup = view.findViewById(R.id.radio_group);

        dateEdit = view.findViewById(R.id.date);
        timeEdit = view.findViewById(R.id.hour);

        MaterialButton generateButton = view.findViewById(R.id.button_generate);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        dateEdit.setText((format.format(new Date(today))));

        format = new SimpleDateFormat("HH", Locale.FRANCE);
        String hour = format.format(new Date());

        format = new SimpleDateFormat("mm", Locale.FRANCE);
        String minute = format.format(new Date());

        timeEdit.setText(hour + ":" + minute);

        //birth date

        birthDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

                birthDateEdit.setText(simpleDateFormat.format(new Date(selectedDate)));

                birthDateEdit.clearFocus();
            }
        });

        birthDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDateEdit.clearFocus();
            }
        });

        birthDatePicker.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                birthDateEdit.clearFocus();
            }
        });

        birthDateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && getFragmentManager() != null){
                    birthDatePicker.show(getFragmentManager(), birthDatePicker.toString());
                }
            }
        });

        //date picker

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

                dateEdit.setText(simpleDateFormat.format(new Date(selectedDate)));

                dateEdit.clearFocus();
            }
        });

        datePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateEdit.clearFocus();
            }
        });

        datePicker.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dateEdit.clearFocus();
            }
        });

        dateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && getFragmentManager() != null){
                    datePicker.show(getFragmentManager(), datePicker.toString());
                }
            }
        });

        //time picker

        final TimePickerDialog timeDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                String hourToSet;
                if(hourOfDay < 10){
                    hourToSet = "0" + hourOfDay;
                }
                else{
                    hourToSet = String.valueOf(hourOfDay);
                }

                String minuteToSet;
                if(minuteOfHour < 10){
                    minuteToSet = "0" + minuteOfHour;
                }
                else{
                    minuteToSet = String.valueOf(minuteOfHour);
                }

                timeEdit.setText(hourToSet + ":" + minuteToSet);

                timeEdit.clearFocus();
            }
        }, Integer.parseInt(hour), Integer.parseInt(minute), true);

        timeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                timeEdit.clearFocus();
            }
        });

        timeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    timeDialog.show();
                }
            }
        });

        loadSaveValues();

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll();
            }
        });
    }

    private void loadSaveValues() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String json = sharedPref.getString(Constants.SHARED_PREFERENCES, "");

        try {
            JSONObject reader = new JSONObject(json);

            firstNameEdit.setText(reader.optString(Constants.FIRST_NAME));
            lastNameEdit.setText(reader.optString(Constants.LAST_NAME));
            birthDateEdit.setText(reader.optString(Constants.BIRTH_DATE));
            birthPlaceEdit.setText(reader.optString(Constants.BIRTH_PLACE));
            addressEdit.setText(reader.optString(Constants.ADDRESS));
            cityEdit.setText(reader.optString(Constants.CITY));
            postalCodeEdit.setText(reader.optString(Constants.POSTAL_CODE));
            actionRadioValue(false, reader.optString(Constants.ACTIVITY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actionRadioValue(boolean needsActivity, String value) {
        if(needsActivity){
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_work:
                    activity = Constants.ACTIVITY_WORK;
                    break;
                case R.id.radio_goods:
                    activity = Constants.ACTIVITY_GOODS;
                    break;
                case R.id.radio_care:
                    activity = Constants.ACTIVITY_CARE;
                    break;
                case R.id.radio_family:
                    activity = Constants.ACTIVITY_FAMILY;
                    break;
                case R.id.radio_stuff:
                    activity = Constants.ACTIVITY_STUFF;
                    break;
                case R.id.radio_administration:
                    activity = Constants.ACTIVITY_ADMINISTRATION;
                    break;
                case R.id.radio_authority:
                    activity = Constants.ACTIVITY_AUTHORITY;
                    break;
            }
        }
        else{
            switch (value) {
                case Constants.ACTIVITY_WORK:
                    radioGroup.check(R.id.radio_work);
                    break;
                case Constants.ACTIVITY_GOODS:
                    radioGroup.check(R.id.radio_goods);
                    break;
                case Constants.ACTIVITY_CARE:
                    radioGroup.check(R.id.radio_care);
                    break;
                case Constants.ACTIVITY_FAMILY:
                    radioGroup.check(R.id.radio_family);
                    break;
                case Constants.ACTIVITY_STUFF:
                    radioGroup.check(R.id.radio_stuff);
                    break;
                case Constants.ACTIVITY_ADMINISTRATION:
                    radioGroup.check(R.id.radio_administration);
                    break;
                case Constants.ACTIVITY_AUTHORITY:
                    radioGroup.check(R.id.radio_authority);
                    break;
                default:
                    break;
            }
        }
    }

    private boolean checkText(Editable editable, String regex){
        if(editable != null && !editable.toString().isEmpty()){
            String value = editable.toString();
            if(regex != null){
                return value.matches(regex);
            }
            return true;
        }
        return false;
    }

    private void checkAll(){
        try {
            List<String> errors = new ArrayList<>();

            JSONObject jsonObject = new JSONObject();

            if (!checkText(firstNameEdit.getText(), null)) {
                errors.add(getString(R.string.first_name_lower));
            } else {
                firstName = firstNameEdit.getText().toString();
                jsonObject.put(Constants.FIRST_NAME, firstName);
            }

            if (!checkText(lastNameEdit.getText(), null)) {
                errors.add(getString(R.string.last_name_lower));
            }
            else{
                lastName = lastNameEdit.getText().toString();
                jsonObject.put(Constants.LAST_NAME, lastName);
            }

            if (!checkText(birthDateEdit.getText(), null)) {
                errors.add(getString(R.string.birth_date_lower));
            }
            else {
                birthDate = birthDateEdit.getText().toString();
                jsonObject.put(Constants.BIRTH_DATE, birthDate);
            }

            if (!checkText(birthPlaceEdit.getText(), null)) {
                errors.add(getString(R.string.birth_place_lower));
            }
            else{
                birthPlace = birthPlaceEdit.getText().toString();
                jsonObject.put(Constants.BIRTH_PLACE, birthPlace);
            }

            if (!checkText(addressEdit.getText(), null)) {
                errors.add(getString(R.string.address_lower));
            }
            else{
                address = addressEdit.getText().toString();
                jsonObject.put(Constants.ADDRESS, address);
            }

            if (!checkText(cityEdit.getText(), null)) {
                errors.add(getString(R.string.city_lower));
            }
            else{
                city = cityEdit.getText().toString();
                jsonObject.put(Constants.CITY, city);
            }

            if (!checkText(postalCodeEdit.getText(), "\\d{5}")) {
                errors.add(getString(R.string.postal_code_lower));
            }
            else{
                postalCode = postalCodeEdit.getText().toString();
                jsonObject.put(Constants.POSTAL_CODE, postalCode);
            }

            if (radioGroup.getCheckedRadioButtonId() == -1) {
                errors.add(getString(R.string.activity_lower));
            }
            else{
                actionRadioValue(true, null);
                jsonObject.put(Constants.ACTIVITY, activity);
            }

            if (!checkText(dateEdit.getText(), null)) {
                errors.add(getString(R.string.leaving_date_lower));
            }

            if (!checkText(timeEdit.getText(), null)) {
                errors.add(getString(R.string.leaving_time_lower));
            }

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constants.SHARED_PREFERENCES, jsonObject.toString());
            editor.commit();

            if (!errors.isEmpty()) {
                StringBuilder message = new StringBuilder(getString(R.string.please_type_in));
                for (String error : errors) {
                    if (errors.size() == 1) {
                        message.append(getString(R.string.your)).append(error);
                    } else if (error.equals(errors.get(errors.size() - 1))) {
                        message.append(getString(R.string.and_your)).append(error);
                    } else {
                        if (error.equals(errors.get(errors.size() - 2))) {
                            message.append(getString(R.string.your)).append(error).append(" ");
                        } else {
                            message.append(getString(R.string.your)).append(error).append(", ");
                        }
                    }
                }
                message.append(getString(R.string.to_generate));

                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext()).setTitle(R.string.error).setMessage(message);
                dialog.setPositiveButton(R.string.ok, null);
                dialog.show();
            } else {
                generateAndSaveQRCode();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void generateAndSaveQRCode(){
        long today = new Date().getTime();
        String todayValue = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(today);
        String hourValue = new SimpleDateFormat("HH", Locale.FRANCE).format(today);
        String minuteValue = new SimpleDateFormat("mm", Locale.FRANCE).format(today);

        WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(
            "Cree le: " + todayValue +
                " a " + hourValue + "h" + minuteValue + ";" +
                " Nom: " + lastName + ";" +
                " Prenom: " + firstName + ";" +
                " Naissance: " + birthDate +
                " a " + birthPlace + ";" +
                " Adresse: " + address +
                " " + postalCode +
                " " + city + ";" +
                " Sortie: " + dateEdit.getText().toString() +
                " a " + timeEdit.getText().toString().replace(":", "h") + ";" +
                " Motifs: " + activity
                , null, QRGContents.Type.TEXT, smallerDimension);

        try {
            Bitmap bitmap = qrgEncoder.getBitmap();

            String filename = today + ".png";

            try (FileOutputStream fos = getContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }

            QRDbHelper dbHelper = new QRDbHelper(getContext());

            ContentValues values = new ContentValues();
            values.put(QRContract.COLUMN_NAME_FIRST_NAME, firstName);
            values.put(QRContract.COLUMN_NAME_LAST_NAME, lastName);
            values.put(QRContract.COLUMN_NAME_FILE_NAME, filename);
            values.put(QRContract.COLUMN_NAME_DATE, dateEdit.getText().toString());
            values.put(QRContract.COLUMN_NAME_TIME, timeEdit.getText().toString());

            dbHelper.getWritableDatabase().insert(QRContract.TABLE_NAME, null, values);

            dbHelper.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext()).setTitle(R.string.success).setMessage(R.string.success_message);
        dialog.setPositiveButton(R.string.ok, null);
        dialog.show();
    }
}
