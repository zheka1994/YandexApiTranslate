package com.example.eugen.translateapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String Key = "trnsl.1.1.20180213T193052Z.4cb4d89621201ca0.2fbb2b085ccbbec4dc23c92a9365d9fa6985fbec";
    private Button mTranslateButton;
    private TextView mWordEditText;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTranslateButton = (Button)findViewById(R.id.button);
        mWordEditText = (TextView)findViewById(R.id.editText);
        mTextView = (TextView)findViewById(R.id.textView);
        mTranslateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InetTask().execute(mWordEditText.getText().toString());
            }
        });
    }

    private class InetTask extends AsyncTask<String,Void,Void>{
        private Response response;
        @Override
        protected Void doInBackground(String... strings) {
            try {
                post(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        public void post(String str) throws IOException {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://translate.yandex.net/api/v1.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TranslateService service = retrofit.create(TranslateService.class);
            retrofit2.Response resp = service.getTranslate(Key,str,"en-ru").execute();
            response = (Response)resp.body();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            StringBuilder s = new StringBuilder();
            for(int i = 0; i < response.getText().size(); i++) {
                s.append(response.getText().get(i));
                s.append(" ");
            }
            mTextView.setText(s.toString());
        }
    }
}
