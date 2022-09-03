package com.lahoriagency.cikolive.Classes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Comments implements List<Comments> {
    private String comment;
    private String gift;
    private String name;
    private String place;
    private String image;
    private int id;
    private int age;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(@Nullable @org.jetbrains.annotations.Nullable Object o) {
        return false;
    }

    @NonNull
    @NotNull
    @Override
    public Iterator<Comments> iterator() {
        return null;
    }

    @NonNull
    @NotNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @NotNull
    @Override
    public <T> T[] toArray(@NonNull @NotNull T[] ts) {
        return null;
    }

    @Override
    public boolean add(Comments comments) {
        return false;
    }

    @Override
    public boolean remove(@Nullable @org.jetbrains.annotations.Nullable Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull @NotNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull @NotNull Collection<? extends Comments> collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, @NonNull @NotNull Collection<? extends Comments> collection) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull @NotNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull @NotNull Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Comments get(int i) {
        return null;
    }

    @Override
    public Comments set(int i, Comments comments) {
        return null;
    }

    @Override
    public void add(int i, Comments comments) {

    }

    @Override
    public Comments remove(int i) {
        return null;
    }

    @Override
    public int indexOf(@Nullable @org.jetbrains.annotations.Nullable Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(@Nullable @org.jetbrains.annotations.Nullable Object o) {
        return 0;
    }

    @NonNull
    @NotNull
    @Override
    public ListIterator<Comments> listIterator() {
        return null;
    }

    @NonNull
    @NotNull
    @Override
    public ListIterator<Comments> listIterator(int i) {
        return null;
    }

    @NonNull
    @NotNull
    @Override
    public List<Comments> subList(int i, int i1) {
        return null;
    }
}
