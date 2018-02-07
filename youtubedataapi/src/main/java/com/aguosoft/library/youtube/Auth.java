package com.aguosoft.library.youtube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by liang on 2016/09/28.
 */

public class Auth {

    /**
     * Define a global instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define a global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new GsonFactory();

    /**
     * This is the directory that will be used under the user's home directory where OAuth tokens will be stored.
     */
    private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";

    public static Credential authorize(List<String> scopes, String credentialDatastore) throws IOException, NoSuchMethodException {
        throw new NoSuchMethodException();
    }

    public static String getApiKey() throws IOException, NoSuchFieldException {
        Properties properties = new Properties();
        File file = new File("youtube.properties");
        if(file.exists()==false){
            throw new FileNotFoundException("Properties file was not found. Please check if you have a youtube.properties file!");
        }
        try {
            properties.load(new FileInputStream(file));
            String apikey =  properties.getProperty("youtube.apikey");
            if(apikey==null || apikey.length()<1){
                throw new NoSuchFieldException("Can not find youtube.apikey in youtube.properties file!");
            }else{
                return apikey;
            }
        } catch (IOException e) {
            throw new IOException("Properties file read failed. Please check the youtube.properties file!", e);
        }
    }
}
