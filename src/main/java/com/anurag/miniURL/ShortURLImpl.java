package com.anurag.miniURL;

import java.util.HashMap;

public class ShortURLImpl implements ShortURL{
    // <longUrl, shortUrl>
    HashMap<String, String> recordMap = new HashMap<>();
    // <shortUrl, longUrl>
    HashMap<String, String> rev_recordMap = new HashMap<>();

    @Override
    //Overloaded method - When user only supplies longUrl and doesn't supply his own shortUrl
    public String registerNewUrl(String longUrl) {
        //A predined starting pattern for shortUrl
        String shortUrl = "http://short.url/";

        //Check if a shortUrl is already present for corresponding longUrl and return it
        if (recordMap.containsKey(longUrl)){
            shortUrl = recordMap.get(longUrl);
        }
        //If not present, create a new shortUrl using helper function(specify the length required)
        //         and append it with prefix shortUrl pattern
        else{
            shortUrl = shortUrl + getAlphaNumericString(9);
            //Also put it in the map recordMap <longUrl, shortUrl> for mapping / record purpose
            //Also put it in the map rev_recordMap <shortUrl, longUrl> for mapping / record purpose
            recordMap.put(longUrl, shortUrl);
            rev_recordMap.put(shortUrl, longUrl);
        }
        //return the generated shortUrl
        return shortUrl;
    }

    //Helper method to generate short URL
    static String getAlphaNumericString(int n){
        //Create alphanumeric string using UpperCase, LowerCase alphabets and numbers
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvwxyz";
        //Iterate over the string and build a string randomly to return such that it is equals to length required
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<n; i++){
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    @Override
    //Overloaded method - When user supplies longUrl as well as his own shortUrl
    public String registerNewUrl(String longUrl, String shortUrl) {
        String res = "";

        String existingLongUrl = getUrl(shortUrl);

        // If shortUrl is already present, return null -> should not be able to register
        //      2 shortUrl for 1 longUrl
        if(recordMap.containsKey(existingLongUrl)){
            res = null;
        }
        //Else, register the specified shortUrl for the given longUrl
        else{
            //Also put it in the map recordMap <longUrl, shortUrl> for mapping / record purpose
            //Also put it in the map rev_recordMap <shortUrl, longUrl> for mapping / record purpose
            recordMap.put(longUrl, shortUrl);
            rev_recordMap.put(shortUrl, longUrl);
            //get shortUrl
            res = recordMap.get(longUrl);
        }

        //Triggering a hit count
        getUrl(shortUrl);
        return res;
    }

    //Map to keep track of Url hits < longUrl(Original Url), Hit count >
    HashMap<String, Integer> hitCountMap = new HashMap<>();

    //Getting original Url (longUrl) from shortUrl -> Hitting the Url
    @Override
    public String getUrl(String shortUrl) {
        // Else, return the corresponding longUrl
        String longUrl = null;

        // Else, return the corresponding longUrl
        if(rev_recordMap.containsKey(shortUrl)){
            longUrl = rev_recordMap.get(shortUrl);

            //Incrementing existing longUrl hit count or setting to zero and then increment it
            //      and adding the entry in hitCountMap
            hitCountMap.put(longUrl, hitCountMap.getOrDefault(longUrl, 0)+1);
        }
        return longUrl;
    }

    // Return the number of times the longUrl has been looked up using getUrl()
    @Override
    public Integer getHitCount(String longUrl) {
        return hitCountMap.getOrDefault(longUrl, 0);
    }

    // Delete the mapping between this longUrl and its corresponding shortUrl
    // Do not zero the Hit Count for this longUrl - Persisting past record
    @Override
    public String delete(String longUrl) {
        //Message to be returned as string
        String deleted = "";
        //Removes the mapping for the corresponding shortUrl from recordMap if present
        // Will return the previous value associated with key
        deleted = recordMap.remove(longUrl);

        //Also delete entry in rev_recordMap
        //Using Lambda Function
        // The line removes entries from rev_recordMap where the values are equal to the longUrl
        // It utilizes removeIf with a lambda expression for the condition.
        rev_recordMap.entrySet().removeIf(entry -> (longUrl.equals(entry.getValue())));

        //returns the deleted shortUrl as string or null if not present
        return deleted;
    }
}



