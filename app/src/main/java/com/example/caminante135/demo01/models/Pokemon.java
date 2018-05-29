package com.example.caminante135.demo01.models;

public class Pokemon {

    private int number;
    private String name;
    private String url;

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        // Separamos cada parte de la url por el simbolo "/" con el metodo split
        String[] urlPartes = url.split("/");
        return Integer.parseInt(urlPartes[urlPartes.length -1]);
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
