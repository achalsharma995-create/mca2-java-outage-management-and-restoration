import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkOrderService } from '../../../../core/services/work-order.service';
import { WorkOrder } from '../../../../core/models/work-order.model';

@Component({
  selector: 'app-work-order-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './work-order-list.component.html',
  styleUrls: ['./work-order-list.component.scss']
})
export class WorkOrderListComponent implements OnInit {
  workOrders: WorkOrder[] = [];
  loading = false;

  constructor(private workOrderService: WorkOrderService) {}

  ngOnInit(): void {
    this.fetchWorkOrders();
  }

  fetchWorkOrders(): void {
    this.loading = true;
    this.workOrderService.getAllWorkOrders().subscribe({
      next: (data) => {
        this.workOrders = data;
        this.loading = false;
      },
      error: () => {
        this.workOrders = [
          {
            id: 1,
            woNumber: 'WO-2026-0001',
            outageId: 1,
            outageNumber: 'OUT-2026-0001',
            outageTitle: 'Fallen Pine Tree Across 11kV Feeder Line',
            outageSeverity: 'HIGH',
            outageAreaName: 'Green Valley Suburbs',
            crewName: 'Bravo Line Restoration Crew',
            crewVehicleNumber: 'FLT-TRK-102',
            priority: 'HIGH',
            status: 'WORKING',
            assignedAt: '14:40',
            estimatedDurationMinutes: 210,
            createdByUsername: 'operator1',
            createdAt: '2026-03-10 14:40'
          },
          {
            id: 2,
            woNumber: 'WO-2026-0002',
            outageId: 2,
            outageNumber: 'OUT-2026-0002',
            outageTitle: 'Underground 33kV Cable Joint Breakdown',
            outageSeverity: 'CRITICAL',
            outageAreaName: 'East Bay Waterfront',
            crewName: 'Delta Underground Team',
            crewVehicleNumber: 'FLT-VAN-202',
            priority: 'EMERGENCY',
            status: 'WORKING',
            assignedAt: '14:05',
            estimatedDurationMinutes: 300,
            createdByUsername: 'operator1',
            createdAt: '2026-03-10 14:05'
          }
        ];
        this.loading = false;
      }
    });
  }

  updateStatus(wo: WorkOrder, nextStatus: string): void {
    this.workOrderService.updateStatus(wo.id, nextStatus).subscribe({
      next: (res) => {
        wo.status = res.status;
      },
      error: () => {
        wo.status = nextStatus as any;
      }
    });
  }
}
