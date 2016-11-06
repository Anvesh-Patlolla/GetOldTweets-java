package me.jhenrique.main;

import java.util.List;

import me.jhenrique.manager.TweetManager;
import me.jhenrique.manager.TwitterCriteria;
import me.jhenrique.model.Tweet;

public class Driver {
	public static void main(String[] args) {
		Driver driver = new Driver();
		driver.getTweets("VIVOIPL", "2016-04-01", "2016-04-30", 5000);
	}

	void getTweets(String searchString, String startDate, String endDate, int maxTweets) {
		TwitterCriteria criteria = null;
		criteria = TwitterCriteria.create().setQuerySearch(searchString).setSince(startDate).setUntil(endDate)
				.setMaxTweets(maxTweets);
		List<Tweet> tweets = TweetManager.getTweets(criteria);
		for (Tweet tweet : tweets) {
			System.out.println(tweet.toString());

		}
	}
}
