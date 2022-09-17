package com.tc.tcapi.repository;

import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.Tag;
import com.tc.tcapi.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {


    @Query(value = "SELECT rv.*, (AVG(DISTINCT rpv.rating)) avgStart, COUNT(DISTINCT rpv.id) countView\n" +
            "FROM review_post rv JOIN review_post_visitor rpv\n" +
            "ON rv.id = rpv.review_post_id \n" +
            "GROUP BY rv.id\n" +
            "HAVING rv.status = 1\n" +
            "ORDER BY avgStart DESC, countView DESC \n" +
            "LIMIT :pageSize OFFSET :page", nativeQuery = true)
    List<ReviewPost> getPopularReviewPostsNative(@Param("pageSize") int limit, @Param("page") int offset);

    List<ReviewPost> findByStatus(int status,Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT (AVG(DISTINCT rpv.rating)) avgStart\n" +
                    "FROM review_post rv JOIN review_post_visitor rpv\n" +
                    "ON rv.id = rpv.review_post_id WHERE rv.id = :reviewPostId"
    )
    Double countRating(@Param("reviewPostId") Long id);

    List<ReviewPost> findByUserAndStatus(User user, Integer status);

    Optional<ReviewPost> findByIdAndUser(Long reqId, User user);

    Optional<ReviewPost> findByIdAndStatus(Long reviewId, int status);

    List<ReviewPost> findByTags(Tag tag);

    /**
     * @param keyword
     * @param pageable
     * @return
     */
    @Query(nativeQuery = true,
            value = "SELECT DISTINCT p.* FROM review_post p JOIN review_post_tag pt\n" +
                    "ON p.id = pt.review_post_id \n" +
                    "JOIN tag t ON t.id = pt.tag_id\n" +
                    "JOIN location l ON l.id = p.location_id\n" +
                    "WHERE ((:keyword IS NULL OR t.name LIKE :keyword)\n" +
                    "OR (:keyword IS NULL OR p.title LIKE :keyword))\n" +
                    "OR  (:keyword IS NULL OR ((l.city LIKE :keyword OR l.label LIKE :keyword) AND l.status = 1 AND l.type = 2))\n" +
                    "OR  (:keyword IS NULL OR l.label LIKE :keyword)\n" +
                    "AND p.status = 1")
    List<ReviewPost> searchReviewPostsNative(@Param("keyword") String keyword, Pageable pageable);

    List<ReviewPost> findByVisitors_UserAndVisitors_Status(User user, int status, Pageable pageable);

    int countByUserAndStatus(User user, int status);
}
