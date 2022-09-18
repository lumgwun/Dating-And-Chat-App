package com.lahoriagency.cikolive;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.lahoriagency.cikolive.Classes.CityName;
import com.lahoriagency.cikolive.Classes.Comments;
import com.lahoriagency.cikolive.Classes.DiamondHistory;
import com.lahoriagency.cikolive.Classes.Gift;
import com.lahoriagency.cikolive.Classes.Notification;
import com.lahoriagency.cikolive.Classes.PurchaseDiamond;
import com.lahoriagency.cikolive.Classes.RedeemRequest;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Utils.SampleData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseActDiamond extends AppCompatActivity {
    public final List<String> listOfImages = new ArrayList<>();
    public final List<Notification> listOfNotification = new ArrayList<>();
    public final List<PurchaseDiamond> listOfPurchaseDiamond = new ArrayList<>();
    public final List<Gift> giftList = new ArrayList<>();
    public final List<RedeemRequest> listOfRedeems = new ArrayList<>();
    public final List<DiamondHistory> listOfDiamondHistory = new ArrayList<>();
    public final List<CityName> cityList = new ArrayList<>();
    public final List<SavedProfile> savedProfileList = new ArrayList<>();
    public final List<Comments> commentsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base_act_diamond);
        makeListOfSavedProfiles();
        makeListOfNotification();
        makeListOfCity();
        makeListOfImages();
        makeListOfRedeems();
        makeListDiamondHistory();
        makeListOfDiamonds();
        makeListOfGift();
    }
    public void makeListOfSavedProfiles() {
        savedProfileList.clear();

        SavedProfile item1 = new SavedProfile();
        SavedProfile item2 = new SavedProfile();
        SavedProfile item3 = new SavedProfile();
        SavedProfile item4 = new SavedProfile();


        item1.setSavedPName("Jhonson Smith ");
        item1.setSavedPLocation("Paris,France");
        item1.setSavedPAge("15");
        item1.setSavedPImage(Uri.parse(SampleData.getModelImagePath(1)));

        item2.setSavedPName("Mimi Brown");
        item2.setSavedPLocation("London,Uk");
        item2.setSavedPAge("25");
        item2.setSavedPImage(Uri.parse(SampleData.getModelImagePath(0)));

        item3.setSavedPName("Jhonson Smith");
        item3.setSavedPLocation("Surat,India");
        item3.setSavedPAge("15");
        item3.setSavedPImage(Uri.parse(SampleData.getModelImagePath(4)));


        item4.setSavedPName("Janny Goa");
        item4.setSavedPLocation("NewYork,Us");
        item4.setSavedPAge("35");
        item4.setSavedPImage(Uri.parse(SampleData.getModelImagePath(5)));

        savedProfileList.add(item1);
        savedProfileList.add(item2);
        savedProfileList.add(item3);
        savedProfileList.add(item4);


    }


    public void makeListOfNotification() {
        listOfNotification.clear();
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        Notification notification3 = new Notification();


        notification1.setHeadLine("Pinky Arora is available");
        notification1.setDescription("Pinkey Arora is available to take calls and\n" +
                "chat, message her and have fun \uD83D\uDE0D");
        notification1.setImage(SampleData.getModelImagePath(1));
        notification1.setTime("5min");


        notification2.setHeadLine("Jessy Sharma is available");
        notification2.setDescription("chat, message her and have fun \uD83D\uDE0D");
        notification2.setImage(SampleData.getModelImagePath(2));
        notification2.setTime("25min");


        notification3.setHeadLine("Pinky Arora is available");
        notification3.setDescription("Diamond Rated are down today, Grab them\n" +
                "at 15% off and enjoy calling and chatting..");
        notification3.setImage(SampleData.getModelImagePath(0));
        notification3.setTime("5min");


        listOfNotification.add(notification1);
        listOfNotification.add(notification2);
        listOfNotification.add(notification3);
        Log.i("TAG", "onCreate: " + listOfNotification.size());


    }
    public void makeListOfCity() {
        cityList.clear();


        CityName item1 = new CityName();
        CityName item2 = new CityName();
        CityName item3 = new CityName();
        CityName item4 = new CityName();
        CityName item5 = new CityName();
        CityName item6 = new CityName();

        item1.setName("Global");
        item1.setId(0);

        item2.setName("Paris");
        item2.setId(1);

        item3.setName("London");
        item3.setId(2);

        item4.setName("Torrento");
        item4.setId(3);

        item5.setName("Bali");
        item5.setId(4);


        item6.setName("Delhi");
        item6.setId(5);

        cityList.add(item1);
        cityList.add(item2);
        cityList.add(item3);
        cityList.add(item4);
        cityList.add(item5);
        cityList.add(item6);

    }

    public void makeListOfImages() {
        listOfImages.clear();
        listOfImages.addAll(Arrays.asList(SampleData.getImagesOfModel()));
    }

    public void makeListOfComments() {
        commentsList.clear();

        Comments model1 = new Comments();
        Comments model2 = new Comments();
        Comments model3 = new Comments();
        Comments model4 = new Comments();

        model1.setName("Hamza Patel");
        model1.setComment("This teacher is so so amazing,\n" +
                "her eyes are little scary \uD83D\uDE12");
        model1.setImage(SampleData.getModelImagePath(2));
        model1.setId(1);
        model1.setPlace("Dubai");
        model1.setAge(22);


        model2.setName("Kety Kate");
        model2.setGift("https://freepngimg.com/thumb/heart/10-heart-png-image-download.png");
        model2.setImage(SampleData.getModelImagePath(1));
        model2.setId(2);
        model2.setPlace("South Africa");
        model2.setAge(12);


        model3.setName("Jhon Maina");
        model3.setComment("Hello everyone ✌");
        model3.setImage(SampleData.getModelImagePath(4));
        model3.setId(1);
        model3.setPlace("USA");
        model3.setAge(42);


        model4.setName("Siara Andrewz");
        model4.setComment("Hey, Cuttie. See you after many days.\n" +
                "Looking so nice dear \uD83D\uDD25");
        model4.setImage(SampleData.getModelImagePath(3));
        model4.setId(1);
        model4.setPlace("Germany");
        model4.setAge(22);

        commentsList.add(model1);
        commentsList.add(model2);
        commentsList.add(model3);
        commentsList.add(model4);

    }

    public void makeListOfRedeems() {

        listOfRedeems.clear();
        RedeemRequest model1 = new RedeemRequest();
        RedeemRequest model2 = new RedeemRequest();
        RedeemRequest model3 = new RedeemRequest();
        RedeemRequest model4 = new RedeemRequest();

        model1.setRr_Id(987);
        model1.setRr_Count(10000);
        model1.setRr_Date("5 Feb 2021");
        model1.setRr_Type(0);


        model2.setRr_Id(654);
        model2.setRr_Count(6000);
        model2.setRr_Date("1 Sep 2021");
        model2.setRr_Type(1);
        model2.setRr_Amount("$2550");

        model3.setRr_Id(6432);
        model3.setRr_Count(7400);
        model3.setRr_Date("5 Oct 2021");
        model3.setRr_Type(1);
        model3.setRr_Amount("$250");

        model4.setRr_Id(7654);
        model4.setRr_Count(8000);
        model4.setRr_Date("25 Sep 2021");
        model4.setRr_Type(1);
        model4.setRr_Amount("$2650");


        listOfRedeems.add(model1);
        listOfRedeems.add(model2);
        listOfRedeems.add(model3);
        listOfRedeems.add(model4);
    }

    public void makeListDiamondHistory() {

        listOfDiamondHistory.clear();
        DiamondHistory model1 = new DiamondHistory();
        DiamondHistory model2 = new DiamondHistory();
        DiamondHistory model3 = new DiamondHistory();
        DiamondHistory model4 = new DiamondHistory();
        DiamondHistory model5 = new DiamondHistory();
        DiamondHistory model6 = new DiamondHistory();


        model1.setdH_Count(500);
        model1.setdH_Date("25 Sep 2021");
        model1.setdH_From("LIVESTREAM");


        model2.setdH_Count(50);
        model2.setdH_Date("15 Sep 2021");
        model2.setdH_From(getString(R.string.chatting));


        model3.setdH_Count(5);
        model3.setdH_Date("5 Sep 2021");
        model3.setdH_From("VIDEO CALL");


        model4.setdH_Count(50);
        model4.setdH_Date("5 Oct 2021");
        model4.setdH_From("LIVESTREAM");


        model5.setdH_Count(40);
        model5.setdH_Date("5 Fab 2021");
        model5.setdH_From(getString(R.string.chatting));

        model6.setdH_Count(10);
        model6.setdH_Date("5 Sep 2021");
        model6.setdH_From(getString(R.string.chatting));


        listOfDiamondHistory.add(model1);
        listOfDiamondHistory.add(model2);
        listOfDiamondHistory.add(model3);
        listOfDiamondHistory.add(model4);
        listOfDiamondHistory.add(model5);
        listOfDiamondHistory.add(model6);
    }

    public void makeListOfDiamonds() {

        listOfPurchaseDiamond.clear();
        PurchaseDiamond purchaseDiamond1 = new PurchaseDiamond();
        PurchaseDiamond purchaseDiamond2 = new PurchaseDiamond();
        PurchaseDiamond purchaseDiamond3 = new PurchaseDiamond();
        PurchaseDiamond purchaseDiamond4 = new PurchaseDiamond();
        purchaseDiamond1.setCount(1500);
        purchaseDiamond1.setPrice("5");
        listOfPurchaseDiamond.add(purchaseDiamond1);

        purchaseDiamond2.setCount(5000);
        purchaseDiamond2.setPrice("15");
        listOfPurchaseDiamond.add(purchaseDiamond2);

        purchaseDiamond3.setCount(10000);
        purchaseDiamond3.setPrice("25");
        listOfPurchaseDiamond.add(purchaseDiamond3);

        purchaseDiamond4.setCount(20000);
        purchaseDiamond4.setPrice("45");
        listOfPurchaseDiamond.add(purchaseDiamond4);

    }

    public void makeListOfGift() {

        giftList.clear();
        Gift model1 = new Gift();
        Gift model2 = new Gift();
        Gift model3 = new Gift();
        Gift model4 = new Gift();

        model1.setCount(20);
        model1.setImage(SampleData.getHeartImagePath(0));

        model2.setCount(30);
        model2.setImage(SampleData.getHeartImagePath(1));

        model3.setCount(10);
        model3.setImage(SampleData.getHeartImagePath(2));

        model4.setCount(50);
        model4.setImage(SampleData.getHeartImagePath(3));

        giftList.add(model1);
        giftList.add(model2);
        giftList.add(model3);
        giftList.add(model4);

    }
}