package Optiver;

import java.util.List;
import java.util.UUID;

class Order {
    String id;
    double amount;
    double margin;

    public Order(double amount, double margin){
        this.id = UUID.randomUUID().toString().substring(0,8);
        this.amount = amount;
        this.margin = margin;
    }
}

interface ProfitableStrategy{
    boolean isProfitable(Order order);
}

interface DisplayService{
    void display(Order order);
}

public class LeaderboardService {
    private final ProfitableStrategy strategy;
    private final DisplayService displayService;

    public LeaderboardService(ProfitableStrategy strategy, DisplayService displayService){
        this.strategy = strategy;
        this.displayService = displayService;
    }

    public void processStream(List<Order> orders){
        orders.stream()
                .filter(strategy::isProfitable)
                .forEach(o -> {
                    System.out.println(o);
                    displayService.display(o);
                });
    }
}
