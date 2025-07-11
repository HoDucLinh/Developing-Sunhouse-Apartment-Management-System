package linh.sunhouse_apartment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
		org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
})
public class SunhouseApartmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(SunhouseApartmentApplication.class, args);
	}

}
