package hotel.model;

import hotel.visitor.Visitor;

public interface Element {
    <R> R accept(Visitor<R> visitor);
}