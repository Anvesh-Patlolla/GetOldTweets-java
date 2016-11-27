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
	private static boolean isGeoTagEnabled = false;
	private String outputFileName = "/Users/anvesh/Desktop/alda/output_mar_";

	public static void main(String[] args) {
		Driver driver = new Driver();
		if (args.length < 6) {
			System.out.println("usage : \n IPL 2016-03-01 2016-03-31 50000 false false");
			return;
		}
		String searchString = args[0];
		String startDate = args[1];
		String endDate = args[2];
		int maxTweets = Integer.parseInt(args[3]);
		boolean sentiment = args[4].equalsIgnoreCase("true") ? true : false;
		isGeoTagEnabled = args[5].equalsIgnoreCase("true") ? true : false;

		long start = System.currentTimeMillis();
		// driver.getTweets("IPL", "2016-03-01", "2016-03-31", 50000, false);
		driver.getTweets(searchString, startDate, endDate, maxTweets, sentiment);
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
			if (isGeoTagEnabled) {
				outputFileName += "geoEnabled_true_";

			} else {
				outputFileName += "geoEnabled_false_";
			}
			if (sentiment) {
				outputFileName += "sentiment_true.csv";
			} else {
				outputFileName += "sentiment_false.csv";
			}
			bw = new BufferedWriter(new FileWriter(outputFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(tweets.size());
		for (Tweet tweet : tweets) {
			if (!isGeoTagEnabled || tweet.getGeo() != null && tweet.getGeo().length() > 0) {
				// System.out.println(tweet.toString());
				try {
					String temp = null;
					if (isGeoTagEnabled) {
						temp = escapeChars("\"" + tweet.getText()) + "\"" + "," + getCordinates(tweet.getGeo());
					} else {
						temp = getCordinates(tweet.getGeo());
					}
					bw.write(temp);
					bw.newLine();
					//System.out.println(temp + "\n");
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
		text = text.replaceAll("\\P{Print}", "");
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
