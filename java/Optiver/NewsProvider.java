package Optiver;

import java.util.*;

/**
 * Eligibility: deliver if news.interest ≥ subscriber.minInterest.
 * Topic filter: deliver if news.topics ∩ subscriber.topics ≠ ∅.
 * Freshness: nowTimestamp − maxAge ≤ news.timestamp ≤ nowTimestamp.
 * Rate limiting: per-subscriber sliding window allowing at most maxNewsPerSecond items in any rolling 1-second window.
 * De-duplication: a given newsId must not be delivered to the same subscriber more than once.
 * Ordering: higher interest first; if ties, older timestamp first; if still tied, smaller newsId first.
 */
public class NewsProvider {

    public record Subscriber(
            int id,
            float interest,
            int maxNewsPerSecond,
            List<String> topics
    ) {
    }

    public record News(
            int newsId,
            long timestamp,
            float interest,
            List<String> topics
    ) {
    }

    private final Map<Integer, Subscriber> subscribers = new HashMap<>();
    private final Map<Integer, News> newsMap = new HashMap<>();

    private final Map<Integer, Set<Integer>> deliveredNews = new HashMap<>();
    private final Map<Integer, Deque<Long>> deliveryHistory = new HashMap<>();

    /**
     * Create or update a subscriber’s configuration.
     * Return true on success; false on invalid input or violated constraints.
     */
    public boolean addSubscription(int subscriberId, float minInterest, int maxNewsPerSecond, List<String> topics) {
        subscribers.put(subscriberId, new Subscriber(subscriberId, minInterest, maxNewsPerSecond, topics));
        deliveredNews.computeIfAbsent(subscriberId, k -> new HashSet<>());
        deliveryHistory.computeIfAbsent(subscriberId, k -> new ArrayDeque<>());
        return true;
    }

    /**
     * Remove a subscriber; return false if subscriberId does not exist.
     */
    public boolean removeSubscription(int subscriberId) {
        if(!subscribers.containsKey(subscriberId)){
            return false;
        }
        subscribers.remove(subscriberId);
        deliveredNews.remove(subscriberId);
        deliveryHistory.remove(subscriberId);
        return true;
    }

    /**
     * Ingest a news item with its timestamp, interest score, and topics.
     * Return false if newsId already exists; otherwise true.
     */
    public boolean newsReceived(int newsId, long timestamp, float interest, List<String> topics) {
        if(newsMap.containsKey(newsId)){
            return false;
        }
        newsMap.put(newsId, new News(newsId, timestamp, interest, topics));
        return true;
    }

    /**
     * For each subscriber, return the list of newsIds to deliver at nowTimestamp, subject to the rules below.
     */
    public Map<Integer, List<Integer>> publish(long nowTimestamp, long maxAge) {
        Map<Integer, List<Integer>> res = new HashMap<>();

        for(Subscriber subscriber:subscribers.values()){
            // 1. Clean sliding window
            Deque<Long> history = deliveryHistory.get(subscriber.id);
            while (!history.isEmpty() && history.peekFirst() <= nowTimestamp - 1000){
                history.pollFirst();
            }

            // 2. Compute quota
            int quota = subscriber.maxNewsPerSecond - history.size();
            if(quota <= 0){
                res.put(subscriber.id, new ArrayList<>());
                continue;
            }
            // 3. Filter
            Comparator<News> newsComparator = Comparator
                    .comparing((News n) -> n.interest).reversed()
                    .thenComparing(n -> n.timestamp)
                    .thenComparing(n -> n.newsId);
            List<Integer> newsList = newsMap.values().stream()
                    .filter(n -> n.interest >= subscriber.interest)
                    .filter(n -> !deliveredNews.get(subscriber.id).contains(n.newsId))
                    .filter(n -> n.timestamp >= nowTimestamp - maxAge && n.timestamp <= nowTimestamp)
                    .filter(n -> !Collections.disjoint(subscriber.topics, n.topics))
                    .sorted(newsComparator)
                    .limit(quota)
                    .map(n -> n.newsId)
                    .toList();
            // 4. Results
            deliveredNews.get(subscriber.id).addAll(newsList);
            history.addAll(Collections.nCopies(newsList.size(), nowTimestamp));
            res.put(subscriber.id, newsList);
        }

        return res;
    }

    public static void main(String[] args) {
        testAddAndRemoveSubscription();
        testDuplicateNewsRejected();
        testPublishNoSubscribers();
        testPublishBasicFiltering();
        testPublishOrdering();
        testPublishExpiredNews();
        testDeduplication();
        testRateLimitingWithinWindow();
        testRateLimitingAfterWindowExpires();
        testMultipleSubscribers();
        testRemovedSubscriberNotPublished();

        System.out.println("All tests passed.");
    }

    private static void testAddAndRemoveSubscription() {
        NewsProvider provider = new NewsProvider();

        assert provider.addSubscription(1, 0.5f, 2, List.of("sports", "tech"));
        assert provider.removeSubscription(1);
        assert !provider.removeSubscription(1);
    }

    private static void testDuplicateNewsRejected() {
        NewsProvider provider = new NewsProvider();

        assert provider.newsReceived(100, 1000L, 0.8f, List.of("tech"));
        assert !provider.newsReceived(100, 1001L, 0.9f, List.of("sports"));
    }

    private static void testPublishNoSubscribers() {
        NewsProvider provider = new NewsProvider();

        assert provider.newsReceived(1, 1000L, 0.8f, List.of("tech"));

        Map<Integer, List<Integer>> result = provider.publish(1000L, 1000L);

        assert result.isEmpty();
    }

    private static void testPublishBasicFiltering() {
        NewsProvider provider = new NewsProvider();

        provider.addSubscription(1, 0.7f, 10, List.of("tech", "finance"));

        assert provider.newsReceived(1, 900L, 0.8f, List.of("tech"));
        assert provider.newsReceived(2, 900L, 0.6f, List.of("tech"));
        assert provider.newsReceived(3, 900L, 0.9f, List.of("sports"));
        assert provider.newsReceived(4, -200L, 0.95f, List.of("finance"));
        assert provider.newsReceived(5, 950L, 0.75f, List.of("finance"));

        Map<Integer, List<Integer>> result = provider.publish(1000L, 1000L);

        assert result.get(1).equals(List.of(1, 5));
    }

    private static void testPublishOrdering() {
        NewsProvider provider = new NewsProvider();

        provider.addSubscription(1, 0.0f, 10, List.of("tech"));

        assert provider.newsReceived(10, 990L, 0.9f, List.of("tech"));
        assert provider.newsReceived(11, 980L, 0.9f, List.of("tech"));
        assert provider.newsReceived(9, 980L, 0.9f, List.of("tech"));
        assert provider.newsReceived(12, 995L, 1.0f, List.of("tech"));

        Map<Integer, List<Integer>> result = provider.publish(1000L, 1000L);

        assert result.get(1).equals(List.of(12, 9, 11, 10));
    }

    private static void testPublishExpiredNews(){
        NewsProvider provider = new NewsProvider();

        provider.addSubscription(1, 0.0f, 10, List.of("tech"));

        assert provider.newsReceived(10, 200L, 0.9f, List.of("tech"));

        Map<Integer, List<Integer>> result = provider.publish(1000L, 500L);

        assert result.get(1).isEmpty();
    }

    private static void testDeduplication() {
        NewsProvider provider = new NewsProvider();

        provider.addSubscription(1, 0.0f, 10, List.of("tech"));

        assert provider.newsReceived(1, 1000L, 0.8f, List.of("tech"));
        assert provider.newsReceived(2, 1000L, 0.7f, List.of("tech"));

        Map<Integer, List<Integer>> first = provider.publish(1000L, 1000L);
        Map<Integer, List<Integer>> second = provider.publish(1000L, 1000L);

        assert first.get(1).equals(List.of(1, 2));
        assert second.get(1).isEmpty();
    }

    private static void testRateLimitingWithinWindow() {
        NewsProvider provider = new NewsProvider();

        provider.addSubscription(1, 0.0f, 2, List.of("tech"));
        assert provider.newsReceived(1, 1000L, 0.9f, List.of("tech"));
        assert provider.newsReceived(2, 1000L, 0.8f, List.of("tech"));
        assert provider.newsReceived(3, 1000L, 0.7f, List.of("tech"));

        Map<Integer, List<Integer>> first = provider.publish(1000L, 1000L);
        Map<Integer, List<Integer>> second = provider.publish(1500L, 1000L);

        assert first.get(1).equals(List.of(1, 2));
        assert second.get(1).isEmpty();
    }

    private static void testRateLimitingAfterWindowExpires() {
        NewsProvider provider = new NewsProvider();

        provider.addSubscription(1, 0.0f, 2, List.of("tech"));
        assert provider.newsReceived(1, 1000L, 0.9f, List.of("tech"));
        assert provider.newsReceived(2, 1000L, 0.8f, List.of("tech"));
        assert provider.newsReceived(3, 1000L, 0.7f, List.of("tech"));

        Map<Integer, List<Integer>> first = provider.publish(1000L, 1000L);
        Map<Integer, List<Integer>> second = provider.publish(2000L, 1000L);

        assert first.get(1).equals(List.of(1, 2));
        assert second.get(1).equals(List.of(3));
    }

    private static void testMultipleSubscribers() {
        NewsProvider provider = new NewsProvider();

        provider.addSubscription(1, 0.5f, 10, List.of("tech"));
        provider.addSubscription(2, 0.85f, 10, List.of("sports", "finance"));

        assert provider.newsReceived(1, 1000L, 0.9f, List.of("tech"));
        assert provider.newsReceived(2, 1000L, 0.8f, List.of("sports"));
        assert provider.newsReceived(3, 1000L, 0.95f, List.of("finance"));
        assert provider.newsReceived(4, 1000L, 0.7f, List.of("tech", "finance"));

        Map<Integer, List<Integer>> result = provider.publish(1000L, 1000L);

        assert result.get(1).equals(List.of(1, 4));
        assert result.get(2).equals(List.of(3));
    }

    private static void testRemovedSubscriberNotPublished() {
        NewsProvider provider = new NewsProvider();

        provider.addSubscription(1, 0.0f, 10, List.of("tech"));
        provider.addSubscription(2, 0.0f, 10, List.of("tech"));
        assert provider.removeSubscription(1);

        assert provider.newsReceived(1, 1000L, 0.9f, List.of("tech"));

        Map<Integer, List<Integer>> result = provider.publish(1000L, 1000L);

        assert !result.containsKey(1);
        assert result.get(2).equals(List.of(1));
    }
}
