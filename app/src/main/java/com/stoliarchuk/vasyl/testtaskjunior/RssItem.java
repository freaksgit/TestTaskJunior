package com.stoliarchuk.vasyl.testtaskjunior;

import android.os.Parcel;
import android.os.Parcelable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by freak on 01.08.2017.
 */

public class RssItem implements Parcelable {
    private final static String TAG = RssDownloaderService.class.getSimpleName();

    private String title;
    private String category;
    private String description;
    private String link;
    private String imageLink;

    public RssItem(String title, String category, String description, String link, String imageLink) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.link = link;
        this.imageLink = imageLink;
    }


    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getImageLink() {
        return imageLink;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Title:  ").append(getTitle())
                .append("\nCategory:  ").append(getCategory())
                .append("\nDescription:  ").append(getDescription())
                .append("\nLink:  ").append(getLink())
                .append("\nImageLink:  ").append(getImageLink()).append("\n\n\n");

        String result = builder.toString();
        return result;
    }

    public static ArrayList<RssItem> getRssItems(URL url) {
        ArrayList<RssItem> rssItems = new ArrayList<RssItem>();

        HttpURLConnection urlConnection = null;
        String responseJson = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(inputStream);

                Element element = document.getDocumentElement();
                NodeList nodeList = element.getElementsByTagName("item");
                if (nodeList.getLength() > 0) {
                    for (int i = 0; i < nodeList.getLength(); i++) {

                        //take each entry (corresponds to <item></item> tags in
                        //xml data

                        Element entry = (Element) nodeList.item(i);
                        NodeList imagesLinks = entry.getElementsByTagName("media:thumbnail");
                        Element _imageLinkE = (Element) imagesLinks.item(5);

                        Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
                        Element _linkE = (Element) entry.getElementsByTagName("link").item(0);
                        Element _descriptionE = (Element) entry.getElementsByTagName("description").item(0);
                        Element _categoryE = (Element) entry.getElementsByTagName("category").item(0);

                        String _imageLink = _imageLinkE.getAttribute("url");
                        String _title = _titleE.getFirstChild().getNodeValue();
                        String _link = _linkE.getFirstChild().getNodeValue();
                        String _description = _descriptionE.getFirstChild().getNodeValue();
                        String _category = _categoryE.getFirstChild().getNodeValue();


                        //create RssItemObject and add it to the ArrayList
                        RssItem rssItem = new RssItem(_title, _category,
                                _description, _link, _imageLink);

                        rssItems.add(rssItem);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return rssItems;
    }


    protected RssItem(Parcel in) {
        title = in.readString();
        category = in.readString();
        description = in.readString();
        link = in.readString();
        imageLink = in.readString();
    }

    public static final Creator<RssItem> CREATOR = new Creator<RssItem>() {
        @Override
        public RssItem createFromParcel(Parcel in) {
            return new RssItem(in);
        }

        @Override
        public RssItem[] newArray(int size) {
            return new RssItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeString(imageLink);
    }
}
