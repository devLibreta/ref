package com.example.ref.optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;

@SpringBootTest
public class OptionalTest {

    @Test
    public void optionalTest(){

            System.out.println("1 ======================================================");
            System.out.println("Optional 객체 생성");

        // 1. 옵셔널 객체 생성방법 3가지

            // .of : null이 아닌 값을 가진 옵셔널 객체를 반환한다.
            Optional<String> optionalOf = Optional.of("optionalString"); // Optional[optional]

            // .ofNullable : null이면 옵셔널 empty 객체, 값이 있으면 값이 들어간 옵셔널 객체
            Optional<String> optionalNullableWithNull = Optional.ofNullable(null); // Optional.empty
            Optional<Integer> optionalNullableWithValue = Optional.ofNullable(1); // Optional[1]

            // .empty :  값이 null인 옵셔널 객체를 생성한다.
            Optional<String> optionalEmpty = Optional.empty(); // Optional.empty

            System.out.println("2 ======================================================");

        // 2. 값이 들어간 옵셔널 객체를 리턴하여 메소드 체이닝을 이어가는 메소드

            // .filter
            // 넘어온 옵셔널 객체의 값이 있고, 람다식 결과가 true이면 넘어온 객체를 그대로 리턴
            // 객체의 값이 없거나, 람다식 결과가 false 이면 옵셔널 empty 객체를 리턴
            // 람다식이 null 이면 NPE를 던진다.
            System.out.println(
                    optionalOf.filter(e -> e.startsWith("optional") )
            ); // Optional[optionalString]

            // .map
            // 넘어온 옵셔널 객체의 값이 있으면, 람다식 결과가 옵셔널 객체를 리턴한다.
            // 람다식 결과 값이 null이면 empty객체
            // 람다식이 Null 이면 NPE를 던진다.
            System.out.println(
                    optionalOf.map(String::toUpperCase)
            ); // Optional[OPTIONALSTRING]

            // .flatMap
            // 옵셔널 객체의 값이 옵셔널 객체일 경우에 사용하는 map
            // ** 결국 map이나 flatMap이나 옵셔널의 타입과 값을 바꾸기 위해 사용한다.
            System.out.println(
                    // Optional<String>을 Optional<Integer>로 변경함.
                optionalOf.flatMap((e)-> Optional.of(e.length()))
            ); // Optional[14]

            System.out.println("3 ======================================================");

        // 3. 옵셔널 객체 안의 값을 리턴하여 메소드 체이닝을 끝내는 메소드

            // .get : 옵셔널에 값이 있다면 그 값, null 이면 예외를 던진다.
            System.out.println("get() : "+ optionalOf.get()); // optionalString

            // .isPresent : 옵셔널에 값이 있다면 true 없으면 false
            System.out.println("isPresent() : "+ optionalOf.isPresent()); // true

            // .ifPresent : 옵셔널 값이 있다면 함수(값을 넣어줌)를 실행하고 값을 리턴한다. 없으면 아무것도 하지않는다.
            optionalOf.ifPresent((e)->{
                System.out.println("ifPresent() : "+ e); // optionalString
            }); // lambda ver
            optionalNullableWithNull.ifPresent(
                    System.out::println // 아무것도 하지않음
            ); // method reference ver

            // .orElse
            // 옵셔널 객체에 값이 없다면 other(param 값)을 리턴한다.
            // 옵셔널 객체에 값이 있다면 그 값을 리턴.
            System.out.println("orElse() : "+
                optionalNullableWithNull.orElse("null 옵셔널 객체")
            ); // null 옵셔널 객체

            // .orElseGet
            // 옵셔널 객체에 값이 없다면 other(param 값 또는 내부함수 결과 값)을 리턴한다.
            // 옵셔널 객체에 값이 있다면 그 값을 리턴.
            System.out.println("orElseGet() : "+
                optionalNullableWithNull.orElseGet(()->"defaultValue")
            ); // defaultValue

            // .orElseThrow
            // 옵셔널 객체에 값이 있다면 그 값을 리턴.
            // 객체 값이 null인 경우에는 내부 함수를 실행하여 예외를 던진다.
            System.out.println(
                optionalNullableWithNull.orElseThrow(NoSuchElementException::new)
            );

        // 4. Java9부터 추가된 기능.
            // .or
            // 옵셔널 객체에 값이 있다면 그 값이 든 옵셔널 객체
            // 객체 값이 null이면 내부 함수 결과 옵셔널 객체를 리턴한다.

            // .ifPresentOrElse
            // 객체에 값이 있을경우 first param 람다식을 실행
            // 값이 없을경우 seconde param 람다식을 실행.
            // .ifPresentOrElse(()->{}, ()->{});

            // .stream
            // 옵셔널 객체를 스트림으로 변환
            // .flatMap(Optional::stream)
            System.out.println("끝 ======================================================");

    }
}
