package com.example.ref.tobi.template_callback;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest
public class CalcSumTest {
	Calculator calculator;
	String numFilepath;

	@BeforeEach
	public void setUp() throws IOException {
		this.calculator = new Calculator();
		ClassPathResource resource = new ClassPathResource("static/txt/numbers.txt");
		this.numFilepath = String.valueOf(resource.getFile());

		// ClassPathResource 메소드
//		System.out.println(this.numFilepath); // static/txt/numbers.txt
//		System.out.println(resource.getClassLoader()); // sun.misc.Launcher$AppClassLoader@2a139a55
//		System.out.println(resource.getDescription()); // class path resource [static/txt/numbers.txt]
//		System.out.println(resource.getPath()); // static/txt/numbers.txt
//		System.out.println(resource.getURL()); // file:/C:/dev/project/ref/build/resources/main/static/txt/numbers.txt
//		System.out.println(resource.getFile()); // C:\dev\project\ref\build\resources\main\static\txt\numbers.txt
	}
	// assertThat 메소드는 첫번째 파라미터와 두번쨰 파라미터 로직과 비교후
	// 로직에 맞다면 끝나고 틀리면 테스트 실패 상태를 반환한다.
	@Test
	public void sumOfNumbers() throws IOException {
		MatcherAssert.assertThat(calculator.calcSum(this.numFilepath), is(10));
	}
	
	@Test public void multiplyOfNumbers() throws IOException {
		assertThat(calculator.calcMultiply(this.numFilepath), is(24));
	}
	
	@Test public void concatenateStrings() throws IOException {
		assertThat(calculator.concatenate(this.numFilepath), is("1234"));
	}

	// =========================================================================================
	//Junit5 기본 어노테이션 from spring 2.1
	@BeforeAll
	@AfterAll
	public static void beforeAllMethod(){
		// 모든 테스트 실행 전 한번 실행. private 불가, 리턴값 불가, 반드시 static void.
		// AfterAll 은 모든 테스트 종료 후 한번 실행. 이하 동일.
	}
	@BeforeEach
	@AfterEach
	public void beforeEachMethod(){
		// 각각의 테스트를 실행할 때 실행 이전 한번 호출.
		// after 는 실행 이후 한번 호출.
	}
	@Disabled
	public void disabledMethod(){
		// 해당 테스트는 실행되지 않는다.
	}

	// =========================================================================================

}

