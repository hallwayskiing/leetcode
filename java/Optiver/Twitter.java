package Optiver;

import java.util.*;

/**
 * Design a simplified version of Twitter where users can post tweets, follow/unfollow another user,
 * and is able to see the 10 most recent tweets in the user's news feed.
 * <p>
 * Implement the Twitter class:
 * <p>
 * Twitter() Initializes your twitter object.
 * void postTweet(int userId, int tweetId) Composes a new tweet with ID tweetId by the user userId.
 * Each call to this function will be made with a unique tweetId.
 * List<Integer> getNewsFeed(int userId) Retrieves the 10 most recent tweet IDs in the user's news feed.
 * Each item in the news feed must be posted by users who the user followed or by the user themself.
 * Tweets must be ordered from the most recent to the least recent.
 * void follow(int followerId, int followeeId) The user with ID followerId started following the user with ID followeeId.
 * void unfollow(int followerId, int followeeId) The user with ID followerId started unfollowing the user with ID followeeId.
 */
public class Twitter {

    static class Tweet {
        int id;
        int time;
        Tweet next;

        public Tweet(int tweetId, int time) {
            this.id = tweetId;
            this.time = time;
        }
    }

    static class User {
        Set<Integer> followees;
        Tweet latest;

        public User() {
            followees = new HashSet<>();
        }

        public void post(Tweet tweet) {
            tweet.next = latest;
            latest = tweet;
        }

        public void follow(int followeeId) {
            followees.add(followeeId);
        }

        public void unfollow(int followeeId) {
            followees.remove(followeeId);
        }
    }

    private final Map<Integer, User> users;
    private int timestamp;

    public Twitter() {
        users = new HashMap<>();
        timestamp = 0;
    }

    public void postTweet(int userId, int tweetId) {
        Tweet newTweet = new Tweet(tweetId, timestamp++);
        users.computeIfAbsent(userId, k -> new User()).post(newTweet);
    }

    public List<Integer> getNewsFeed(int userId) {
        PriorityQueue<Tweet> pq = new PriorityQueue<>((a, b) -> b.time - a.time);

        User user = users.get(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        if (user.latest != null) {
            pq.add(user.latest);
        }

        Set<Integer> followees = user.followees;
        for (int followeeId : followees) {
            User followee = users.get(followeeId);
            if (followee != null && followee.latest != null) {
                pq.offer(followee.latest);
            }
        }


        List<Integer> res = new ArrayList<>();
        while (!pq.isEmpty() && res.size() < 10) {
            Tweet curr = pq.poll();
            res.add(curr.id);
            if (curr.next != null) {
                pq.offer(curr.next);
            }
        }

        return res;
    }

    public void follow(int followerId, int followeeId) {
        if (followerId == followeeId) return;
        users.computeIfAbsent(followerId, k -> new User()).follow(followeeId);
    }

    public void unfollow(int followerId, int followeeId) {
        User user = users.get(followerId);
        if (user != null) {
            user.unfollow(followeeId);
        }
    }
}
