package com.omrs.dto.dashboard;

public class DashboardMetricsDto {

    private long activeOutages;
    private long crewsAvailable;
    private long totalCrews;
    private long restoredToday;
    private long pendingNotifications;
    private long totalAffectedCustomers;

    public DashboardMetricsDto() {}

    public DashboardMetricsDto(long activeOutages, long crewsAvailable, long totalCrews,
                               long restoredToday, long pendingNotifications, long totalAffectedCustomers) {
        this.activeOutages = activeOutages;
        this.crewsAvailable = crewsAvailable;
        this.totalCrews = totalCrews;
        this.restoredToday = restoredToday;
        this.pendingNotifications = pendingNotifications;
        this.totalAffectedCustomers = totalAffectedCustomers;
    }

    public long getActiveOutages() { return activeOutages; }
    public void setActiveOutages(long activeOutages) { this.activeOutages = activeOutages; }
    public long getCrewsAvailable() { return crewsAvailable; }
    public void setCrewsAvailable(long crewsAvailable) { this.crewsAvailable = crewsAvailable; }
    public long getTotalCrews() { return totalCrews; }
    public void setTotalCrews(long totalCrews) { this.totalCrews = totalCrews; }
    public long getRestoredToday() { return restoredToday; }
    public void setRestoredToday(long restoredToday) { this.restoredToday = restoredToday; }
    public long getPendingNotifications() { return pendingNotifications; }
    public void setPendingNotifications(long pendingNotifications) { this.pendingNotifications = pendingNotifications; }
    public long getTotalAffectedCustomers() { return totalAffectedCustomers; }
    public void setTotalAffectedCustomers(long totalAffectedCustomers) { this.totalAffectedCustomers = totalAffectedCustomers; }
}
