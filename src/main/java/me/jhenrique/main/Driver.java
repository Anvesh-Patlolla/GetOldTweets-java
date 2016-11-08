package me.jhenrique.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

import me.jhenrique.manager.TweetManager;
import me.jhenrique.manager.TwitterCriteria;
import me.jhenrique.model.Tweet;

public class Driver {
	private static Geocoder geocoder;

	public static void main(String[] args) {
		Driver driver = new Driver();
		long start = System.currentTimeMillis();
		driver.getTweets("IPL", "2016-04-01", "2016-05-31", 5000, true);
		long end = System.currentTimeMillis();
		System.out.println("time:" + (start - end));
	}

	void getTweets(String searchString, String startDate, String endDate, int maxTweets, boolean sentiment) {
		geocoder = new Geocoder();
		TwitterCriteria criteria = null;
		criteria = TwitterCriteria.create().setQuerySearch(searchString).setSince(startDate).setUntil(endDate)
				.setMaxTweets(maxTweets).setPositiveTweet(sentiment);

		List<Tweet> tweets = TweetManager.getTweets(criteria);
		int count = 0;
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("/Users/anvesh/Desktop/alda/output_true.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(tweets.size());
		for (Tweet tweet : tweets) {
			if (true || tweet.getGeo() != null && tweet.getGeo().length() > 0) {
				// System.out.println(tweet.toString());
				try {
					String temp = escapeChars("\"" + tweet.getText()) + "\"" + "," + getCordinates(tweet.getGeo());
					bw.write(temp);
					bw.newLine();
					System.out.println(temp + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}

				count++;
			}
		}
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Count:" + count);
	}

	private String escapeChars(String text) {
		// return text.replaceAll(",", "\\\\,");
		return text;
	}

	private String getCordinates(String geo) {
		String latLong = "";
		try {
			LatLng location = getLocation(geo);
			latLong = "\"" + location.getLat() + "\"" + "," + "\"" + location.getLng() + "\"";
		} catch (Exception e) {
			e.printStackTrace();
			latLong = "\"0\",\"0\"";
		}
		return latLong;
	}

	public LatLng getLocation(String place) throws IOException {
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(place).getGeocoderRequest();
		GeocodeResponse geocodeResponse = geocoder.geocode(geocoderRequest);

		List<GeocoderResult> results = geocodeResponse.getResults();

		return results.get(0).getGeometry().getLocation();

	}
}
