package com.example.usermanagement.repository;

import com.example.usermanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

       boolean existsByName(String name);

       @Query("SELECT u FROM User u WHERE " +
                     "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
                     "(:city IS NULL OR LOWER(u.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
                     "(:job IS NULL OR LOWER(u.job) LIKE LOWER(CONCAT('%', :job, '%'))) AND " +
                     "(:age IS NULL OR u.age = :age)")
       Page<User> findUsersWithFilters(@Param("name") String name,
                     @Param("city") String city,
                     @Param("job") String job,
                     @Param("age") Integer age,
                     Pageable pageable);
}