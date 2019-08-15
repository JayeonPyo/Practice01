package com.example.intentexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

   TextView textView;
   TextView busNumber;
   String getData;
   int busNumber = 0;
   int busNum = 503;
   String strSrch = busNumber +"";
   int count;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       textView = (TextView) findViewById(R.id.data);
       busNumber = (TextView) findViewById(R.id.bus);

       String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";

       String serviceKey = "aGhG51co4M6VEHfSTrleuH%2F4B4yeZfjfi2P0ClVHwRJFgaqwmFkANtcXNtUfZs6KbCiKnEP9q%2BSIRnjxqkoIdg%3D%3D";

       //가져올 정보를 strUrl에 저장하기

       String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch=" + strSrch;

       DownloadWebContent dwc1 = new DownloadWebContent();
       dwc1.execute(strUrl);



    }

    public class DownloadWebContent extends AsyncTask<String, Void,String> {

       @Override
        protected String doInBackground(String ...urls) {
           try{
               return (String) downloadByUrl((String) urls[0]);

           }catch (IOException e){
               return "다운로드 실패";
           }
       }


       protected void onPostExecute (String result){
           //xml 문서를 파싱하는 방법 중 PullParer 사용
           String headerCd="";
           String busRouteId="";

           boolean bus_headerCd = false;
           boolean bus_busRouteId = false;

           //textView.apppend("=====노선아이디=====
           try{

               //xmlpullparer을 위해 xmlparserfactory 객체 생성

               XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
               factory.setNamespaceAware(true);
               XmlPullParser xmlpp = factory.newPullParser();

               //parser에 url를 입력함
               xmlpp.setInput(new StringReader(result));

               //parser 이벤트를 저장할 변수 지정
               int eventType = xmlpp.getEventType();

               while(eventType ! = XmlPullParser.END_DOCUMENT) {
                   if(eventType == XmlPullParser.START_DOCUMENT) {
                       ;
                   } else if (eventType == XmlPullParser.START_TAG) {
                       String tag_name = xmlpp.getName();
                       if(tag_name.equals("headerCd"))
                           bus_headerCd = true;
                       if(tag_name.equals("busRouteId"))
                           bus_busRouteId = true;
                   }else if(eventType == XmlPullParser.TEXT) {
                       if(bus_headerCd){
                           headerCd = xmlpp.getText();
                           bus_headerCd = false;
                       }

                       if(headerCd.equals("0")) {
                           if(bus_busRouteId){
                               busRouteId = xmlpp.getText();
                               bus_busRouteId =false;
                           }
                       }
                   }else if(eventType == XmlPullParser.END_TAG){
                       ;
                   }
                   eventType = xmlpp.next();
               }
           }catch (Exception e) {
               textView.setText(e.getMessage());
           }

           String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";

           String serviceKey = "aGhG51co4M6VEHfSTrleuH%2F4B4yeZfjfi2P0ClVHwRJFgaqwmFkANtcXNtUfZs6KbCiKnEP9q%2BSIRnjxqkoIdg%3D%3D";

           String strUrl = serviceUrl + "?ServiceKey=" + serviceKey
                   + "&busRouteId=" + busRouteId;

           DownloadWebContent2 dwc2 = new DownloadWebContent2();
           dwc2.execute(strUrl);
       }

       public String downloadByUrl (String myurl) throws IOException {
           //HTTP 통신 HttpURLconnection 클래스이용하여 데이터 끌어오기

           HttpURLConnection conn = null;
           try {
               //요청 URL, 전달받은 url string으로 URL 객체를 만듦
               URL url = new URL(myurl);
               conn = (HttpURLConnection) url.openConnection();

               BufferedInputStream buffer =
                       new BufferedInputStream(conn.getInputStream());

               BufferedReader buffer_reader =
                       new BufferedReader(new InputStreamReader(buffer,"utf-8"));


               String line = null;
               getData = "";
               while  ((line = buffer_reader.readLine()) != null) {
                   getData +=line;
               }
               return  getData;
           } finally {
               //접속 해제

               conn.disconnect();
           }
       }
    }
    public class DownloadWebContent2 extends  AsyncTask<String,Void,String> {

       @Override
        protected  String doInBackground(String ... urls) {
           try {
               return  (String) downloadByUrl ((String) urls[0]);

           }catch (IOException e){
               return  "다운로드 실패";
           }
       }

       protected void  onPostExecute(String result){
           String headerCd = "";
           String gpsX = "";
           String gpsY = "";
           String stationNm = "";
           String direction ="";
           String sectSpd = "";
           boolean bus_heagerCd = false;
           boolean bus_gbsX  = false;
           boolean bus_gpsY = false;
           boolean bus_stationNm = false;
           boolean bus_direction = false;
           boolean bus_sectSpd = false;
           textView.append("-버스 위치 검색 결과-\n");
           try {
               XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
               factory.setNamespaceAware(true);
               XmlPullParser xmlpp = factory.newPullParser();

               xmlpp.setInput(new StringReader(result));
               int eventType = xmlpp.getEventType();


               count = 0;
               while (eventType!= XmlPullParser.END_DOCUMENT){
                   if(eventType == XmlPullParser.END_DOCUMENT){
                       ;
                   } else  if(eventType == XmlPullParser.START_TAG) {
                       String tag_name = xmlpp.getName();

                       switch (tag_name) {

                           case  "headerCd":
                               bus_heagerCd = true;
                               break;

                           case  "gpsX":
                               bus_gbsX = true;
                               break;
                           case  "gpsY":
                               bus_gpsY = true;
                               break;
                           case  "sectSpd":
                               bus_sectSpd = true;
                               break;
                           case  "stationNM":
                               bus_stationNm = true;
                               break;
                           case  "direction":
                               bus_direction = true;
                               break;





                       }

                   }else  if(eventType == XmlPullParser.TEXT) {
                       if(bus_heagerCd){
                           headerCd = xmlpp.getText();
                           bus_heagerCd = false;
                       }

                       if(headerCd.equals("0")) {
                           if(bus_gbsX) {
                               count++;
                               textView.append("================================\n");

                               gpsX = xmlpp.getText();
                               textView.append("("+ count + ")gpsX: " + gpsX + "\n");
                               bus_gbsX = false;
                           }
                           if (bus_gpsY) {
                               gpsY = xmlpp.getText();
                               textView.append("("+ count + ")gpsY: " + gpsY + "\n");
                               bus_gpsY= false;
                           }
                           if (bus_stationNm) {
                               gpsY = xmlpp.getText();
                               textView.append("("+ count + ")정류장이름: " + stationNm + "\n");
                               bus_stationNm= false;
                           }
                           if (bus_sectSpd) {
                               gpsY = xmlpp.getText();
                               textView.append("("+ count + ")구간속도: " + sectSpd + "\n");
                               bus_sectSpd= false;
                           }
                       }
                   }else  if(eventType == XmlPullParser.END_TAG) {
                       ;
                   }
                   eventType = xmlpp.next();
               }
           }catch (Exception e){
               textView.setText(e.getMessage());
           }
       }


       public String downloadByUrl (String myurl) throws  IOException {
           //JAVA 와 HttpURLConnection 클래스를 통해 데이터 끙ㄹㄹ어오기


           HttpURLConnection conn = null;
           BufferedReader buffer_reader;
           try{
               //요청 URL 전달받은 url string 으로 URL 객체를 만든다아..
               URL url = new URL (myurl);
               conn = (HttpURLConnection) url.openConnection();
               conn.setRequestMethod("GET");

               BufferedInputStream buffer =
                       new BufferedInputStream(conn.getInputStream());
               buffer_reader = new BufferedReader(
                       new InputStreamReader(buffer,"utf-8"));

               String line = null;
               getData = "";
               while ((line = buffer_reader.readLine()) != null) {
                   getData +=line;
               }

               return  getData;
           } finally {
                //전송 해제
               conn.disconnect();
           }
       }
    }
    public void plusBusNumber(View v){

       busNum += 1;

        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";

        String serviceKey = "aGhG51co4M6VEHfSTrleuH%2F4B4yeZfjfi2P0ClVHwRJFgaqwmFkANtcXNtUfZs6KbCiKnEP9q%2BSIRnjxqkoIdg%3D%3D";
       strSrch = busNum + "";

       //가져올 정보를 strUrl에 저장학,ㅣ
      String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch="
              +strSrch;

      DownloadWebContent dwc1 = new DownloadWebContent();
      dwc1.execute(strUrl);
      textView.setText("");
      busNumber.setText("");
      busNumber.append("버스번호");
      busNumber.append(strSrch + "\n");
    }


    public void minusBusNumber(View v) {

       busNum -=1;
        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";

        String serviceKey = "aGhG51co4M6VEHfSTrleuH%2F4B4yeZfjfi2P0ClVHwRJFgaqwmFkANtcXNtUfZs6KbCiKnEP9q%2BSIRnjxqkoIdg%3D%3D";
        strSrch = busNum + "";

        //가져올 정보를 strUrl에 저장
        String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch="
                +strSrch;

        DownloadWebContent dwc1 = new DownloadWebContent();
        dwc1.execute(strUrl);
        textView.setText("");
        busNumber.setText("");
        busNumber.append("버스번호");
        busNumber.append(strSrch + "\n");

    }

    public void  resetCurrentBus (View v){

       //busNum +=1;
        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";

        String serviceKey = "aGhG51co4M6VEHfSTrleuH%2F4B4yeZfjfi2P0ClVHwRJFgaqwmFkANtcXNtUfZs6KbCiKnEP9q%2BSIRnjxqkoIdg%3D%3D";
        strSrch = busNum + "";

        //가져올 정보를 strUrl에 저장
        String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch="
                +strSrch;

        DownloadWebContent dwc1 = new DownloadWebContent();
        dwc1.execute(strUrl);
        textView.setText("");
        busNumber.setText("");
        busNumber.append("버스번호");
        busNumber.append(strSrch + "\n");


    }
    public void  plusBaek(View v) {
       busNum += 100;
        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";

        String serviceKey = "aGhG51co4M6VEHfSTrleuH%2F4B4yeZfjfi2P0ClVHwRJFgaqwmFkANtcXNtUfZs6KbCiKnEP9q%2BSIRnjxqkoIdg%3D%3D";
        strSrch = busNum + "";

        //가져올 정보를 strUrl에 저장
        String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch="
                +strSrch;

        DownloadWebContent dwc1 = new DownloadWebContent();
        dwc1.execute(strUrl);
        textView.setText("");
        busNumber.setText("");
        busNumber.append("버스번호");
        busNumber.append(strSrch + "\n");

    }
    public void  minusBaek (View v) {
       busNum -=100;
        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";

        String serviceKey = "aGhG51co4M6VEHfSTrleuH%2F4B4yeZfjfi2P0ClVHwRJFgaqwmFkANtcXNtUfZs6KbCiKnEP9q%2BSIRnjxqkoIdg%3D%3D";
        strSrch = busNum + "";

        //가져올 정보를 strUrl에 저장
        String strUrl = serviceUrl + "?ServiceKey=" + serviceKey + "&strSrch="
                +strSrch;

        DownloadWebContent dwc1 = new DownloadWebContent();
        dwc1.execute(strUrl);
        textView.setText("");
        busNumber.setText("");
        busNumber.append("버스번호");
        busNumber.append(strSrch + "\n");

    }


}
