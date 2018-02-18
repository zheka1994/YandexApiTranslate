package com.example.eugen.translateapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String Key = "trnsl.1.1.20180213T193052Z.4cb4d89621201ca0.2fbb2b085ccbbec4dc23c92a9365d9fa6985fbec";
    private Button mTranslateButton;
    private EditText mWordEditText;
    private TextView mTextView;
    private Spinner mSpinner1;
    private Spinner mSpinner2;
    private ImageButton mReverseButton;
    private Languages mLanguages;
    private ArrayList<String> mListLang = new ArrayList<>();
    private Map<String,String> langs;
    int pos1, pos2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        mTranslateButton = findViewById(R.id.button);
        mWordEditText = findViewById(R.id.editText);
        mTextView = findViewById(R.id.textView);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/api/v1.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final TranslateService service = retrofit.create(TranslateService.class);
        service.getLanguages(Key, Locale.getDefault().getLanguage()).enqueue(new Callback<Languages>() {
            @Override
            public void onResponse(Call<Languages> call, retrofit2.Response<Languages> response) {
                if(response.isSuccessful()){
                    mLanguages = response.body();
                    langs = mLanguages.getLangs();
                    for(Map.Entry<String,String> pair : langs.entrySet()){
                        mListLang.add(pair.getValue());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,mListLang.toArray(new String[mListLang.size()]));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner1 = findViewById(R.id.spinner);
                    mSpinner1.setAdapter(adapter);
                    mSpinner2 = findViewById(R.id.spinner2);
                    mSpinner2.setAdapter(adapter);
                    mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            pos1 = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            pos2 = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            pos1 = 0;
                            pos2 = 0;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Languages> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
     mTranslateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromLanguage = mListLang.get(pos1);
                String toLanguage = mListLang.get(pos2);
                String keyFrom = findKey(fromLanguage);
                String keyTo = findKey(toLanguage);
                String text = mWordEditText.getText().toString();
                service.getTranslate(Key,text,keyFrom+"-"+keyTo).enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        if(response.isSuccessful()){
                            Response resp = response.body();
                            StringBuilder s = new StringBuilder();
                            for(int i = 0; i < resp.getText().size(); i++) {
                                s.append(resp.getText().get(i));
                                s.append(" ");
                            }
                            mTextView.setText(s.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {

                    }
                });
            }
        });
     mReverseButton = findViewById(R.id.reverseButton);
     mReverseButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             reverse();
             mSpinner1.setSelection(pos1);
             mSpinner2.setSelection(pos2);
         }
     });
    }
    public String findKey(String val){
        Iterator it = langs.entrySet().iterator();
        String str = null;
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            if(pair.getValue().equals(val)){
                str = pair.getKey().toString();
            }
        }
        return str;
    }
    public void reverse(){
        int tmp = pos1;
        pos1 = pos2;
        pos2 = tmp;
    }
}
