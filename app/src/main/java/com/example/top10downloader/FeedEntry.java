package com.example.top10downloader;

public class FeedEntry {

    private String name;
    private String artist;
    private String relaseDate;
    private String summary;
    private String imageURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getRelaseDate() {
        return relaseDate;
    }

    public void setRelaseDate(String relaseDate) {
        this.relaseDate = relaseDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

//    @Override
//    public String toString() {
//        return "name=" + name + '\n' +
//                ", artist=" + artist + '\n' +
//                ", relaseDate=" + relaseDate + '\n' +
//                ", imageURL=" + imageURL + '\n';
//    }
}
