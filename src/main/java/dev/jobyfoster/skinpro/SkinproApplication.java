package dev.jobyfoster.skinpro;

import dev.jobyfoster.skinpro.model.Role;
import dev.jobyfoster.skinpro.model.User;
import dev.jobyfoster.skinpro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SkinproApplication {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SkinproApplication.class, args);
	}

	public void run(String... args){
		User adminAccount = userRepository.findByRole(Role.ADMIN);
		if(adminAccount == null){
			User newUser = new User();
			newUser.setEmail("admin@skinpro.co");
			newUser.setUsername("admin");
			newUser.setRole(Role.ADMIN);
			newUser.setPoints(0);
			newUser.setPassword(new BCryptPasswordEncoder().encode("admin"));
		}
	}
}
