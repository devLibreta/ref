package com.example.ref.tobi.template_callback;

public interface LineCallback<T> {
	T doSomethingWithLine(String line, T value);
}
