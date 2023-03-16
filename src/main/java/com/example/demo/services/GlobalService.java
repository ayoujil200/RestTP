package com.example.demo.services;
import java.util.List;

public interface GlobalService<T> {
	public T getOne(Long id);

	public List<T> getAll();

	public T createOne(T o);

	public boolean updateOne(T o);

	public boolean deleteOne(Long id);
}
