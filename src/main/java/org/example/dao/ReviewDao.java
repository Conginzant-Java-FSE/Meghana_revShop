package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewDao {

    public boolean addReview(Review r){

        String sql="""
INSERT INTO review(buyer_id,product_id,rating,comment,reviewed_at)
VALUES(?,?,?,?,NOW())
ON DUPLICATE KEY UPDATE rating=VALUES(rating),comment=VALUES(comment)
""";

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(sql)){

            ps.setInt(1,r.getBuyerId());
            ps.setInt(2,r.getProductId());
            ps.setInt(3,r.getRating());
            ps.setString(4,r.getComment());
            return ps.executeUpdate()>0;

        }catch(Exception e){}

        return false;
    }

    public List<Review> getProductReviews(int pid){

        List<Review> list=new ArrayList<>();

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement("""
SELECT r.rating,r.comment,b.full_name
FROM review r JOIN buyer b ON r.buyer_id=b.buyer_id
WHERE r.product_id=?
""")){

            ps.setInt(1,pid);
            var rs=ps.executeQuery();

            while(rs.next()){
                Review r=new Review();
                r.setRating(rs.getInt(1));
                r.setComment(rs.getString(2));
                r.setBuyerName(rs.getString(3));
                list.add(r);
            }

        }catch(Exception e){}

        return list;
    }

    public List<Review> getReviewsBySeller(int sellerId){

        List<Review> list=new ArrayList<>();

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement("""
SELECT p.product_name,r.rating,r.comment
FROM review r JOIN product p ON r.product_id=p.product_id
WHERE p.seller_id=?
""")){

            ps.setInt(1,sellerId);
            var rs=ps.executeQuery();

            while(rs.next()){
                Review r=new Review();
                r.setProductName(rs.getString(1));
                r.setRating(rs.getInt(2));
                r.setComment(rs.getString(3));
                list.add(r);
            }

        }catch(Exception e){}

        return list;
    }
    public List<Review> reviewsBySeller(int sellerId){

        List<Review> list=new ArrayList<>();

        String sql="""
    SELECT p.product_name,r.rating,r.comment
    FROM review r
    JOIN product p ON r.product_id=p.product_id
    WHERE p.seller_id=?
    """;

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(sql)){

            ps.setInt(1,sellerId);
            var rs=ps.executeQuery();

            while(rs.next()){
                Review r=new Review();
                r.setProductName(rs.getString(1));
                r.setRating(rs.getInt(2));
                r.setComment(rs.getString(3));
                list.add(r);
            }
        }catch(Exception e){}

        return list;
    }

}
