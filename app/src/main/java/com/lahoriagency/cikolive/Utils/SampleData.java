package com.lahoriagency.cikolive.Utils;


import android.net.Uri;

import com.lahoriagency.cikolive.Classes.SavedProfile;

import java.util.ArrayList;
import java.util.List;

public class SampleData {
    protected static final List<SavedProfile> savedProfileList = new ArrayList<>();
    protected static final String[] imagesOfModel = {"https://www.whatsappprofiledpimages.com/wp-content/uploads/2021/08/Profile-Photo-Wallpaper.jpg",
            "https://images.unsplash.com/photo-1474978528675-4a50a4508dc3?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTV8fHByb2ZpbGV8ZW58MHx8MHx8&ixlib=rb-1.2.1&w=1000&q=80",
            "https://images.unsplash.com/photo-1551024739-78e9d60c45ca?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80",
            "https://images.unsplash.com/photo-1529680459049-bf0340fa0755?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1534008757030-27299c4371b6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
            "https://images.unsplash.com/photo-1513207565459-d7f36bfa1222?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=689&q=80"};
    protected static final String[] imagesOfHeart = {"https://freepngimg.com/thumb/heart/10-heart-png-image-download.png",
            "https://pngimg.com/uploads/love/love_PNG79.png",
            "https://freepngimg.com/thumb/heart/10-heart-png-image-download.png",
            "https://www.pngitem.com/pimgs/m/186-1863131_transparent-background-png-heart-hd-png-download.png"};


    public static String getModelImagePath(int p) {
        return imagesOfModel[p];
    }

    public static String getHeartImagePath(int p) {
        return imagesOfHeart[p];
    }

    public static String[] getImagesOfModel() {
        return imagesOfModel;
    }

    public SampleData() {
        makeListOfSavedProfiles();
    }

    public void makeListOfSavedProfiles() {
        savedProfileList.clear();

        SavedProfile item1 = new SavedProfile();
        SavedProfile item2 = new SavedProfile();
        SavedProfile item3 = new SavedProfile();
        SavedProfile item4 = new SavedProfile();


        item1.setSavedPName("Jhonson Smith ");
        item1.setSavedPLocation("Paris,France1");
        item1.setSavedPAge("15");
        item1.setSavedPImage(Uri.parse(SampleData.imagesOfModel[1]));

        item2.setSavedPName("Mimi Brown");
        item2.setSavedPLocation("Paris,France2");
        item2.setSavedPAge("25");
        item2.setSavedPImage(Uri.parse(SampleData.imagesOfModel[0]));

        item3.setSavedPName("Jhonson Smith ");
        item3.setSavedPLocation("Paris,France3");
        item3.setSavedPAge("15");
        item3.setSavedPImage(Uri.parse(SampleData.imagesOfModel[4]));


        item4.setSavedPName("Janny Goa");
        item4.setSavedPLocation("Paris,France4");
        item4.setSavedPAge("35");
        item4.setSavedPImage(Uri.parse(SampleData.imagesOfModel[5]));

        savedProfileList.add(item1);
        savedProfileList.add(item2);
        savedProfileList.add(item3);
        savedProfileList.add(item4);


    }

}


