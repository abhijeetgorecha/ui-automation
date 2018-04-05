package com.ptc.integrity.automation.core.keydriven;

import gherkin.deps.net.iharder.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class LocalizationTokens {


    public static void main(String[] args) throws Exception {

//        String encoding = Base64.getEncoder().encodeToString(("Administrator:trUf6yuz2?_Gub")).getBytes();
            String authStr = "Administrator" + ":" + "trUf6yuz2?_Gub";
            String authEncoded = Base64.encodeBytes(authStr.getBytes());
            String  USER_AGENT = "Mozilla/5.0";
            String url = "https://localhost:8999/Thingworx/LocalizationTables/zh-Hans-CN/Services/GetTokens";

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", "Basic " + authEncoded);
            post.setHeader("Content-Type","application/json");

            // add header
//            post.setHeader("User-Agent", USER_AGENT);
 HttpResponse response = client.execute(post);
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + post.getEntity());
            System.out.println("Response Code : " +
                    response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println(result.toString());
        Document doc = Jsoup.parse(result.toString());
        Elements tds = doc.select("tr");
        for(int i = 1 ; i < tds.size() ; i++){

        }
    }

}