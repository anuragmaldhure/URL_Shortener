package com.anurag.miniURL;

import java.util.HashMap;

public class ShortURLImpl implements ShortURL{

    HashMap<String, String> map = new HashMap<>();
    HashMap<String, String> rev_map = new HashMap<>();

    @Override
    public String registerNewUrl(String longUrl) {
        String shortUrl = "http://short.url/";

        if (map.containsKey(longUrl)){
            shortUrl = map.get(longUrl);
        }else{
            shortUrl = shortUrl + getAlphaNumericString(9);
            map.put(longUrl, shortUrl);
            rev_map.put(shortUrl, longUrl);
        }
        return shortUrl;
    }

    //Helper method to generate short URL
    static String getAlphaNumericString(int n){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<n; i++){
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public String registerNewUrl(String longUrl, String shortUrl) {
        String res = "";
        String longUr = getUrl(shortUrl);

        if(map.containsKey(longUr)){
            res = null;
        }else{
            map.put(longUrl, shortUrl);
            rev_map.put(shortUrl, longUrl);
            res = map.get(longUrl);
        }
        getUrl(shortUrl);
        return res;
    }

    HashMap<String, Integer> hitcount = new HashMap<>();
    @Override
    public String getUrl(String shortUrl) {
        String longUrl = null;

        if(rev_map.containsKey(shortUrl)){
            longUrl = rev_map.get(shortUrl);
            hitcount.put(longUrl, hitcount.getOrDefault(longUrl, 0)+1);
        }
        return longUrl;
    }

    @Override
    public Integer getHitCount(String longUrl) {
        return hitcount.getOrDefault(longUrl, 0);
    }

    @Override
    public String delete(String longUrl) {
        String deleted = "";
        deleted = map.remove(longUrl);
        rev_map.entrySet().removeIf(entry -> (longUrl.equals(entry.getValue())));
        return deleted;
    }
}



