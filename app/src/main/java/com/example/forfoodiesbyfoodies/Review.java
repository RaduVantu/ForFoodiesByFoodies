package com.example.forfoodiesbyfoodies;

import java.io.Serializable;

//constructor class for taking values from review variables and storing them in a a Review type  object
public class Review {

    String user, review;
    Float stars;

    public Review(String user, String review, Float stars) {
        this.user = user;
        this.review = review;
        this.stars = stars;
    }

    public Review() {}

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public String getReview() { return review; }

    public void setReview(String review) { this.review = review; }

    public Float getStars() { return stars; }

    public void setStars(Float stars) { this.stars = stars; }
}
