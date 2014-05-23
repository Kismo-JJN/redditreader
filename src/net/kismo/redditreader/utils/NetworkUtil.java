package net.kismo.redditreader.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {
	public NetworkUtil(){
		// empty constructor
	}
	
    public String get(String url)
    {
    	HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 7000);
        HttpConnectionParams.setSoTimeout(httpParams, 7000);
    	HttpClient httpclient = new DefaultHttpClient(httpParams);

        String result ="";

        try {
        	HttpGet httpget = new HttpGet(url);          
  	       	HttpResponse response = httpclient.execute(httpget); 
  	       	StatusLine statusLine = response.getStatusLine();
  	       	int statusCode = statusLine.getStatusCode();
  	       	
  	       	if (statusCode == 200) {
  	       		HttpEntity entity = response.getEntity(); 
  	        	if (entity != null) { 
  		    	     InputStream instream = entity.getContent();
  		    	     result= convertStreamToString(instream);
  		   	     
  		    	     // Closing the input stream will trigger connection release
  		    	     instream.close();                            
  	         	}
  	       	}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 

        return result;
     }
	
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");               
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();
    } 
    
    public boolean isOnline(Context context) {
    	 ConnectivityManager cm = 
    		 (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	 return cm.getActiveNetworkInfo().isConnectedOrConnecting();

    }
	
}
