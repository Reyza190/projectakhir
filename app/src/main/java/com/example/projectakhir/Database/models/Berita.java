package com.example.projectakhir.Database.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Berita implements Parcelable {
    private String image_berita;
    private String judul, id, detail_article;

    public Berita(){}

    public Berita(String image_berita, String judul, String detail_article) {
        this.image_berita = image_berita;
        this.judul = judul;
        this.detail_article = detail_article;
    }

    public String getImage_berita() {
        return image_berita;
    }

    public void setImage_berita(String image_berita) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image_berita);
        dest.writeString(this.judul);
        dest.writeString(this.id);
        dest.writeString(this.detail_article);
    }

    public void readFromParcel(Parcel source) {
        this.image_berita = source.readString();
        this.judul = source.readString();
        this.id = source.readString();
        this.detail_article = source.readString();
    }

    protected Berita(Parcel in) {
        this.image_berita = in.readString();
        this.judul = in.readString();
        this.id = in.readString();
        this.detail_article = in.readString();
    }

    public static final Creator<Berita> CREATOR = new Creator<Berita>() {
        @Override
        public Berita createFromParcel(Parcel source) {
            return new Berita(source);
        }

        @Override
        public Berita[] newArray(int size) {
            return new Berita[size];
        }
    };
}
