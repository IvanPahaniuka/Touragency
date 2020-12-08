package bean;

import bean.enums.OrderStatus;

import java.io.Serializable;
import java.util.Objects;

public class Order implements Serializable {
    private User user;
    private Book book;
    private OrderStatus status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return user.equals(order.user) &&
                book.equals(order.book) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, book, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user +
                ", book=" + book +
                ", status=" + status +
                '}';
    }
}
