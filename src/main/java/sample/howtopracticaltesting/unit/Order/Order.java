package sample.howtopracticaltesting.unit.Order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sample.howtopracticaltesting.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Order {

    private final LocalDateTime orderDateTime;
    private final List<Beverage> beverages;
}
