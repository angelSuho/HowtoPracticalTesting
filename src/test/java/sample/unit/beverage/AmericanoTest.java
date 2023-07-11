package sample.unit.beverage;

import org.junit.jupiter.api.Test;
import sample.howtopracticaltesting.unit.beverage.Americano;

import static org.assertj.core.api.Assertions.assertThat;

class AmericanoTest {

    @Test
    void getName() {
        Americano americano = new Americano();
//        assertEquals("아메리카노", americano.getName());
        assertThat(americano.getName()).isEqualTo("아메리카노");
    }

    @Test
    void getPrice() {
        Americano americano = new Americano();
//        assertEquals(4000, americano.getPrice());
        assertThat(americano.getPrice()).isEqualTo(4000);
    }
}