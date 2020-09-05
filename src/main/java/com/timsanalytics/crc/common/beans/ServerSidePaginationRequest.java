package com.timsanalytics.crc.common.beans;

public class ServerSidePaginationRequest<T> {
    private String nameFilter;
    private String stateFilter;
    private String sortColumn;
    private String sortDirection;
    private int pageIndex;
    private int pageSize;
    private T advancedFilters;

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public String getStateFilter() {
        return stateFilter;
    }

    public void setStateFilter(String stateFilter) {
        this.stateFilter = stateFilter;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public T getAdvancedFilters() {
        return advancedFilters;
    }

    public void setAdvancedFilters(T advancedFilters) {
        this.advancedFilters = advancedFilters;
    }
}
