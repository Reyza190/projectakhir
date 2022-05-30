package com.example.projectakhir.Database.models;

public class Berita {
    private int image_berita;
    private String judul, id, detail_article;

    public Berita(){}

    public Berita(int image_berita, String judul, String detail_article) {
        this.image_berita = image_berita;
        this.judul = judul;
        this.detail_article = detail_article;
    }

    public int getImage_berita() {
        return image_berita;
    }

    public void setImage_berita(int image_berita) {
        this.image_berita = image_berita;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail_article() {
        return detail_article;
    }

    public void setDetail_article(String detail_article) {
        this.detail_article = detail_article;
    }
}
