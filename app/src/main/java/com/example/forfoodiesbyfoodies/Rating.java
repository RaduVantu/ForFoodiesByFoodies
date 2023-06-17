package com.example.forfoodiesbyfoodies;


//constructor class for taking star values and storing them as ratings in a a Rating type  object
public class Rating {

    Float stars;

    public Rating(Float rating) {
        this.stars = rating;
    }

    public Rating () {}

    public Float getStars() { return stars;}

    public void setStars(Float stars) { this.stars = stars; }
}
