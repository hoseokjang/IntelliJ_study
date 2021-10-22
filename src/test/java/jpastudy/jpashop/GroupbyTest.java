package jpastudy.jpashop;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class GroupbyTest {
    @Test
    public void groupby()
    {
        List<Dish> dishList = Arrays.asList(
                new Dish("pork", 700, Type.MEAT)
                , new Dish("spaggeti", 550, Type.NOODLE)
                , new Dish("tomata", 200, Type.VEGE)
                , new Dish("onion", 150, Type.VEGE)
        );
        // Dish의 이름만 출력 (List로 출력)
        List<String> namelist = dishList.stream().map(Dish::getName).collect(toList());
        namelist.forEach(dishname -> System.out.println(dishname));
        System.out.println("==============================");
        // Dish의 이름을 구분자를 포함한 문자열로 출력
        String namestr = dishList.stream().map(Dish::getName).collect(joining(","));
        System.out.println(namestr);
    }

    static class Dish
    {
        String name;
        int calory;
        Type type;
        // alt + insert -> constructor로 생성자 만듬
        public Dish(String name, int calory, Type type) {
            this.name = name;
            this.calory = calory;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public int getCalory() {
            return calory;
        }

        public Type getType() {
            return type;
        }
    }
    enum Type
    {
        MEAT, FISH, NOODLE, VEGE
    }
}
