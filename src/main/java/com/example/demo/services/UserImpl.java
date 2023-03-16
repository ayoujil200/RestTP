package com.example.demo.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;

@Service
public class UserImpl implements GlobalService<User> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User getOne(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public User createOne(User user) {
		return userRepository.save(user);
	}

	@Override
	public boolean updateOne(User user) {
		try {
			User oldUser = userRepository.findById(user.getId()).get();

			if (oldUser == null) {
				return false;
			}

			oldUser.setFirstName(user.getFirstName());
			oldUser.setLastName(user.getLastName());
			oldUser.setEmail(user.getEmail());

			userRepository.save(oldUser);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteOne(Long id) {
		try {
			userRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
