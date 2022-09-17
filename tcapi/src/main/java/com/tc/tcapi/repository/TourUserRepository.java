package com.tc.tcapi.repository;

import com.tc.tcapi.model.Tour;
import com.tc.tcapi.model.TourUser;
import com.tc.tcapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourUserRepository extends JpaRepository<TourUser, Long> {

    boolean existsByIdAndStatusAndTour_IdIsNotIn(Long id, int status, Collection<Long> ids);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM tv_db.tour_user tu\n" +
                    " JOIN user u ON u.id = tu.id\n" +
                    " WHERE tu.user_id = :userId AND tu.status =2;"
    )
    List<TourUser> getJoinedTours(@Param("userId") Long userId);

    boolean existsByIdAndStatus(Long id, int status);

    List<TourUser> findByTour_IdAndTour_UserOrderByStatusDescCreateDateDesc(Long id, User user);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM tour_user tu JOIN tour t ON t.id = tu.tour_id\n" +
                    "WHERE tour_id = :tourId AND tu.user_id <> :currentUserId AND tu.status <4\n" +
                    "ORDER BY tu.update_date, tu.create_date DESC"
    )
    List<TourUser> getTourUsersNative(@Param("currentUserId") Long currentUserId, @Param("tourId") Long tourId);

    int countByTourAndStatus(Tour tour, int status);

    Optional<TourUser> findByTour_UserAndId(User currentUser, Long tourUserId);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM tour_user tu JOIN tour t ON t.id = tu.tour_id\n" +
                    "WHERE tour_id = :tourId AND tu.status = 2 AND tu.user_id <> :id"
    )
    List<TourUser> getApprovedUserInTour(@Param("tourId") Long tourId, @Param("id") Long currentUserId);

    boolean existsByUser_IdAndTour_IdAndStatus(Long userId, Long tourId, int i);

    boolean existsByUser_IdAndTour_IdAndStatus(Long id, Tour tour, int i);

    Optional<TourUser> findByTour_IdAndTour_User_Id(Long tourId, Long tourUserId);
}