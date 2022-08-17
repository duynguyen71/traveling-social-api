package com.tc.tcapi.repository;

import com.tc.core.model.Post;
import com.tc.core.model.ReviewPost;
import com.tc.core.model.ReviewPostComment;
import com.tc.core.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewPostCommentRepository extends JpaRepository<ReviewPostComment,Long> {

    Optional<ReviewPostComment> findByIdAndUser(Long id, User user);

    List<ReviewPostComment> findByPost(Post post);

    List<ReviewPostComment> findByPostAndUserAndStatusAndParentIsNull(ReviewPost post, User user, Integer status);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM review_post_comment pc \n" +
                    "JOIN review_post p \n" +
                    "ON pc.post_id = p.id \n" +
                    "WHERE post_id = :postId \n" +
                    "AND pc.parent_comment_id IS NULL \n" +
                    "AND pc.status = :status \n"
    )
    List<ReviewPostComment> getPostCommentsNative(@Param("postId") Long postId, @Param("status") Integer status, Pageable pageable);

    List<ReviewPostComment> getByParentAndStatus( ReviewPostComment parent,Integer status);

    Integer countByPostAndStatusAndParentIsNull(ReviewPost post, Integer status);

    Integer countByPostAndStatus(ReviewPost post, Integer status);

    Integer countByParentAndStatus(ReviewPostComment parent,Integer status);

    Optional<ReviewPostComment> findByIdAndStatus(Long id, Integer status);

}
