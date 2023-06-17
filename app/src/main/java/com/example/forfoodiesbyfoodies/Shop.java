package com.example.forfoodiesbyfoodies;

import android.os.Parcel;
import android.os.Parcelable;

//constructor class for taking values from shop variables and storing them in a Shop type object
public class Shop implements Parcelable {
    //Parcelable can be used to transfer an array containing the variables that form a Shop type object

    String name, address, desc, imgUrl, web, type;

    public Shop(String name, String address, String desc, String imgUrl, String web, String type) {
        this.name = name;
        this.address = address;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.web = web;
        this.type = type;
    }

    //empty constructor used by firebase
    public Shop() {}

    //function that reads the variables to be included in the parcel
    protected Shop(Parcel in) {
        name = in.readString();
        address = in.readString();
        desc = in.readString();
        imgUrl = in.readString();
        web = in.readString();
        type = in.readString();
    }


    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        //this array stores the parcel content
        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //function that writes the variables in the parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(desc);
        dest.writeString(imgUrl);
        dest.writeString(web);
        dest.writeString(type);
    }
}
