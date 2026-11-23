import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OutageService } from '../../../../core/services/outage.service';
import { Outage } from '../../../../core/models/outage.model';

@Component({
  selector: 'app-outage-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './outage-list.component.html',
  styleUrls: ['./outage-list.component.scss']
})
export class OutageListComponent implements OnInit {
  outages: Outage[] = [];
  searchTerm = '';
  statusFilter = 'ALL';
  severityFilter = 'ALL';
  loading = false;

  constructor(private outageService: OutageService) {}

  ngOnInit(): void {
    this.fetchOutages();
  }

  fetchOutages(): void {
    this.loading = true;
    this.outageService.getOutages({
      search: this.searchTerm,
      status: this.statusFilter !== 'ALL' ? this.statusFilter : undefined,
      severity: this.severityFilter !== 'ALL' ? this.severityFilter : undefined
    }).subscribe({
      next: (res) => {
        this.outages = res.content || [];
        this.loading = false;
      },
      error: () => {
        // Sample fallback data if backend offline
        this.outages = [
          {
            id: 1,
            outageNumber: 'OUT-2026-0001',
            title: 'Fallen Pine Tree Across 11kV Feeder Line',
            description: 'Severe wind gust knocked tree across primary conductors.',
            areaId: 3,
            areaName: 'Green Valley Suburbs',
            areaCode: 'GVS-03',
            feederCode: 'FDR-GVS-3301',
            severity: 'HIGH',
            status: 'IN_PROGRESS',
            outageType: 'WEATHER_EVENT',
            reportedAt: '2026-03-10 14:15',
            estimatedRestoreTime: '2026-03-10 18:30',
            affectedCustomersCount: 1450,
            reportCount: 4
          },
          {
            id: 2,
            outageNumber: 'OUT-2026-0002',
            title: 'Underground 33kV Cable Joint Breakdown',
            description: 'Water ingress in marina vault causing overcurrent trip.',
            areaId: 5,
            areaName: 'East Bay Waterfront',
            areaCode: 'EBW-05',
            feederCode: 'FDR-EBW-5502',
            severity: 'CRITICAL',
            status: 'IN_PROGRESS',
            outageType: 'EQUIPMENT_FAILURE',
            reportedAt: '2026-03-10 13:40',
            estimatedRestoreTime: '2026-03-10 19:00',
            affectedCustomersCount: 3200,
            reportCount: 6
          }
        ];
        this.loading = false;
      }
    });
  }

  onFilterChange(): void {
    this.fetchOutages();
  }
}
