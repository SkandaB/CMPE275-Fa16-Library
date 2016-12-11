package edu.sjsu.cmpe275.lms.entity;


import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = "BOOK")
public class Book {

    @Autowired
    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    @JoinTable
            (
                    name = "USER_BOOK_WAITLIST",
                    joinColumns = {@JoinColumn(name = "BOOK_ID", referencedColumnName = "BOOK_ID")},
                    inverseJoinColumns = {@JoinColumn(name = "ID", referencedColumnName = "ID", unique = true)}
            )
    List<User> waitlist = new ArrayList<User>();

    @OneToMany(mappedBy = "book", cascade = {CascadeType.REMOVE})
    List<UserBook> currentUsers = new ArrayList<UserBook>();

    @OneToMany(mappedBy = "book", cascade = {CascadeType.REMOVE})
    List<LibUserBook> listAddUpdateUsers = new ArrayList<LibUserBook>();

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "BOOK_ID", length = 8, unique = true, nullable = false)
    private Integer bookId;

    @Column(name = "ISBN", nullable = false, unique = true)
    private String isbn;

    @Column(name = "AUTHOR", nullable = false)
    private String author;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CALLNUMBER", length = 10)
    private String callnumber;

    @Column(name = "PUBLISHER", nullable = false)
    private String publisher;

    @Column(name = "YEAR_OF_PUBLICATION")
    private String year_of_publication;
    /**
     * Location of the book in library
     */
    @Column(name = "LOCATION")
    private String location;

    @Column(name = "NUM_OF_COPIES")
    private int num_of_copies;

    @Column(name = "CURRENT_STATUS")
    private String current_status;

    @Column(name = "KEYWORDS")
    private String keywords;

    @Column(name = "IMAGE")
    private byte[] image;

    /**
     *
     */
    public Book() {
    }

    /**
     * @param isbn
     * @param author
     * @param title
     * @param callnumber
     * @param publisher
     * @param year_of_publication
     * @param location
     * @param num_of_copies
     * @param current_status
     * @param keywords
     * @param image
     */
    public Book(String isbn, String author, String title, String callnumber, String publisher, String year_of_publication, String location, int num_of_copies, String current_status, String keywords, byte[] image) {
        this.isbn = isbn;
        this.author = author;
        this.title = title;
        this.callnumber = callnumber;
        this.publisher = publisher;
        this.year_of_publication = year_of_publication;
        this.location = location;
        this.num_of_copies = num_of_copies;
        this.current_status = current_status;
        this.keywords = keywords;
        this.image = image;

    }

    /**
     * @return
     */
    public List<LibUserBook> getListAddUpdateUsers() {
        return listAddUpdateUsers;
    }

    /**
     * @param listAddUpdateUsers
     */
    public void setListAddUpdateUsers(List<LibUserBook> listAddUpdateUsers) {
        this.listAddUpdateUsers = listAddUpdateUsers;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "Book Details " + "\n" +
                " isbn='" + isbn + "\n" +
                " author='" + author + "\n" +
                " title='" + title + "\n" +
                " callnumber='" + callnumber + "\n" +
                " publisher='" + publisher + "\n" +
                " year_of_publication='" + year_of_publication + "\n";
    }

    /**
     * @return
     */
    public String printBookInfo() {
        return "ISBN: " + isbn + "\n" +
                "Author: " + author + "\n" +
                "Title'" + title + "\n" +
                "Publisher: " + publisher + "\n" +
                "Year of Publication: " + year_of_publication + "\n";
    }

    /**
     * @return
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return
     */
    public String getCallnumber() {
        return callnumber;
    }

    /**
     * @param callnumber
     */
    public void setCallnumber(String callnumber) {
        this.callnumber = callnumber;
    }

    /**
     * @return
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return
     */
    public int getNum_of_copies() {
        return num_of_copies;
    }

    /**
     * @param num_of_copies
     */
    public void setNum_of_copies(int num_of_copies) {
        this.num_of_copies = num_of_copies;
    }

    /**
     * @return
     */
    public String getCurrent_status() {
        return current_status;
    }

    /**
     * @param current_status
     */
    public void setCurrent_status(String current_status) {
        this.current_status = current_status;
    }

    /**
     * @return
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * @param keywords
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * @return
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * @param image
     */
    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * @return
     */
    public String getYear_of_publication() {
        return year_of_publication;
    }

    /**
     * @param year_of_publication
     */
    public void setYear_of_publication(String year_of_publication) {
        this.year_of_publication = year_of_publication;
    }

    /**
     * @return the waitlist
     */
    public List<User> getWaitlist() {
        return waitlist;
    }

    /**
     * @param waitlist the waitlist to set
     */
    public void setWaitlist(List<User> waitlist) {
        this.waitlist = waitlist;
    }


    /**
     * @return the currentUsers
     */
    public List<UserBook> getCurrentUsers() {
        return currentUsers;
    }

    /**
     * @param currentUsers the currentUsers to set
     */
    public void setCurrentUsers(List<UserBook> currentUsers) {
        this.currentUsers = currentUsers;
    }

    /**
     * @return the bookId
     */
    public Integer getBookId() {
        return bookId;
    }

    /**
     * @param bookId the bookId to set
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

}
