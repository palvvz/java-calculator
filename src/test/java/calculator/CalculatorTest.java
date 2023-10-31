package calculator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CalculatorTest {

    @Test
    void whenOnePlusTwo() throws Exception {
        String result = Calculator.calc("1 + 2");
        assertThat(result).isEqualTo("3");
    }

    @Test
    void whenVIDivideIII() throws Exception {
        String result = Calculator.calc(" VI / III ");
        assertThat(result).isEqualTo("II");
    }

    @Test
    public void whenResultRomanIsNegative() throws Exception {
        String input = " I - II ";
        assertThatThrownBy(() -> Calculator.calc(input)).isInstanceOf(Exception.class)
                .hasMessageContaining("negative");
    }

    @Test
    public void whenRomanAndArabic() throws Exception {
        String input = "I + 1 ";
        Throwable e = null;
        assertThatThrownBy(() -> Calculator.calc(input)).isInstanceOf(Exception.class)
                .hasMessageContaining("Invalid input");
    }

    @Test
    public void whenOne() throws Exception {
        String input = "1";
        assertThatThrownBy(() -> Calculator.calc(input)).isInstanceOf(Exception.class)
                .hasMessageContaining("Invalid input");
    }

    @Test
    public void whenOnePlusTwoPlusThree() throws Exception {
        String input = "1 + 2 + 3";
        assertThatThrownBy(() -> Calculator.calc(input)).isInstanceOf(Exception.class)
                .hasMessageContaining("Invalid input");
    }
}