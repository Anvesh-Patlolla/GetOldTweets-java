package me.jhenrique.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import me.jhenrique.manager.TweetManager;
import me.jhenrique.manager.TwitterCriteria;
import me.jhenrique.model.Tweet;

public class Driver {
	public static void main(String[] args) {
		Driver driver = new Driver();
		long start = System.currentTimeMillis();
		driver.getTweets("VIVOIPL", "2016-04-01", "2016-04-15", 5000);
		long end = System.currentTimeMillis();
		System.out.println("time:" + (start - end));
	}

	void getTweets(String searchString, String startDate, String endDate, int maxTweets) {
		TwitterCriteria criteria = null;
		criteria = TwitterCriteria.create().setQuerySearch(searchString).setSince(startDate).setUntil(endDate)
				.setMaxTweets(maxTweets).setPositiveTweet(false);

		List<Tweet> tweets = TweetManager.getTweets(criteria);
		int count = 0;
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("/Users/anvesh/Desktop/alda/output_got_false.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
		System.out.println(tweets.size());
		for (Tweet tweet : tweets) {
			if (true || tweet.getGeo() != null && tweet.getGeo().length() > 0) {
				//System.out.println(tweet.toString());
				try {
					bw.write(tweet.toString());
					bw.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				count++;
			}
		}
		System.out.println("Count:" + count);
	}
}
