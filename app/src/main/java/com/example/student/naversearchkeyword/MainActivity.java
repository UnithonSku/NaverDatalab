package com.example.student.naversearchkeyword;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
TextView textView,textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        textView2 = (TextView)findViewById(R.id.textView2);
        NaverSearchDataLab naverSearchDataLab = new NaverSearchDataLab();
        naverSearchDataLab.execute();

    }

    public class NaverSearchDataLab extends AsyncTask <String,String,String>{
        String clientid = "_a4fbKfLQFHmS8MMXQrh";
        String clientSecret = "QHWvMXwRVu";
        String parsingData,RawData;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String apiURL = "https://openapi.naver.com/v1/datalab/search";
                String body = "{\"startDate\":\"2017-01-01\",\"endDate\":\"2017-04-30\",\"timeUnit\":\"month\",\"keywordGroups\":[{\"groupName\":\"숫자1\",\"keywords\":[\"숫자2\",\"korean\"]},{\"groupName\":\"숫자3\",\"keywords\":[\"숫자4\",\"english\"]}],\"device\":\"pc\",\"ages\":[\"1\",\"2\"],\"gender\":\"f\"}";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientid);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                con.setRequestProperty("Content-Type", "application/json");

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(body.getBytes());
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }

                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                parsingData = response.toString();
                br.close();
                System.out.println(response.toString());
                JSONObject jsonObject = new JSONObject(response.toString());
                Log.d("ysj","json값 실행시작");
                //response2 = jsonObject.getJSONObject("keywordGroups").get("groupName").toString();
                parsingData =  jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).get("period").toString(); //데이터 받아오기
                RawData = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).get("ratio").toString(); //데이터 가져오기
                Log.d("ysj", parsingData);

            } catch (Exception e) {
                System.out.println(e);
            }
            return parsingData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(parsingData+"");
            textView2.setText(RawData+"");
        }

    }
}



