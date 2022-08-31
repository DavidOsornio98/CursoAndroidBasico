package training.edu.droidbountyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Fugitivo implements Parcelable {
    private int id;
    private String name;
    private String status;
    private String photo;

    public Fugitivo(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.photo = "";
    }

    public Fugitivo(int id, String name, String status, String photo) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.photo = photo;
    }

    protected Fugitivo(Parcel in) {
        id = in.readInt();
        name = in.readString();
        status = in.readString();
        photo = in.readString();
    }

    public static final Creator<Fugitivo> CREATOR = new Creator<Fugitivo>() {
        @Override
        public Fugitivo createFromParcel(Parcel in) {
            return new Fugitivo(in);
        }

        @Override
        public Fugitivo[] newArray(int size) {
            return new Fugitivo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(status);
        parcel.writeString(photo);
    }
}
