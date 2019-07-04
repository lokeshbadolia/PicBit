package com.picbit.info.android;

import android.widget.LinearLayout;

/**
 * Created by lokesh badolia on 6/7/2017.
 */

public class User_Data {

    private String title;
    private String descreption;
    private String image;
    private String username;
    private String profileimage;
    private String time;
    private String youtubelink;
    private String totalLike;
    private String Status_title, Status_descreption;
    private String Comment_time;
    private String Comment;

    public User_Data() {
    }
    //----------------------------------------------------------------


    public User_Data(String title, String descreption, String image, String username, String profileimage, String time, String youtubelink, String totalLike, String Comment_time, String Comment) {
        this.title = title;
        this.descreption = descreption;
        this.image = image;
        this.username = username;
        this.profileimage = profileimage;
        this.time = time;
        this.youtubelink = youtubelink;
        this.totalLike = totalLike;
        this.Comment_time = Comment_time;
        this.Comment = Comment;

    }


    //-------------------------------------------------------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getYoutubelink() {
        return youtubelink;
    }

    public void setYoutubelink(String youtubelink) {
        this.youtubelink = youtubelink;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }


    public String getComment_time() {
        return Comment_time;
    }

    public void setComment_time(String comment_time) {
        Comment_time = comment_time;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }


    //-------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------

}
