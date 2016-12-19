package edu.sjsu.cmpe275.lms.entity;

import javax.persistence.*;

/**
 * User Book Cart holds temporary cart for user who wants to issue the books
 */
@Entity
@Table(name = "USER_BOOK_CART")
public class UserBookCart {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Column(name = "USER_ID")
    private int user_id;

    @Column(name = "BOOK_ID")
    private int book_id;

    @Column(name = "TYPE_RETURN")
    private int type_return;

    /**
     * THe default constructor.
     */
    public UserBookCart() {
    }

    /**
     * @param user_id
     * @param book_id
     * @param type_return
     */
    public UserBookCart(int user_id, int book_id, int type_return) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.type_return = type_return;
    }

    /**
     * Id Getter
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Id setter
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * User_Id setter
     *
     * @return user_id
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * User_Id setter
     *
     * @param user_id
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * Book_Id getter
     *
     * @return book_id
     */

    public int getBook_id() {
        return book_id;
    }

    /**
     * Book_Id setter
     *
     * @param book_id
     */
    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    /**
     *
     * @return
     */
    public int getType_return() {
        return type_return;
    }

    /**
     *
     * @param type_return
     */
    public void setType_return(int type_return) {
        this.type_return = type_return;
    }
}
