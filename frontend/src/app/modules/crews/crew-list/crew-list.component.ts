import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CrewService } from '../../../../core/services/crew.service';
import { Crew } from '../../../../core/models/crew.model';

@Component({
  selector: 'app-crew-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './crew-list.component.html',
  styleUrls: ['./crew-list.component.scss']
})
export class CrewListComponent implements OnInit {
  crews: Crew[] = [];
  loading = false;

  constructor(private crewService: CrewService) {}

  ngOnInit(): void {
    this.fetchCrews();
  }

  fetchCrews(): void {
    this.loading = true;
    this.crewService.getAllCrews().subscribe({
      next: (data) => {
        this.crews = data;
        this.loading = false;
      },
      error: () => {
        this.crews = [
          { id: 1, crewCode: 'CRW-ALPHA', crewName: 'Alpha Rapid Response Crew', vehicleNumber: 'FLT-TRK-101', vehicleType: 'Bucket Truck', contactPhone: '+1-555-7001', availabilityStatus: 'AVAILABLE', baseAreaId: 1, baseAreaName: 'Downtown Financial', membersCount: 4, skillSpecialization: 'High Voltage Overhead' },
          { id: 2, crewCode: 'CRW-BRAVO', crewName: 'Bravo Line Restoration Crew', vehicleNumber: 'FLT-TRK-102', vehicleType: 'Line Truck', contactPhone: '+1-555-7002', availabilityStatus: 'DISPATCHED', baseAreaId: 3, baseAreaName: 'Green Valley', membersCount: 3, skillSpecialization: 'Distribution Lines' },
          { id: 3, crewCode: 'CRW-CHARLIE', crewName: 'Charlie Substation Specialist Unit', vehicleNumber: 'FLT-VAN-201', vehicleType: 'Mobile Lab', contactPhone: '+1-555-7003', availabilityStatus: 'ON_DUTY', baseAreaId: 2, baseAreaName: 'North Industrial', membersCount: 3, skillSpecialization: 'Transformers & Switchgear' }
        ];
        this.loading = false;
      }
    });
  }
}
