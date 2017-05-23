package io.hkmushtaq.sampleapp.models;

public class Photo {

    private final String id;
    private final String owner;
    private final String secret;
    private final int isfriend;
    private final String server;
    private final String title;
    private final int ispublic;
    private final int isfamily;
    private final int farm;

    public Photo(String id, String owner, String secret, int isfriend, String server,
            String title, int ispublic, int isfamily, int farm) {
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.isfriend = isfriend;
        this.server = server;
        this.title = title;
        this.ispublic = ispublic;
        this.isfamily = isfamily;
        this.farm = farm;
    }

    public int getFarm() {
        return farm;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public int isfriend() {
        return isfriend;
    }

    public String getServer() {
        return server;
    }

    public String getTitle() {
        return title;
    }

    public int ispublic() {
        return ispublic;
    }

    public int isfamily() {
        return isfamily;
    }

    public String getUri() {
        return "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret
                + "_n.jpg";
    }

}
