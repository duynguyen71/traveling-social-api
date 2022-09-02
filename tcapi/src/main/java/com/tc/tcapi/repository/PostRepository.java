package com.tc.tcapi.repository;

import com.tc.core.enumm.EPostType;
import com.tc.tcapi.model.Post;
import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByStatusAndTypeOrderByCreateDateDesc(int status, EPostType type);

    int countByUserAndStatusAndType(User user, int status, EPostType type);

    Optional<Post> findByIdAndUser(Long postId, User u);

    List<Post> findAllByTypeAndStatus(EPostType type, int status, Pageable pageable);

//    @Query(
//            nativeQuery = true,
//            value = "SELECT p.* FROM post p JOIN user u " +
//                    "ON p.user_id = u.id " +
//                    "WHERE p.user_id=:userId " +
//                    "AND (:status IS NULL OR p.status =:status) " +
//                    "AND (:type IS NULL OR p.type =:type) " +
//                    "AND (:hour IS NULL OR p.create_date > DATE_SUB(NOW(),INTERVAL :hour HOUR) )"
//    )
//    List<Post> getUserPostsNative(@Param("userId") Long userId,
//                                  @Param("status") Integer status,
//                                  @Param("hour") Integer hour,
//                                  @Param("type") Integer type,
//                                  Pageable pageable
//    );
//
    @Query(
            nativeQuery = true,
            value = "SELECT p.* FROM post p JOIN user u " +
                    "ON p.user_id = u.id " +
                    "WHERE p.user_id=:userId " +
                    "AND (:status IS NULL OR p.status =:status) " +
                    "AND (:type IS NULL OR p.type =:type) "
    )
    List<Post> getUserPostsNative(@Param("userId") Long userId,
                                  @Param("status") Integer status,
                                  @Param("type") Integer type,
                                  Pageable pageable
    );

    @Query(
            nativeQuery = true,
            value = "SELECT p.* FROM post p \n" +
                    "JOIN user u ON p.user_id = u.id\n" +
                    "WHERE  p.user_id \n" +
                    "IN (SELECT f.user_id FROM user u \n" +
                    "JOIN follow f ON u.id = f.follower_id \n" +
                    "WHERE u.id = :userId AND f.status =1 \n" +
                    "UNION SELECT :userId) \n" +
                    "AND p.create_date > SUBDATE(NOW(),INTERVAL 9000 HOUR)\n" +
                    "AND p.type = 0 AND p.status=1"
    )
    List<Post> getUserStoriesNative(@Param("userId") Long userId, Pageable pageable);

    // Get Stories or Posts
    @Query(
            nativeQuery = true,
            value = "SELECT p.* FROM post p \n" +
                    "JOIN user u ON p.user_id = u.id\n" +
                    "WHERE  p.user_id \n" +
                    "IN (SELECT f.user_id FROM user u \n" +
                    "JOIN follow f ON u.id = f.follower_id \n" +
                    "WHERE u.id = :userId AND f.status =1 \n" +
                    "UNION SELECT :userId) \n" +
                    "AND p.type = 1 AND p.status=1"
    )
    List<Post> getPostsNative(@Param("userId") Long userId, Pageable pageable);


    @Query(
            nativeQuery = true,
            value = "SELECT * FROM post p\n" +
                    " JOIN user u \n" +
                    "ON p.user_id = u.id\n" +
                    "WHERE p.user_id = :userId AND p.type =:type"
    )
    List<Post> findByUserAndTypeAndStatus(@Param("userId") Long userId, @Param("type") Integer type, Pageable pageable);


    @Query(nativeQuery = true, value = "SELECT DISTINCT p.* FROM post p JOIN post_tag pt\n" +
            "ON p.id = pt.post_id \n" +
            "JOIN tag t ON t.id = pt.tag_id\n" +
            "WHERE ((:tagName IS NULL OR t.name LIKE :tagName)\n" +
            "OR (:title IS NULL OR p.caption LIKE :title)) AND p.type = :type AND p.status = 1")
    List<Post> searchPostsNative(@Param("tagName") String tagName,
                                       @Param("title") String title,
                                       @Param("type") int type,
                                       Pageable pageable);

    Optional<Post> findByIdAndStatus(Long id, int status);
}
