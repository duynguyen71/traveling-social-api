package com.tc.tcapi.repository;

import com.tc.tcapi.model.Tour;
import com.tc.tcapi.model.TourUser;
import com.tc.tcapi.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    @Query(
            nativeQuery = true,
            value = "select distinct* from tour t join tour_user tu\n" +
                    "on t.id = tu.tour_id\n" +
                    "where (t.user_id = :userId\n" +
                    "or (tu.user_id = :userId AND tu.status = 2)) \n" +
                    "order by t.create_date desc, tu.update_date desc"
    )
    List<Tour> getCurrentJoinTour(@Param("userId") Long userId);


    List<Tour> findByUserOrTourUsers_UserAndTourUsers_StatusOrderByCreateDateDescTourUsers_UpdateDateDesc(User user, User user3, int status);

//    List<Tour> findByUserOrTourUsers_UserAndTourUsers_StatusOrderByCreateDateAndTourUsers_UpdateDateDesc(User user, int status);


    List<Tour> findByTourUsers_UserAndStatus(User tourUsers, @Nullable int status, Pageable pageable);

    List<Tour> findByUserAndStatus(User user, int status, Pageable pageable);

    boolean existsByIdAndUser(Long id, User user);

    Optional<Tour> findByIdAndUser(Long id, User currentUser);

    Optional<Tour> findByUserAndTourUsers_Id(User user, Long tourUserId);

}