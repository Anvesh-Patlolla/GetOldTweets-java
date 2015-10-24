# Get Old Tweets Programatically
A project written in Java to get old tweets, it bypass some limitations of Twitter Official API.

## Details
Twitter Official API has the bother limitation of time constraints, you can't get older tweets than a week. Some tools provide access to older tweets but in the most of them you have to spend some money before.
I was searching other tools to do this job but I didn't found it, so after analyze how Twitter Search through browser works I understand its flow. Basically when you enter on Twitter page a scroll loader starts, if you scroll down you start to get more and more tweets, all through calls to a JSON provider. After mimic we get the best advantage of Twitter Search on browsers, it can search the deepest oldest tweets.

## Components
- **Tweet:** Model class to give some informations about a specific tweet (username, text, date, retweets, favorites)
- **TweetManager:** A manager class to help getting tweets in Tweet Model.
- **Main:** A simple class showing an example of use.

## Examples of use
- Get tweets by username
``` java
    TwitterCriteria criteria = TwitterCriteria.create()
				.setUsername("barackobama")
				.setMaxTweets(1);
    Tweet t = TweetManager.getTweets(criteria).get(0);
    System.out.println(t.getText());
```    
- Get tweets by query search
``` java
    TwitterCriteria criteria = TwitterCriteria.create()
				.setQuerySearch("europe refugees")
				.setMaxTweets(1);
    Tweet t = TweetManager.getTweets(criteria).get(0);
    System.out.println(t.getText());
```    
- Get tweets by username and bound dates
``` java
    TwitterCriteria criteria = TwitterCriteria.create()
				.setUsername("barackobama")
				.setSince("2015-09-10")
				.setUntil("2015-09-12")
				.setMaxTweets(1);
    Tweet t = TweetManager.getTweets(criteria).get(0);
    System.out.println(t.getText());
```    
