package hello;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2/12/2018.
 */
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
    List<Customer> findByLastNameContainingIgnoreCase(String lastNameFragment);
}

