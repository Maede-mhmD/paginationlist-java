package com.example.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PageResponse<T> {
    private List<T> items;
    
    @JsonProperty("total_items")
    private long totalItems;
    
    @JsonProperty("total_pages")
    private int totalPages;
    
    @JsonProperty("current_page")
    private int currentPage;
    
    @JsonProperty("per_page")
    private int perPage;

    public PageResponse() {}

    public PageResponse(List<T> items, long totalItems, int totalPages, int currentPage, int perPage) {
        this.items = items;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.perPage = perPage;
    }

    public List<T> getItems() { return items; }
    public void setItems(List<T> items) { this.items = items; }

    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { this.totalItems = totalItems; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getPerPage() { return perPage; }
    public void setPerPage(int perPage) { this.perPage = perPage; }
}