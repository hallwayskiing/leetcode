package Optiver;

import java.util.*;

public class OrderMatcher {
    public enum Side {
        SELL,
        BUY
    }

    public static class Order {
        int id;
        Side side;
        long price;
        long quantity;

        public Order(int id, Side side, long price, long quantity) {
            this.id = id;
            this.side = side;
            this.price = price;
            this.quantity = quantity;
        }
    }

    public static class OrderNode {
        Order order;
        OrderNode prev;
        OrderNode next;

        public OrderNode() {
        }

        public OrderNode(Order order) {
            this.order = order;
        }
    }

    public static class PriceLevel {
        OrderNode head;
        OrderNode tail;
        long totalQuantity;
        int size;

        PriceLevel() {
            head = new OrderNode();
            tail = new OrderNode();
            head.next = tail;
            tail.prev = head;
            totalQuantity = 0;
        }

        public OrderNode offer(Order order) {
            OrderNode node = new OrderNode(order);
            node.prev = tail.prev;
            node.next = tail;
            tail.prev.next = node;
            tail.prev = node;
            totalQuantity += order.quantity;
            size++;
            return node;
        }

        public Order peek() {
            return head.next.order;
        }

        public Order poll() {
            OrderNode node = head.next;
            remove(node);
            return node.order;
        }

        public void remove(OrderNode orderNode) {
            orderNode.prev.next = orderNode.next;
            orderNode.next.prev = orderNode.prev;
            orderNode.next = orderNode.prev = null;
            totalQuantity -= orderNode.order.quantity;
            size--;
        }

        public boolean isEmpty() {
            return size == 0;
        }
    }

    private final TreeMap<Long, PriceLevel> buyBook = new TreeMap<>(Collections.reverseOrder());
    private final TreeMap<Long, PriceLevel> sellBook = new TreeMap<>();
    private final Map<Integer, OrderNode> activeOrders = new HashMap<>();

    public double match(Order order) {
        long initialAmount = order.quantity;
        long totalSpent = 0;

        TreeMap<Long, PriceLevel> opponentBook = (order.side == Side.BUY) ? sellBook : buyBook;
        TreeMap<Long, PriceLevel> ownBook = (order.side == Side.BUY) ? buyBook : sellBook;

        while (order.quantity > 0 && !opponentBook.isEmpty()) {
            long bestOpponentPrice = opponentBook.firstKey();

            // Cannot find any match
            if ((order.side == Side.BUY && order.price < bestOpponentPrice) ||
                    (order.side == Side.SELL && order.price > bestOpponentPrice)) {
                break;
            }

            PriceLevel priceLevel = opponentBook.get(bestOpponentPrice);
            while (!priceLevel.isEmpty() && order.quantity > 0) {
                Order matched = priceLevel.peek();
                long tradedAmount = Math.min(order.quantity, matched.quantity);

                order.quantity -= tradedAmount;
                matched.quantity -= tradedAmount;
                priceLevel.totalQuantity -= tradedAmount;
                totalSpent += tradedAmount * bestOpponentPrice;

                if (matched.quantity == 0) {
                    priceLevel.poll();
                    activeOrders.remove(matched.id);
                }
            }

            if (priceLevel.isEmpty()) {
                opponentBook.remove(bestOpponentPrice);
            }
        }

        // If not entirely matched, add to the book
        if (order.quantity > 0) {
            OrderNode node = ownBook.computeIfAbsent(order.price, k -> new PriceLevel()).offer(order);
            activeOrders.put(order.id, node);
        }

        long executedAmount = initialAmount - order.quantity;
        return executedAmount == 0 ? 0 : (double) totalSpent / executedAmount;
    }

    public boolean cancel(int orderId) {
        OrderNode node = activeOrders.remove(orderId);
        if (node == null) return false;

        Order order = node.order;
        TreeMap<Long, PriceLevel> ownBook = order.side == Side.BUY ? buyBook : sellBook;
        PriceLevel priceLevel = ownBook.get(order.price);

        priceLevel.remove(node);
        if (priceLevel.isEmpty()) {
            ownBook.remove(order.price);
        }

        return true;
    }

    public boolean modify(Order order) {
        OrderNode oldNode = activeOrders.get(order.id);
        if (oldNode == null) return false;

        cancel(order.id);
        match(order);

        return true;
    }

    public static void main(String[] args) {
        testNoMatch();
        testFullMatch();
        testMatchAcrossLevels();
        testPartialMatch();
        testOverMatch();
        testFifoAtSamePriceLevel();
        testCancelExistingOrder();
        testCancelNonExistingOrder();
        testModifyExistingOrder();
        testModifyNonExistingOrder();
        testModifyOrderRerank();

        System.out.println("All tests passed.");
    }

    private static void testNoMatch() {
        OrderMatcher matcher = new OrderMatcher();

        Order sell = new Order(1, Side.SELL, 105, 10);
        double avgPrice = matcher.match(sell);

        assert avgPrice == 0.0;

        Order buy = new Order(2, Side.BUY, 104, 10);
        double secondAvgPrice = matcher.match(buy);

        assert secondAvgPrice == 0.0;
        assert matcher.cancel(1);
        assert matcher.cancel(2);
    }

    private static void testFullMatch() {
        OrderMatcher matcher = new OrderMatcher();

        matcher.match(new Order(1, Side.SELL, 100, 10));

        Order buy = new Order(2, Side.BUY, 100, 10);
        double avgPrice = matcher.match(buy);

        assert avgPrice == 100.0;
        assert buy.quantity == 0L;
        assert !matcher.cancel(1);
        assert !matcher.cancel(2);
    }

    private static void testPartialMatch() {
        OrderMatcher matcher = new OrderMatcher();

        Order sell = new Order(1, Side.SELL, 100, 10);
        matcher.match(sell);

        Order buy = new Order(2, Side.BUY, 100, 5);
        double avgPrice = matcher.match(buy);

        assert avgPrice == 100.0;
        assert sell.quantity == 5L;
        assert buy.quantity == 0L;
        assert matcher.cancel(1);
        assert !matcher.cancel(2);
    }

    private static void testOverMatch() {
        OrderMatcher matcher = new OrderMatcher();

        Order sell = new Order(1, Side.SELL, 100, 5);
        matcher.match(sell);

        Order buy = new Order(2, Side.BUY, 100, 10);
        double avgPrice = matcher.match(buy);

        assert avgPrice == 100.0;
        assert sell.quantity == 0L;
        assert buy.quantity == 5L;
        assert !matcher.cancel(1);
        assert matcher.cancel(2);
    }

    private static void testMatchAcrossLevels() {
        OrderMatcher matcher = new OrderMatcher();

        matcher.match(new Order(1, Side.SELL, 100, 5));
        matcher.match(new Order(2, Side.SELL, 101, 5));
        matcher.match(new Order(3, Side.SELL, 105, 5));

        Order buy = new Order(4, Side.BUY, 102, 20);
        double avgPrice = matcher.match(buy);

        assert avgPrice == 100.5;
        assert buy.quantity == 10L;
        assert !matcher.cancel(1);
        assert !matcher.cancel(2);
        assert matcher.cancel(3);
        assert matcher.cancel(4);
    }

    private static void testFifoAtSamePriceLevel() {
        OrderMatcher matcher = new OrderMatcher();

        matcher.match(new Order(1, Side.SELL, 100, 5));
        Order sell2 = new Order(2, Side.SELL, 100, 5);
        matcher.match(sell2);

        Order buy = new Order(3, Side.BUY, 100, 7);
        double avgPrice = matcher.match(buy);

        assert avgPrice == 100.0;
        assert buy.quantity == 0L;
        assert !matcher.cancel(1);
        assert sell2.quantity == 3;
        assert matcher.cancel(2);
    }

    private static void testCancelExistingOrder() {
        OrderMatcher matcher = new OrderMatcher();

        matcher.match(new Order(1, Side.BUY, 99, 10));

        assert matcher.cancel(1);
        assert !matcher.cancel(1);

        Order sell = new Order(2, Side.SELL, 99, 10);
        double avgPrice = matcher.match(sell);

        assert avgPrice == 0.0;
        assert matcher.cancel(2);
    }

    private static void testCancelNonExistingOrder() {
        OrderMatcher matcher = new OrderMatcher();

        assert !matcher.cancel(999);
    }

    private static void testModifyExistingOrder() {
        OrderMatcher matcher = new OrderMatcher();

        matcher.match(new Order(1, Side.BUY, 99, 10));
        matcher.match(new Order(2, Side.SELL, 100, 10));

        boolean modified = matcher.modify(new Order(1, Side.BUY, 100, 10));

        assert modified;
        assert !matcher.cancel(1);
        assert !matcher.cancel(2);
    }

    private static void testModifyNonExistingOrder() {
        OrderMatcher matcher = new OrderMatcher();

        assert !matcher.modify(new Order(100, Side.BUY, 100, 10));
    }

    private static void testModifyOrderRerank() {
        OrderMatcher matcher = new OrderMatcher();

        matcher.match(new Order(1, Side.BUY, 100, 5));
        matcher.match(new Order(2, Side.BUY, 100, 5));

        assert matcher.modify(new Order(1, Side.BUY, 100, 5));

        Order sell = new Order(3, Side.SELL, 100, 7);
        double avgPrice = matcher.match(sell);

        assert avgPrice == 100.0;
        assert sell.quantity == 0L;
        assert !matcher.cancel(2);
        assert matcher.cancel(1);
    }
}